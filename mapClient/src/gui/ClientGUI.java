package gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mapClient.Client;
import mapClient.ServerException;

/**
 * Interfaccia grafica client-side per l'interfacciamento con il server
 * 
 * @author Nazar Chekalin
 *
 */
public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private Client client;

	/**
	 * Avvia una schermata di collegamento con il server attraverso l'inserimento
	 * dell'indirizzo ip. In caso di ip errato segnala, tramite una finestra di
	 * dialogo, l'errore e ripristina la schermata.
	 */
	public void ClientGUIRun() {
		JFrame connect = new JFrame("Server Connection");
		connect.setLayout(new GridLayout(3, 1));
		connect.setSize(250, 150);
		connect.setLocationRelativeTo(null);
		connect.add(new JLabel("Server IP Address: "));
		JTextField text = new JTextField();
		connect.add(text);
		JButton button = new JButton("CONNECT");
		connect.add(button);
		connect.setVisible(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					boolean connected = ServerGUI(text.getText().trim(), 8080);
					if (!connected)
						JOptionPane.showMessageDialog(connect, "Wrong IP Address, try again!");
					else
						connect.setVisible(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

		connect.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowevent) {
				try {
					windowevent.getWindow().dispose();
				} finally {
					System.exit(0);
				}
			}
		});

	}

	/**
	 * Avvia un'interfaccia grafica che comunica con il server e visualizza
	 * informazioni della computazione.
	 * 
	 * @param ip   - indirizzo del server
	 * @param port - porta del server
	 * @return boolean - ritorna un valore booleano che indica se � avvenuta
	 *         correttamente la connessione con il server
	 * @throws IOException
	 */
	private boolean ServerGUI(String ip, int port) throws IOException {
		boolean connected;
		Container app = getContentPane();
		JTabbedPane tabpanel = new JTabbedPane();
		TabbedPane tab = new TabbedPane();
		app.setLayout(new GridLayout(1, 1));
		tabpanel.addTab("DATABASE", tab.panelDB);
		tabpanel.addTab("FILE", tab.panelFile);
		app.add(tabpanel);
		this.client = new Client();
		connected = client.connect(ip, port);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					client.close();
					event.getWindow().dispose();
				} catch (IOException | ServerException | ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					System.exit(0);
				}
			}
		});
		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		if (!connected)
			setVisible(false);
		return connected;
	}

	/**
	 * Gestisce una finestra grafica con due pannelli tab
	 * 
	 * @author Nazar Chekalin
	 *
	 */
	private class TabbedPane extends JPanel {
		private static final long serialVersionUID = 1L;
		private JPanelTabPanel panelDB;
		private JPanelTabPanel panelFile;

		/**
		 * Gestisce la finestra di tab
		 * 
		 * @author Nazar Chekalin
		 *
		 */
		private class JPanelTabPanel extends JPanel {
			private static final long serialVersionUID = 1L;
			private JTextField tableText = new JTextField(10);
			private JTextField choiceText = new JTextField(10);
			private JTextArea output = new JTextArea();
			private JButton execute;
			private JButton sendChoice;

			/**
			 * Crea un template per la finestra tab, con un layout di tipo Grid 2x1
			 * 
			 * @param button - Stringa che indica il testo sul tasto execute per il calcolo
			 *               dell'albero decisionale
			 * @param action - interfaccia listener per la ricezione di un evento
			 * @param send   - interfaccia listener per la ricezione di un evento
			 */
			private JPanelTabPanel(String button, ActionListener action, ActionListener send) {
				this.setLayout(new GridLayout(2, 1));
				output.setEditable(false);
				choiceText.setEditable(false);
				JScrollPane topPanel = new JScrollPane(output);
				JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				downPanel.add(new JLabel("Table Name: "));
				downPanel.add(tableText);
				execute = new JButton(button);
				sendChoice = new JButton("Predict");
				downPanel.add(execute);
				downPanel.add(sendChoice);
				downPanel.add(choiceText);
				execute.addActionListener(action);
				sendChoice.addActionListener(send);
				sendChoice.setEnabled(false);
				choiceText.addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent event) {
						choiceText.setText("");
					}

					public void focusLost(FocusEvent event) {
						// nothing
					}
				});
				this.add(topPanel);
				this.add(downPanel);
				this.setVisible(true);
			}
		}

		/**
		 * Costruttore di classe per l'istanzia del template per la finestra tab
		 */
		private TabbedPane() {
			panelDB = new JPanelTabPanel("Learn RT", new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					try {
						panelDB.output.setText("");
						learningFromDB();
						panelDB.sendChoice.setEnabled(true);
					} catch (ClassNotFoundException | IOException e) {
						JOptionPane.showMessageDialog(panelDB, "Operation failed");
					} catch (ServerException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					}
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try {
						panelDB.choiceText.setEditable(true);
						panelDB.execute.setEnabled(false);
						panelFile.execute.setEnabled(false);
						panelFile.sendChoice.setEnabled(false);
						predictClass(panelDB);
						if (client.isPredicted()) {
							panelDB.execute.setEnabled(true);
							panelFile.execute.setEnabled(true);
							panelFile.sendChoice.setEnabled(true);
						}
					} catch (ClassNotFoundException | IOException e) {
						JOptionPane.showMessageDialog(panelDB, "Operation failed");
					} catch (ServerException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					}
				}
			});
			panelFile = new JPanelTabPanel("Store from file", new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					try {
						panelFile.output.setText("");
						learningFromFile();
						panelFile.sendChoice.setEnabled(true);
					} catch (ClassNotFoundException | IOException e1) {
						JOptionPane.showMessageDialog(panelFile, "Operation failed!");
					} catch (ServerException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage());
					}
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try {
						panelFile.choiceText.setEditable(true);
						panelFile.execute.setEnabled(false);
						panelDB.execute.setEnabled(false);
						panelDB.sendChoice.setEnabled(false);
						predictClass(panelFile);
						if (client.isPredicted()) {
							panelFile.execute.setEnabled(true);
							panelDB.execute.setEnabled(true);
							panelDB.sendChoice.setEnabled(true);
						}
					} catch (ClassNotFoundException | IOException e) {
						JOptionPane.showMessageDialog(panelDB, "Operation failed");
					} catch (ServerException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					}
				}
			});

			this.add(panelFile);
			this.add(panelDB);
		}

		/**
		 * Invia il contenuto del textfield, relativo alla scelta decisionale
		 * dell'albero, al metodo predictClass dell'oggetto client ed appende all'area
		 * di testo relativa all'output nel pannello tab le informazioni ricevute
		 * dall'oggetto client.
		 * 
		 * @param panel - finestra tab, all'interno della quale verranno stampate le
		 *              informazioni relative all'albero decisionale
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws ServerException
		 */
		private void predictClass(JPanelTabPanel panel)
				throws SocketException, IOException, ClassNotFoundException, ServerException {
			String choice;
			String results;
			choice = panel.choiceText.getText().trim();
			results = client.predictClass(choice);

			panel.output.append(results);
		}

		/**
		 * Invia il nome della tabella, contenuto nell'area di testo relativa,
		 * all'oggetto client che restituisce l'albero decisionale sotto forma di
		 * stringa. Il contenuto della stringa verr� stampato nell'area di testa
		 * relativo all'output. e visualizza un messaggio di operazione completata in
		 * caso di successo.
		 * 
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws ServerException
		 */
		private void learningFromDB() throws SocketException, IOException, ClassNotFoundException, ServerException {
			String tableName;
			String results;
			try {
				tableName = panelDB.tableText.getText().trim();
				client.storeTableFromDb(tableName);
				results = client.learningFromDbTable();

				panelDB.output.append(results);

				// Save results to FILE
				client.storeTreeInFile(tableName);
			} catch (Exception e) {
				throw new ServerException(e.getMessage());
			}
			JOptionPane.showMessageDialog(this, "Operation completed successfully!");
		}

		/**
		 * Invia il nome del file, contenuto nell'area di testo relativa, all'oggetto
		 * client che restituisce l'albero decisionale, appreso dal file, sotto forma di
		 * stringa. Il contenuto � stampato nell'area di testo relativo all'output nel
		 * pannello tab relativo. Compare un messaggio di operazione completata in caso
		 * di successo.
		 * 
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws ServerException
		 */
		private void learningFromFile() throws SocketException, IOException, ClassNotFoundException, ServerException {
			String tableName;
			String results;
			tableName = panelFile.tableText.getText().trim();
			results = client.learningFromFile(tableName);

			panelFile.output.setText(results);
			JOptionPane.showMessageDialog(this, "Operation completed successfully!");
		}
	}

	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI();
		gui.ClientGUIRun();
	}
}
