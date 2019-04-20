package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import communication.Network;
import communication.OpCode;
import facade.exceptions.ApplicationException;

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
	
	
	public List<String> rpcSendReceiveList(OpCode opcode, List<String> list) {
		try {
			outObj.writeObject(opcode);
			Network.listToBuffer(list, socket);
			return Network.bufferToList(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> rpcReceiveList(OpCode opcode) {
		try {
			outObj.writeObject(opcode);
			return Network.bufferToList(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void rpcEndConnectiont() {
		try {
			outObj.writeObject(OpCode.END_CONNECTION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rpcDownloadFileFromServer(List<String> msg) {
		try {
			outObj.writeObject(OpCode.DOWNLOAD_FILE);
			Network.listToBuffer(msg, socket);
			Network.receiveFile("", socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	


}