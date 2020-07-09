package mapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	private ObjectOutputStream out;
	private ObjectInputStream in; 


	public boolean connect(String ip, int port) throws IOException {
		boolean connected;

		try {
			InetAddress addr = InetAddress.getByName(ip);
			Socket socket = new Socket(addr, port);
			System.out.print(socket);
			connected = true;
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (SocketException | UnknownHostException e) {
			connected = false;
		}
		return connected;
	}
	public String learningFromFile(String tabName) throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("2");
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (result.equals("OK"))
			return (String) in.readObject();
		else
			throw new ServerException(result);

	}

	public void storeTableFromDb(String tabName) throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("0");
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);

	}

	public String learningFromDbTable() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("1");
		String result = (String) in.readObject();
		if (result.equals("OK")) {
			return (String) in.readObject();
		} else
			throw new ServerException(result);

	}

	public void storeTreeInFile(String fileName) throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("4");
		out.writeObject(fileName);

		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);

	}

	private void closeSocket() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("5");
	}

	public void close() throws SocketException, ServerException, IOException, ClassNotFoundException{
		this.closeSocket();
	}
}
