package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Modella la classe per la gestione del server multi-client.
 * @author Nazar Chekalin
 *
 */
public class MultiServer {
	private int PORT = 8080;

	public static void main(String[] args) {
		new MultiServer(8080);
	}

	/**
	 * Costruttore di classe, avvia la sessione del server sulla porta data.
	 * @param port - porta su cui avviare il server.
	 */
	public MultiServer(int port) {
		PORT = port;
		run();
	}

	/**
	 * Avvia il server
	 */
	private void run() {
		try {
			ServerSocket s = new ServerSocket(PORT);
			System.out.println("Server Started");
			try {
				while (true) {
					Socket socket = s.accept();
					try {
						new ServerOneClient(socket);
					} catch (IOException e) {
						socket.close();
						e.printStackTrace();
					}
				}
			} finally {
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

