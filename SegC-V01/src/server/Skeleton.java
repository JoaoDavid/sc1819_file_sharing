package server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import communication.Network;
import communication.OpCode;
import communication.OpResult;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.services.UserService;
import facade.startup.MsgFileServerApp;


public class Skeleton {

	private String userName;
	private Socket socket;
	private PrivateKey privKey;
	private PublicKey pubKey;

	private FileService fileService;
	private MessageService msgService;
	private UserService userService;

	public Skeleton(String userName, Socket socket, FileService fileService, 
			MessageService msgService, UserService userService, PrivateKey privKey, PublicKey pubKey) {
		this.userName = userName;
		this.socket = socket;
		this.fileService = fileService;
		this.msgService = msgService;
		this.userService = userService;
		this.privKey = privKey;
		this.pubKey = pubKey;
	}

	public boolean communicate(ObjectOutputStream outStream, ObjectInputStream inStream){
		boolean connected = true;
		try {
			Object obj = inStream.readObject();
			OpCode opcode;
			if(obj == null || !(obj instanceof OpCode)) {
				opcode = OpCode.END_CONNECTION;
			}else {
				opcode = (OpCode) obj;
			}			

			switch (opcode) {
			case STORE_FILES: //store <files>
				this.uploadFile();
				break;
			case LIST_FILES: //list
				System.out.println("entrei");
				this.listFiles();
				break;
			case REMOVE_FILES: //remove <files>
				this.removeFiles();
				break;
			case USERS: //users
				this.listUsers();
				break;
			case TRUST_USERS: //trusted <trustedUserIDs>
				this.trustUsers();
				break;
			case UNTRUST_USERS: //untrusted <untrustedUserIDs>
				this.untrustUsers();
				break;
			case DOWNLOAD_FILE: //download <userID> <file>
				this.downloadFile();
				break;
			case SEND_MSG: //msg <userID> <msg>
				this.storeMessage();
				break;
			case COLLECT_MSG: //collect
				this.collectMsg();
				break;
			case END_CONNECTION:
				connected = false;
				break;
			default:

				break;
			}
		} catch (IOException e) {
			connected = false;
			System.out.println("Lost connection with client");
		} catch (ClassNotFoundException e) {
			connected = false;
			System.out.println("ClassNotFoundException on communicate");
		} catch (ApplicationException e) {
			// TODO: handle exception
		}
		return connected;
	}



	

	private void uploadFile() {
		fileService.storeFile(userName, socket, pubKey);
	}

	private void downloadFile() {
		try {
			List<String> msg = Network.bufferToList(socket);
			String userOwner = msg.get(0);
			String fileName = msg.get(1);
			System.out.println(userOwner + " " + fileName);
			fileService.clientDownloadFile(this.userName, userOwner, fileName, socket, privKey, pubKey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void endConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void removeFiles() {
		try {
			List<String> files = Network.bufferToList(socket);
			List<String> res = new ArrayList<String>();
			for(String curr : files) {
				boolean deleted = fileService.removeFile(this.userName, curr);
				if(deleted) {
					res.add("DELETED");
				}else {
					res.add("error");
				}
			}
			Network.listToBuffer(res, socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void listUsers() throws ApplicationException {
		Network.listToBuffer(userService.listUsers(), socket);
	}

	private void listFiles() throws ApplicationException {
		String[] arr = fileService.listFiles(this.userName);

		Network.listToBuffer(Arrays.asList(arr), socket);
	}

	private void trustUsers() throws ApplicationException {
		try {
			List<String> users = Network.bufferToList(socket);
			List<String> res = new ArrayList<String>();
			for(String curr : users) {
				boolean trusted = userService.trustUser(userName, curr, privKey, pubKey);
				if(trusted) {
					res.add("TRUSTED");
				}else {
					res.add("error");
				}
			}
			Network.listToBuffer(res, socket);
		} catch (IOException e) {
			throw new ApplicationException("Error trusting users");
		}
	}

	private void untrustUsers() throws ApplicationException {
		try {
			List<String> users = Network.bufferToList(socket);
			List<String> res = new ArrayList<String>();
			for(String curr : users) {
				boolean untrusted = userService.untrustUser(userName, curr, privKey, pubKey);
				if(untrusted) {
					res.add("UNTRUSTED");
				}else {
					res.add("error");
				}
			}
			Network.listToBuffer(res, socket);
		} catch (IOException e) {
			throw new ApplicationException("Error untrusting users");
		}
	}

	private void storeMessage() throws ApplicationException {
		try {
			List<String> msg = Network.bufferToList(socket);
			msgService.storeMsg(this.userName, msg.get(0), msg.get(1), privKey, pubKey);
			Network.sendInt(OpResult.SUCCESS, socket);
		} catch (IOException e) {
			Network.sendInt(OpResult.ERROR, socket);
		} catch (ApplicationException e) {
			Network.sendInt(e.getCode(), socket);
		}
	}
	
	private void collectMsg() throws ApplicationException {
		List<String> msg = msgService.collectMessages(this.userName, privKey, pubKey);
		Network.listToBuffer(msg, socket);
	}

}
