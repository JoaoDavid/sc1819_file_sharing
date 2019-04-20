package server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import communication.Network;
import communication.OpCode;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.services.UserService;
import facade.startup.MsgFileServerApp;


public class Skeleton {

	private String userName;
	private Socket socket;


	private FileService fileService;
	private MessageService msgService;
	private UserService userService;

	public Skeleton(String userName, Socket socket, FileService fileService, MessageService msgService, UserService userService) {
		this.userName = userName;
		this.socket = socket;
		this.fileService = fileService;
		this.msgService = msgService;
		this.userService = userService;
	}

	public void communicate(ObjectOutputStream outStream, ObjectInputStream inStream) throws ApplicationException {
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

				break;
			case LIST_FILES: //list
				System.out.println("entrei");
				this.listFiles();

				break;
			case REMOVE_FILES: //remove <files>
				this.removeFiles();
				break;
			case USERS: //users

				break;
			case TRUST_USERS: //trusted <trustedUserIDs>
				this.trustUsers();
				break;
			case UNTRUST_USERS: //untrusted <untrustedUserIDs>
				this.untrustUsers();
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
			e.printStackTrace();
			//throw new ApplicationException("IOException on communicate");
		} catch (ClassNotFoundException e) {
			throw new ApplicationException("ClassNotFoundException on communicate");
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

	private void listFiles() throws ApplicationException {
		String[] arr = fileService.listFiles(this.userName);

		try {
			Network.listToBuffer(Arrays.asList(arr), socket);
		} catch (IOException e) {
			throw new ApplicationException("Error sending list of files");
		}
	}

	private void trustUsers() throws ApplicationException {
		try {
			List<String> users = Network.bufferToList(socket);
			List<String> res = new ArrayList<String>();
			for(String curr : users) {
				boolean trusted = userService.trustUser(userName, curr);
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
				boolean untrusted = userService.untrustUser(userName, curr);
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

}
