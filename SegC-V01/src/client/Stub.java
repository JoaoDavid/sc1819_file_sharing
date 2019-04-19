package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import communication.OpCode;

public class Stub {
	
	private String username;
	private Socket socket;
	private String host;
	private int port;
	
	private ObjectInputStream inObj;
	private ObjectOutputStream outObj;
	private boolean isConnected;
	
	
	
	public Stub(String userName, String host, int port) {
		this.username = userName;
		this.host = host;
		this.port = port;
	}

	public boolean connect(String password) {
		try {
			socket = new Socket(host, port);
			inObj = new ObjectInputStream(socket.getInputStream());
			outObj = new ObjectOutputStream(socket.getOutputStream());
			outObj.writeObject(username);
			outObj.writeObject(password);
			try {
				Object obj = inObj.readObject();
				if(obj != null && obj instanceof OpCode) {
					OpCode opcode = (OpCode) obj;
					if(opcode == OpCode.OP_SUCCESSFUL) {
						this.isConnected = true;
						return this.isConnected;
					}
				}
				return false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch ( IOException e) {
		}
		return false;
	}
	/**
	 * Disconnect() from Socket and Streams
	 * @return without errors True
	 */
	public boolean disconnect() {
		try {
			if(outObj != null) {
				outObj.close();
			}
			if(inObj != null) {
				inObj.close();
			}
			if(socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			//logger.log(Level.SEVERE, "Error to close Socket/Streams", e);
			return false;
		}
		this.isConnected = false;
		return true;
	}
	/**
	 * Connection with server 
	 * @return true if connected otherwise false
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	public void remoteList() {
		
	}

}
