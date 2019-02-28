package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;

public class Client {

	private static final String CLASS_NAME = Client.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	private String username;
	private String password;
	private String host;
	private int port;
	private Socket echoSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean isConnected;


	public Client(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.isConnected = false;
	}
	
	public String getUsername() {
		return this.username;
	}


	public boolean connect() {
		//logger.log(Level.CONFIG, host + " " + port);
		try {
			echoSocket = new Socket(host, port);
			in = new ObjectInputStream(echoSocket.getInputStream());
			out = new ObjectOutputStream(echoSocket.getOutputStream());
			logger.log(Level.CONFIG, "Connecting the user: " + username);
			out.writeObject(username);
			out.writeObject(password);
			isConnected = (Boolean) in.readObject();
			
			return isConnected;
		} catch ( IOException | ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error to Connect the Client", e);
		}

		return false;
	}
	
	public boolean disconnect() {
		try {
			if(out != null) {
				out.close();
			}
			if(in != null) {
				in.close();
			}
			if(echoSocket != null) {
				echoSocket.close();
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error to close Socket/Streams", e);
			return false;
		}
		return true;
	}


	
	public boolean isConnected() {
		return isConnected;
	}
	
	public Message sendMsg(Message msgSent) {
		try {
			out.writeObject(msgSent);
			Message response = (Message) in.readObject();
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Error to resquest/response", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Send message class not found", e);
		}
		return null;
	}

}
