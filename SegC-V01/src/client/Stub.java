package client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import communication.Network;
import communication.OpCodeDM;
import facade.exceptions.ApplicationException;

public class Stub {

	private String username;
	//private Socket socket;
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
			//socket = new Socket(host, port);
			//socket.startHandshake();
			inObj = new ObjectInputStream(socket.getInputStream());
			outObj = new ObjectOutputStream(socket.getOutputStream());
			outObj.writeObject(username);
			outObj.writeObject(password);
			try {
				Object obj = inObj.readObject();
				if(obj != null && obj instanceof OpCodeDM) {
					OpCodeDM OpCodeDM = (OpCodeDM) obj;
					if(OpCodeDM == OpCodeDM.OP_SUCCESSFUL) {
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


	public List<String> rpcSendReceiveList(OpCodeDM OpCodeDM, List<String> list) {
		try {
			outObj.writeObject(OpCodeDM);
			Network.listToBuffer(list, socket);
			return Network.bufferToList(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<String> rpcReceiveList(OpCodeDM OpCodeDM) {
		try {
			outObj.writeObject(OpCodeDM);
			return Network.bufferToList(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void rpcEndConnectiont() {
		try {
			outObj.writeObject(OpCodeDM.END_CONNECTION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean rpcDownloadFileFromServer(List<String> msg) {
		try {
			outObj.writeObject(OpCodeDM.DOWNLOAD_FILE);
			Network.listToBuffer(msg, socket);
			return Network.receiveFile("", socket,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public List<String> rpcUploadFileToServer(String filePath) {
		try {
			File file = new File(filePath);
			if(file.exists()) {
				outObj.writeObject(OpCodeDM.STORE_FILES);
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

	public void rpcSendList(OpCodeDM OpCodeDM, List<String> list) {
		try {
			outObj.writeObject(OpCodeDM);
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
