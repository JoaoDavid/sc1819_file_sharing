package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import communication.OpCode;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.startup.MsgFileServerApp;


public class Skeleton {
	
	private String userName;
	private Socket socket;

	
	private FileService fileService;
	private MessageService msgService;
	
	public Skeleton(String userName, Socket socket, FileService fileService, MessageService msgService) {
		this.userName = userName;
		this.socket = socket;
		this.fileService = fileService;
		this.msgService = msgService;
	}

	public void communicate() throws ApplicationException {
		try {
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			Object obj = inStream.readObject();
			OpCode opcode;
			if(obj == null || !(obj instanceof OpCode)) {
				opcode = OpCode.END_CONNECTION;
			}else {
				opcode = (OpCode) obj;
			}			
			
			switch (opcode) {
			case STORE_FILES: //store <files>

				break;
			case LIST_FILES: //list
				this.listFiles();

				break;
			case REMOVE_FILES: //remove <files>

				break;
			case USERS: //users

				break;
			case TRUST_USERS: //trusted <trustedUserIDs>

				break;
			case UNTRUST_USERS: //untrusted <untrustedUserIDs>

				break;
			case DOWNLOAD_FILE: //download <userID> <file>

				break;
			case SEND_MSG: //msg <userID> <msg>

				break;
			case COLLECT_MSG: //collect

				break;
			case END_CONNECTION:
				
				break;
			default:

				break;
			}
		} catch (IOException e) {
			throw new ApplicationException("IOException on communicate");
		} catch (ClassNotFoundException e) {
			throw new ApplicationException("ClassNotFoundException on communicate");
		}

		
	}
	
	private void listFiles() throws ApplicationException {
		String[] list = fileService.listFiles(this.userName);
		
		try {
			//BufferedOutputStream outBuff = new BufferedOutputStream(socket.getOutputStream());
			//List<Byte[]> listByte = new ArrayList<Byte[]>();
			socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(list.length).array());
			//for(String curr : list) {
			//	byte[] byteCurrStr = curr.getBytes();
			//	outBuff.write(ByteBuffer.allocate(4).putInt(byteCurrStr.length).array());
			//	outBuff.write(byteCurrStr);
			//}
			//int lenBuffer = 1337;//calcular
			//socket.getOutputStream().write(list.length);
			//First send the number of bytes that will be sent
			//byte[] bytes = ByteBuffer.allocate(4).putInt(lenBuffer).array();
			//socket.getOutputStream().write(bytes);
			//Then sends those bytes
		} catch (IOException e) {
			throw new ApplicationException("Error sending list of files");
		}
	}

}
