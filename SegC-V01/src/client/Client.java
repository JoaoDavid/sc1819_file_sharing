package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private String username;
	private String password;
	private String host;
	private int port;
	private Socket echoSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	
	public Client(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	
	public boolean connect() {
		//System.out.println(host + " " + port);
		try {
			echoSocket = new Socket(host, port);
			in = new ObjectInputStream(echoSocket.getInputStream());
			out = new ObjectOutputStream(echoSocket.getOutputStream());
			out.writeObject(username);
			out.writeObject(password);
			boolean fromServer = (boolean) in.readObject();
			return fromServer;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean disconnect() {
		try {
			out.close();
			in.close();
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	

}
