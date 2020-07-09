package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	private int PORT = 8080;

	public static void main(String[] args) {
		new MultiServer(8080);
	}

	public MultiServer(int port) {
		PORT = port;
		run();
	}

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

