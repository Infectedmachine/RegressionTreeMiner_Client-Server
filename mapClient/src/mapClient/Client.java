package mapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import utility.Keyboard;

public class Client {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean predicted = false;
	private boolean inPrediction = false;

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

	public void storeTableFromDb(String tabName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
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

	public void storeTreeInFile(String fileName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject("4");
		out.writeObject(fileName);

		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);

	}

	private void closeSocket() throws SocketException, ServerException, IOException, ClassNotFoundException {
		if (!inPrediction)
			out.writeObject("5");
		in.close();
		out.close();
		socket.close();
	}

	public String predictClass(String msg)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		if (predicted && !msg.toUpperCase().equals("Y")) {
			return "Prediction already computed, would you start a new one? (y/n)\n";
		} else if (msg.toUpperCase().equals("Y") && !inPrediction) {
			predicted = false;
			msg = "start";
		}
		if (msg.equals("start") && !inPrediction) {
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
				return "Input Should be numeric!";
			}
			out.writeObject(Integer.parseInt(msg));
			msg = in.readObject().toString();
			if (msg.equals("QUERY")) {
				msg = in.readObject().toString();
				return msg;
			} else {
				msg = in.readObject().toString();
				predicted = true;
				inPrediction = false;
				return msg;
			}

		}

	}

	public void close() throws SocketException, ServerException, IOException, ClassNotFoundException {
		this.closeSocket();
	}
}
