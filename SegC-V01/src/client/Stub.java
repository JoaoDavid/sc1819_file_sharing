package client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import communication.Network;
import communication.OpCode;

public class Stub {

	private String username;
	private SSLSocket socket;
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
			SocketFactory sf = SSLSocketFactory.getDefault();
			socket = (SSLSocket) sf.createSocket(host, port);
			inObj = new ObjectInputStream(socket.getInputStream());
			outObj = new ObjectOutputStream(socket.getOutputStream());
			List<String> logInfo = new ArrayList<String>();
			logInfo.add(username);
			logInfo.add(password);
			Network.listToBuffer(logInfo, socket);
			try {
				Object obj = inObj.readObject();
				if(obj instanceof OpCode) {
					OpCode opcode = (OpCode) obj;
					if(opcode == OpCode.OP_SUCCESSFUL) {
						this.isConnected = true;
						return this.isConnected;
					}
				}
				return false;
			} catch (ClassNotFoundException e) {
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
			Network.receiveFile(ClientConst.FOLDER_CLIENT_USERS + File.separator + username, socket,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> rpcUploadFileToServer(String filePath) {
		try {
			File file = new File(filePath);
			if(file.exists()) {
				outObj.writeObject(OpCode.STORE_FILES);
				Network.sendFile(file, socket);
				List<String> res = Network.bufferToList(socket);
				return res;
			}else {
				//file not found
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void rpcSendList(OpCode opcode, List<String> list) {
		try {
			outObj.writeObject(opcode);
			Network.listToBuffer(list, socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public int rpcReceiveInt() throws IOException {
		return Network.receiveInt(socket);
	}




}
