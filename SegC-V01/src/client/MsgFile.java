package client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.OpCode;
import communication.OpResult;
import facade.exceptions.ApplicationException;


public class MsgFile {

	private static final String CLASS_NAME = MsgFile.class.getName();

	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	private String userName;
	private Stub stub;


	public MsgFile(String userName, String host, int port) {
		this.userName = userName;
		this.stub = new Stub(userName, host, port);
	}
	
	//args:    127.0.0.1:23456 fernando pass .\keystore\myClient.keyStore
	public static void main(String[] args) throws ApplicationException, IOException {		
		Scanner sc = new Scanner(System.in);
		if(args.length == 3 || args.length == 4) {
			//System.setProperty("javax.net.ssl.trustStore", "keystore" + File.separator + "myClient.keyStore");
			System.setProperty("javax.net.ssl.trustStore", args[3]);
			String[] hostPort = args[0].split(":");
			//System.out.print(hostPort[0] + hostPort[1]);
			int port;
			String passwd;

			if(args.length == 4) {
				passwd = args[2];
			}else {
				System.out.println("Write your password please\n>>>");
				passwd = sc.nextLine();
			}
			try {
				port = Integer.parseInt(hostPort[1]);
			}
			catch (NumberFormatException e){
				System.out.println("Client failed: Invalid serverAddress:" + e.toString());
				sc.close();
				return;
			}
			System.out.println("Connecting to " + hostPort[0] + ":" + port + " ...");
			MsgFile app = new MsgFile(args[1], hostPort[0], port);
			if(app.connect(passwd)) {
				System.out.println("Connected to the server");
				System.out.println("Welcome " + args[1]);
				File userDirectory = new File(ClientConst.FOLDER_CLIENT_USERS + File.separator + args[1]);
				if(!userDirectory.exists()) {
					userDirectory.mkdirs();
					userDirectory.createNewFile();
				}
				app.startParser();
				app.disconnect();
			}else{
				System.out.println("Login failed");
				sc.close();
				return;
			}
			app.disconnect();
			System.out.println("Disconnected");
			sc.close();
		}else {
			System.out.println("Your args are not correct");
			System.out.println("Valid args: <serverAddress> <username> <userPassword> <truststoreLocation>");
			System.out.println("example: 127.0.0.1:23456 fernando pass .\\keystore\\myClient.keyStore");
		}		
	}

	public boolean connect(String password) {
		return this.stub.connect(password);
	}

	public boolean disconnect() {
		return this.stub.disconnect();
	}

	/**
	 * @requires client != null && client.isConnected()
	 * @param client
	 * @throws ApplicationException 
	 */
	public void startParser() throws ApplicationException {
		Scanner sc = new Scanner(System.in);
		boolean onLoop = true;
		while(onLoop) {
			System.out.print(userName + ">>>");
			String rawInput = sc.nextLine();
			String[] parsedInput = rawInput.split("(\\s)+");

			switch (parsedInput[0]) {
			case "store": //store <files>
				if(parsedInput.length > 1) {
					for(int i = 1; i < parsedInput.length ; i++){
						List<String> res = this.stub.rpcUploadFileToServer(parsedInput[i]);
						if(res == null) {
							System.out.println(parsedInput[i] + " : File not found");
						}else {
							System.out.println(parsedInput[i] + " : " + res.get(0));
						}
					}
				}else {
					incompleteCommand();
				}
				break;
			case "list":
				if(parsedInput.length == 1) {
					List<String> res = this.stub.rpcReceiveList(OpCode.LIST_FILES);
					if(!res.isEmpty()) {
						for(String curr : res) {
							System.out.println(curr);
						}
					}else {
						System.out.println("You have no files in the server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "remove": //remove <files>
				if(parsedInput.length > 1) {
					List<String> files = Arrays.asList(Arrays.copyOfRange(parsedInput, 1, parsedInput.length));
					List<String> res = this.stub.rpcSendReceiveList(OpCode.REMOVE_FILES, files);
					//files and res must have the same size
					//just to avoid nullPointerException, use min
					for(int i = 0; i < Math.min(files.size(), res.size()); i++) {
						System.out.println(files.get(i) + " : " + res.get(i));
					}
				}else {
					incompleteCommand();
				}
				break;
			case "users":
				if(parsedInput.length == 1) {
					List<String> res = this.stub.rpcReceiveList(OpCode.USERS);
					if(!res.isEmpty()) {
						for(String curr : res) {
							System.out.println(curr);
						}
					}else {
						System.out.println("No users registered");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "trusted": //trusted <trustedUserIDs>
				if(parsedInput.length > 1) {
					List<String> users = Arrays.asList(Arrays.copyOfRange(parsedInput, 1, parsedInput.length));
					List<String> res = this.stub.rpcSendReceiveList(OpCode.TRUST_USERS, users);
					//both lists must have the same size
					//just to avoid nullPointerException, use min
					for(int i = 0; i < Math.min(users.size(), res.size()); i++) {
						System.out.println(users.get(i) + " : " + res.get(i));
					}
				}else {
					incompleteCommand();
				}
				break;
			case "untrusted": //untrusted <untrustedUserIDs>
				if(parsedInput.length > 1) {
					List<String> users = Arrays.asList(Arrays.copyOfRange(parsedInput, 1, parsedInput.length));
					List<String> res = this.stub.rpcSendReceiveList(OpCode.UNTRUST_USERS, users);
					//both lists must have the same size
					//just to avoid nullPointerException, use min
					for(int i = 0; i < Math.min(users.size(), res.size()); i++) {
						System.out.println(users.get(i) + " : " + res.get(i));
					}
				}else {
					incompleteCommand();
				}
				break;
			case "download": //download <userID> <file>
				logger.log(Level.CONFIG, "download");
				if(parsedInput.length == 3) {
					List<String> msg = new ArrayList<>();
					String userOwner = parsedInput[1];//users name account that has the file
					String fileName = parsedInput[2];//name of the file
					msg.add(userOwner);
					msg.add(fileName);					
					this.stub.rpcDownloadFileFromServer(msg);
				}else {
					incompleteCommand();
				}
				break;
			case "msg": //msg <userID> <msg>
				logger.log(Level.CONFIG, "msg");
				if(parsedInput.length >= 3) {
					String userReceiver = parsedInput[1];
					String msg = rawInput.substring(rawInput.indexOf(userReceiver) + userReceiver.length());
					List<String> recMsg = new ArrayList<>();
					recMsg.add(userReceiver);
					recMsg.add(msg);
					this.stub.rpcSendList(OpCode.SEND_MSG, recMsg);
					try {
						int resCode = this.stub.rpcReceiveInt();
						System.out.println(OpResult.getDesig(resCode));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					incompleteCommand();
				}
				break;
			case "collect":
				if(parsedInput.length == 1) {
					List<String> res = this.stub.rpcReceiveList(OpCode.COLLECT_MSG);
					if(!res.isEmpty()) {
						for(String curr : res) {
							System.out.println(curr);
						}
					}else {
						System.out.println("Your inbox is empty");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "help":
				logger.log(Level.CONFIG, "help");
				System.out.println("Commands available");				
				System.out.println("store <files>");
				System.out.println("list");
				System.out.println("remove <files>");
				System.out.println("users");
				System.out.println("trusted <trustedUserIDs>");
				System.out.println("untrusted <untrustedUserIDs>");
				System.out.println("download <userID> <file>");
				System.out.println("msg <userID> <msg>");
				System.out.println("collect");
				System.out.println("exit");
				System.out.println("-------------------");
				break;
			case "exit":
				if(parsedInput.length == 1) {
					this.stub.rpcEndConnectiont();
					//disconnect();
					sc.close();
					return;
				}else {
					incompleteCommand();
				}
				break;
			default:
				System.out.println("Command is not recognized");
				break;
			}
		}
		sc.close();
	}

	public static void incompleteCommand() {
		System.out.println("ERROR: incomplete command line");
	}


}
