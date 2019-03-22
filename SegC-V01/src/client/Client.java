package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;
/**
 * Responsible to connect to the Server!
 */
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

	/**
	 * Constructor
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * @requires all parameters != null
	 */
	public Client(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.isConnected = false;
	}
	/**
	 * Get the UserName to Login
	 * @return userName
	 */
	public String getUsername() {
		return this.username;
	}
	/**
	 * Connect to the Server
	 * @return True if connected otherwise False
	 */
	public boolean connect() {
		//logger.log(Level.CONFIG, host + " " + port);
		try {
			echoSocket = new Socket(host, port);
			in = new ObjectInputStream(echoSocket.getInputStream());
			out = new ObjectOutputStream(echoSocket.getOutputStream());
			logger.log(Level.CONFIG, "Connecting the user: " + username);
			out.writeObject(username);
			out.writeObject(password);
			 Message response = (Message) in.readObject();
			 if(response != null && response.getOpCode() == OpCode.OP_SUCCESSFUL){
				 isConnected = true;
				 return isConnected;
			 }else if(response != null && response.getOpCode() == OpCode.ERR_ALREADY_EXISTS){
				 System.out.println("You are already logged in: Existing Session");
			 }else if(response != null && response.getOpCode() == OpCode.OP_ERROR) {
				 System.out.println("Username and password do not match");
			 }
		} catch ( IOException | ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error to Connect the Client", e);
		}

		return false;
	}
	/**
	 * Disconnect() from Socket and Streams
	 * @return without errors True
	 */
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
	/**
	 * Connection with server 
	 * @return true if connected otherwise false
	 */
	public boolean isConnected() {
		return isConnected;
	}
	/**
	 * Send the message to Server
	 * @param msgSent
	 * @return if receive response return Message otherwise null
	 * @requires msgSent != null
	 */
	public Message sendMsg(Message msgSent) {
		try {
			out.writeObject(msgSent);
			Object obj = in.readObject();
			if(obj instanceof Message){
				Message response = (Message) obj;
				return response;
			}else{
				logger.log(Level.SEVERE, "Different object send by socket");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error to resquest/response", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Send message class not found", e);
		}
		return null;
	}

}
