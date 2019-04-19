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
	
	public List<String> remoteList() throws ApplicationException {
		try {					
			outObj.writeObject(OpCode.LIST_FILES);	
			/*
			byte[] buffLenByte = new byte[4];
			socket.getInputStream().read(buffLenByte);
			int buffLen = ByteBuffer.wrap(buffLenByte).getInt();
			System.out.println("buffLen:" +buffLen);
			
			byte[] buff = new byte[buffLen];
			int read = socket.getInputStream().read(buff);
			if(read != buffLen) {//information lost
				throw new ApplicationException("Information incomplete");
			}
			
			int i = 0;
			byte[] strLenBytes = Arrays.copyOfRange(buff, i, i + 4);
			int nStrs = ByteBuffer.wrap(strLenBytes).getInt();
			System.out.println("num strings:"+nStrs);
			i+=4;
			String[] result2 = new String[nStrs];
			int index = 0;
			while(i < buffLen) {
				strLenBytes = Arrays.copyOfRange(buff, i, i + 4);
				int currStrNumBytes = ByteBuffer.wrap(strLenBytes).getInt();
				i+=4;
				String str = new String(Arrays.copyOfRange(buff, i, i+currStrNumBytes));
				System.out.println(str);
				i+=currStrNumBytes;
				result2[index] = str;
				index++;
			}
			return result;*/
			return Network.bufferToList(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> remoteRemoveFiles(List<String> files) {
		try {
			outObj.writeObject(OpCode.REMOVE_FILES);
			Network.listToBuffer(files, socket);
			return Network.bufferToList(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
