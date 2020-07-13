package mapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Gestisce l'interfacciamento diretto con il server attraverso chiamate a relativi metodi. 
 * 
 * @author Nazar Chekalin
 *
 */
public class Client {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean predicted = false;
	private boolean inPrediction = false;

	/**
	 * Effettua la connessione con il server
	 * @param ip - indirizzo ip del server
	 * @param port - indirizzo di porta del server
	 * @return boolean - valore booleano che indica il successo della connessione
	 * @throws IOException
	 */
	public boolean connect(String ip, int port) throws IOException {
		boolean connected;

		try {
			InetAddress addr = InetAddress.getByName(ip);
			this.socket = new Socket(addr, port);
			System.out.print(socket);
			connected = true;
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (SocketException | UnknownHostException e) {
			connected = false;
		}
		return connected;
	}

	/**
	 * Invia al server il commando relativo al recupero dell'albero decisionale da un file .dmp
	 * @param tabName - nome del file
	 * @return String - Stringa contenente l'albero decisionale in forma di testo
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String learningFromFile(String tabName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("2");
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (result.equals("OK"))
			return (String) in.readObject();
		else
			throw new ServerException(result);

	}

	/**
	 * Invia al server il commando relativo all'apprendimento degli esempi da un database
	 * @param tabName - nome della tabella all'interno del database
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void storeTableFromDb(String tabName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("0");
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);

	}

	/**
	 * Invia al server il commando relativo all'apprendimento dell'albero decisionale
	 * @return String - Stringa contenente l'albero decisionale in forma di testo
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String learningFromDbTable() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("1");
		String result = (String) in.readObject();
		if (result.equals("OK")) {
			return (String) in.readObject();
		} else
			throw new ServerException(result);

	}

	/**
	 * Invia al server il commando relativo al salvataggio dell'albero decisionale all'interno di un file .dmp
	 * @param fileName - nome del file in cui salvare l'intero albero decisionale
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void storeTreeInFile(String fileName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("4");
		out.writeObject(fileName);

		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);

	}

	/**
	 * Invia al server il commando per terminare la sessione
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void closeSocket() throws SocketException, ServerException, IOException, ClassNotFoundException {
		if (!inPrediction && !socket.isOutputShutdown())
			out.writeObject("5");
		else if (socket.isOutputShutdown()) {
			// do nothing
		} else {
			in.close();
			out.close();
			socket.close();
		}
	}

	/**
	 * Apre una sessione interattiva con l'utente per il traversamento dell'albero decisionale
	 * @param msg - Stringa contenente la decisione per il traversamento (indica il successivo nodo o foglia su cui spostarsi)
	 * @return Stringa - Stringa contenente la risposta del server alla decisione presa
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String predictClass(String msg)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		if (predicted && !msg.toUpperCase().equals("Y")) {
			return "\nPrediction already computed, would you start a new one? (y/n)\n";
		} else if (msg.toUpperCase().equals("Y") && !inPrediction) {
			predicted = false;
		}
		if (!inPrediction) {
			inPrediction = true;
			out.writeObject("3");
			msg = in.readObject().toString();
			msg = "Starting prediction phase!\n";
			msg += in.readObject().toString();
			return msg;
		} else {
			try {
				Integer.parseInt(msg);
			} catch (Exception e) {
				return "Input Should be numeric!\n";
			}
			out.writeObject(Integer.parseInt(msg));
			msg = in.readObject().toString();
			if (msg.equals("QUERY")) {
				msg = in.readObject().toString();
				return msg;
			} else if (msg.equals("OK")) {
				msg = in.readObject().toString();
				predicted = true;
				inPrediction = false;
				return msg;
			} else {
				predicted = true;
				inPrediction = false;
				return msg;
			}

		}

	}
	
	/**
	 * Controlla se vi è una sessione di predizione dell'albero decisionale attualmente aperta
	 * @return boolean - valore booleano che indica se vi è una sessione decisionale avviata. 
	 */
	public boolean isPredicted() {
		return this.predicted;
	}

	/**
	 * Effettua la chiusura della sessione
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void close() throws SocketException, ServerException, IOException, ClassNotFoundException {
		this.closeSocket();
	}
}
