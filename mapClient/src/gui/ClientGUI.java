package gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private Client client;

	public void ClientGUIRun() {
		JFrame connect = new JFrame("Server Connection");
		connect.setLayout(new GridLayout(3, 1));
		connect.setSize(500, 200);
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
		return connected;
	}

	private class TabbedPane extends JPanel {
		private static final long serialVersionUID = 1L;
		private JPanelCluster panelDB;
		private JPanelCluster panelFile;

		private class JPanelCluster extends JPanel {
			private static final long serialVersionUID = 1L;
			private JTextField tableText = new JTextField(30);
			//private JTextField radiusText = new JTextField(10);
			private JTextArea output = new JTextArea();
			private JButton execute;

			private JPanelCluster(String button, ActionListener action) {
				this.setLayout(new GridLayout(3, 1));
				JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				output.setEditable(false);
				JScrollPane centralPanel = new JScrollPane(output);
				JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				topPanel.add(new JLabel("Table Name: "));
				topPanel.add(tableText);
				execute = new JButton(button);
				downPanel.add(execute);
				execute.addActionListener(action);
				this.add(topPanel);
				this.add(centralPanel);
				this.add(downPanel);
				this.setVisible(true);
			}
		}

		private TabbedPane() {
			panelDB = new JPanelCluster("Start Mining", new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					try {
						panelDB.output.setText("");
						learningFromDB();
					} catch (ClassNotFoundException | IOException e) {
						JOptionPane.showMessageDialog(panelDB, "Operation failed");
					} catch (ServerException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					}
				}
			});
			panelFile = new JPanelCluster("Store from file", new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					try {
						panelFile.output.setText("");
						learningFromFile();
					} catch (ClassNotFoundException | IOException e1) {
						JOptionPane.showMessageDialog(panelFile, "Operation failed!");
					} catch (ServerException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage());
					}
				}
			});

			this.add(panelFile);
			this.add(panelDB);
		}

		private void learningFromDB() throws SocketException, IOException, ClassNotFoundException, ServerException {
			String tableName;
			String results;
			tableName = panelDB.tableText.getText().trim();
			client.storeTableFromDb(tableName);
			results = client.learningFromDbTable();

			panelDB.output.append("Tree:\n" + results);

			// Save results to FILE
			client.storeTreeInFile(tableName);
			JOptionPane.showMessageDialog(this, "Operation completed successfully!");
		}

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
