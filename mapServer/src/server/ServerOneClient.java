package server;

import java.io.IOException;
import data.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.FileNotFoundException;
import data.Data;
import tree.RegressionTree;
import tree.UnknownValueException;

/**
 * Modella la classe per la gestione del server a singolo client. Estende la classe Thread
 * @author Nazar Chekalin
 *
 */
public class ServerOneClient extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Data data;
	private RegressionTree tree;

	/**
	 * Costruttore di classe, inizializza il client socket e I/O Stream.
	 * @param s - client socket
	 * @throws IOException
	 */
	public ServerOneClient(Socket s) throws IOException {
		socket = s;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		System.out.println(socket + " is connected!");
		start();
	}

	@Override
	public void run() {
		boolean check = true;
		String request;
		String tableName;
		String rulesString;
		String treeString;

		while (check) {
			try {
				request = (String) in.readObject();
				switch (request) {
				case "0": // LEARN FROM DB
					tableName = (String) in.readObject();
					data = new Data(tableName);
					out.writeObject("OK");
					break;

				case "1": // COMPUTE
					tree = new RegressionTree(data);
					out.writeObject("OK");
					rulesString = "********* RULES **********\n";
					rulesString += tree.getRulesString();
					rulesString += "*************************\n";
					treeString = "********* TREE **********\n";
					treeString += tree.toString();
					treeString += "*************************\n";
					out.writeObject(rulesString + treeString);
					break;

				case "4": // SAVE TO DMP FILE
					tableName = (String) in.readObject();
					tree.save(tableName + ".dmp");
					out.writeObject("OK");
					break;

				case "2": // LEARN FROM FILE DMP
					tableName = (String) in.readObject();
					tree = RegressionTree.load(tableName + ".dmp");
					out.writeObject("OK");
					rulesString = "********* RULES **********\n";
					rulesString += tree.getRulesString();
					rulesString += "*************************\n";
					treeString = "********* TREE **********\n";
					treeString += tree.toString();
					treeString += "*************************\n";
					out.writeObject(rulesString + treeString);
					break;

				case "5": // DISCONNECT
					System.out.println(socket + " is disconnected!");
					out.close();
					in.close();
					socket.close();
					check = false;
					break;

				case "3": // INTERACTIVE PHASE
					tree.predictClass(in, out);
					break;

				}
			} catch (TrainingDataException e) {
				e.printStackTrace();
				try {
					out.writeObject(e.getMessage());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (UnknownValueException unknown) {
				unknown.printStackTrace();
				try {
					out.writeObject(unknown.getMessage());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (FileNotFoundException notfile) {
				notfile.printStackTrace();
				try {
					out.writeObject("FILE NOT FOUND!\n");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (IOException | ClassNotFoundException ioex) {
				System.out.println(socket + " connection dropped!");
				ioex.printStackTrace();
				check = false;
				try {
					out.close();
					in.close();
					socket.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
