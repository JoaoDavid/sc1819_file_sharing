package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import communication.Message;
import communication.OpCode;


public class MsgFile {

	private static final String CLASS_NAME = MsgFile.class.getName();

	private final static Logger logger = Logger.getLogger(CLASS_NAME);


	//args <serverAddress> <localUserID> [password]
	//127.0.0.1:23456 fernando pass123
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		if(args.length == 2 || args.length == 3) {
			String[] hostPort = args[0].split(":");
			//System.out.print(hostPort[0] + hostPort[1]);
			int port;
			String passwd = null;

			if(args.length == 3) {
				passwd = args[2];
			}else {
				System.out.print("Write your password please\n>>>");
				passwd = sc.nextLine();
			}
			try {
				port = Integer.parseInt(hostPort[1]);
			}
			catch (NumberFormatException e){
				logger.log(Level.SEVERE,"Client failed: Invalid serverAddress", e);
				sc.close();
				return;
			}
			logger.log(Level.INFO,"Connecting to " + hostPort[0] + " " + port);
			Client client = new Client(args[1], passwd, hostPort[0], port);
			if(client.connect()) {
				logger.log(Level.INFO,"Connected to the server");
				//Process command lines
				parser(client);
			}else{
				logger.log(Level.INFO,"Login failed");
			}

			client.disconnect();
			logger.log(Level.INFO,"Client Disconnected");
			sc.close();
		}

	}

	/**
	 * @requires client != null && client.isConnected()
	 * @param client
	 */
	public static void parser(Client client) {
		Scanner sc = new Scanner(System.in);
		Message msgSent;
		Message msgResponse;
		boolean onLoop = true;
		while(onLoop) {
			System.out.print(">>>");
			String rawInput = sc.nextLine();
			String[] parsedInput = rawInput.split("(\\s)+");

			switch (parsedInput[0]) {
			case "store": //store <files>
				if(parsedInput.length > 1) {
					logger.log(Level.CONFIG, "store");
					File file;
					ArrayList<String> nameFiles = new ArrayList<>();
					ArrayList<Byte[]> byteFiles = new ArrayList<>();
					ArrayList<String> nonExistent = new ArrayList<>();
					for(int i = 1; i < parsedInput.length ; i++){
						file = new File(parsedInput[i]);
						if(file.exists()){
							try {
								byteFiles.add(toObjects(Files.readAllBytes(file.toPath())));
								nameFiles.add(file.getName());
							} catch (IOException e) {
								logger.log(Level.SEVERE, "N�o foi possivel converter para bytes", e);
							}
						}else {
							nonExistent.add(parsedInput[i]);
						}
					}
					if(nameFiles.size() != 0) {
						msgSent = new Message(OpCode.STORE_FILES, nameFiles, byteFiles);
						msgResponse = client.sendMsg(msgSent);
					}else {
						System.out.println("Input files were not found : No files sent");
						break;
					}
					if (msgResponse != null && msgResponse.getOpCode() == OpCode.OP_RES_ARRAY) {
						OpCode[] arrCodes = msgResponse.getOpCodeArr();
						int i = 0;
						System.out.println("--- Operation results ---");
						for(String str : nameFiles) {
							if(arrCodes[i] == OpCode.OP_SUCCESSFUL) {
								System.out.println(str + " : " + "STORED");
							}else {
								System.out.println(str + " : " + arrCodes[i].toString());
							}
							i++;
						}
						if(nonExistent.size() != 0) {
							for(String str : nonExistent) {
								System.out.println(str+ " : not found");
							}
						}
						System.out.println("-------------------------");
					}else {
						System.out.println("ERROR: no answer from server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "list":
				if(parsedInput.length == 1) {
					logger.log(Level.CONFIG, "list");
					msgSent = new Message(OpCode.LIST_FILES);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if (msgResponse != null && msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
						if(msgResponse.getArrStrParam().length != 0) {
							System.out.println("--- Files in the server ---");
							for(String curr : msgResponse.getArrStrParam()) {
								System.out.println(curr);
							}
							System.out.println("---------------------------");
						}else {
							System.out.println("You have no files in the server");
						}	
					}else {
						System.out.println("error: no answer from server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "remove": //remove <files>
				if(parsedInput.length > 1) {
					logger.log(Level.CONFIG, "remove");
					String[] arrSent = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);
					msgSent = new Message(OpCode.REMOVE_FILES,arrSent);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if (msgResponse != null && msgResponse.getOpCode() == OpCode.OP_RES_ARRAY) {
						OpCode[] arrCodes = msgResponse.getOpCodeArr();
						for(int i = 0; i < arrSent.length; i++) {
							if(arrCodes[i] == OpCode.OP_SUCCESSFUL) {
								System.out.println(arrSent[i] + " : " + "DELETED");
							}else {
								System.out.println(arrSent[i] + " : " + arrCodes[i].toString());
							}
						}
					}else {
						System.out.println("ERROR: no answer from server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "users":
				if(parsedInput.length == 1) {
					logger.log(Level.CONFIG, "users");
					msgSent = new Message(OpCode.USERS);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if (msgResponse != null && msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
						if(msgResponse.getArrStrParam().length != 0) {
							System.out.println("--- Users registered in the server ---");
							for(String curr : msgResponse.getArrStrParam()) {
								System.out.println(curr);
							}
							System.out.println("--------------------------------------");
						}else {
							System.out.println("No users registered in the server");
						}	
					}else {
						System.out.println("ERROR: no answer from server");
					}	            	
				}else {
					incompleteCommand();
				}
				break;
			case "trusted": //trusted <trustedUserIDs>
				if(parsedInput.length > 1) {
					logger.log(Level.CONFIG, "trusted");
					String[] arrSent = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);
					msgSent = new Message(OpCode.TRUST_USERS,arrSent);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if (msgResponse != null && msgResponse.getOpCode() == OpCode.OP_RES_ARRAY) {
						OpCode[] arrCode = msgResponse.getOpCodeArr();
						System.out.println("--- Operation results ---");
						for(int i = 0; i < arrSent.length; i++) {
							System.out.println(arrSent[i] + " : " + arrCode[i]);
						}
						System.out.println("--------------------------------------");

					}else {
						System.out.println("ERROR: no answer from server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "untrusted": //untrusted <untrustedUserIDs>
				if(parsedInput.length > 1) {
					logger.log(Level.CONFIG, "untrusted");
					String[] arrSent = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);
					msgSent = new Message(OpCode.UNTRUST_USERS,arrSent);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if (msgResponse != null && msgResponse.getOpCode() == OpCode.OP_RES_ARRAY) {
						OpCode[] arrCode = msgResponse.getOpCodeArr();
						System.out.println("--- Operation results ---");
						for(int i = 0; i < arrSent.length; i++) {
							System.out.println(arrSent[i] + " : " + arrCode[i]);
						}
						System.out.println("--------------------------------------");

					}else {
						System.out.println("ERROR: no answer from server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "download": //download <userID> <file>
				logger.log(Level.CONFIG, "download");
				if(parsedInput.length == 3) {
					String[] arrSent = new String[2];
					arrSent[0] = parsedInput[1];//users name account that has the file
					arrSent[1] = parsedInput[2];//name of the file
					//download file
					msgSent = new Message(OpCode.DOWNLOAD_FILE, arrSent);
					msgResponse = client.sendMsg(msgSent);
					if(msgResponse != null) {
						if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
							//File tempFile = new File("MsgFileResources" + File.separator + "client" 
							//		+ File.separator + client.getUsername() + File.separator + msgResponse.getStr());					
							File fileDown = new File(ClientConst.FOLDER_CLIENT_USERS + File.separator + client.getUsername() + 
									File.separator + ClientConst.FOLDER_DOWNLOADS + File.separator + arrSent[1]);
							try{
								if(fileDown.exists()) {
									fileDown.delete();
									System.out.println("File will be replaced with the new one that was downloaded");
								}								
								fileDown.getParentFile().mkdirs(); 
								fileDown.createNewFile();
								FileOutputStream fos = new FileOutputStream(fileDown);
								fos.write(toPrimitives(msgResponse.getArrByte()));
								fos.close();
								System.out.println("File downloaded to");
								System.out.println("File path: " + fileDown.getPath());
							}catch(Exception e){
								System.out.println("error downloading file");
								e.printStackTrace();
							}	
						}else if(msgResponse.getOpCode() == OpCode.ERR_NOT_FOUND){
							System.out.println(arrSent[1] + " : " + msgResponse.getOpCode());
						}else if(msgResponse.getOpCode() == OpCode.ERR_NOT_TRUSTED){
							System.out.println("error: you are not on " + arrSent[0] + "'s trusted list");
						}else {
							System.out.println(msgResponse.getOpCode());
						}
					}else {
						System.out.println("ERROR: no answer from server");
					}
				}else {
					incompleteCommand();
				}
				break;
			case "msg": //msg <userID> <msg>
				logger.log(Level.CONFIG, "msg");
				if(parsedInput.length >= 3) {
					String userReceiver = parsedInput[1];
					String msg = rawInput.substring(rawInput.indexOf(userReceiver) + userReceiver.length());
					//send message
					System.out.println("To:" + userReceiver + " msg:" + msg);
					String[] arrSent = new String[2];
					arrSent[0] = userReceiver;//receiver
					arrSent[1] = msg;//text message
					if(client.getUsername().equals(userReceiver)) {
						System.out.println("msg not sent : can't send a msg to yourself");
					}else {
						msgSent = new Message(OpCode.SEND_MSG, arrSent);
						msgResponse = client.sendMsg(msgSent);
						if(msgResponse != null) {
							if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
								System.out.println("Message Sent Successfully to " + userReceiver);
							}else if(msgResponse.getOpCode() == OpCode.ERR_NOT_REGISTERED) {
								System.out.println("error: " + userReceiver + " not registered in the server");
							}else if(msgResponse.getOpCode() == OpCode.ERR_NOT_TRUSTED) {
								System.out.println("error: you are not on " + userReceiver + "'s trusted list");
							}else{
								System.out.println("ERROR: msg not sent");
							}
						}else {
							System.out.println("ERROR: no answer from server");
						}
					}
				}else {
					incompleteCommand();
				}
				break;
			case "collect":
				if(parsedInput.length == 1) {
					logger.log(Level.CONFIG, "collect");
					msgSent = new Message(OpCode.COLLECT_MSG);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if(msgResponse != null) {
						if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
							if(msgResponse.getArrListStr().size() == 0) {
								System.out.println("You don't have new messages in the server");
							}else {
								File inboxFile = new File(ClientConst.FOLDER_CLIENT_USERS + File.separator + client.getUsername() + 
										File.separator + ClientConst.FILE_NAME_INBOX);

								try {
									inboxFile.getParentFile().mkdirs();
									inboxFile.createNewFile();
									FileWriter fileWriter = new FileWriter(inboxFile,true);
									System.out.println("--- Messages collected from the server ---");
									System.out.println("---    and stored in your inbox file   ---");
									for (String str : msgResponse.getArrListStr()) {
										fileWriter.write(str + System.getProperty("line.separator"));
										System.out.println(str);
									}
									fileWriter.close();
									System.out.println("------------------------------------------");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									System.out.println("Error saving messages in your inbox");
								}
							}
						}else {
							System.out.println(msgResponse.getOpCode());
						}
					}else {
						System.out.println("ERROR: no answer from server");
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
					logger.log(Level.CONFIG, "exit");
					msgSent = new Message(OpCode.END_CONNECTION);
					msgResponse = client.sendMsg(msgSent);
					if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
						client.disconnect();
						onLoop = false;
					}		            	
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

	public static Byte[] toObjects(byte[] bytesPrim) {
		Byte[] bytes = new Byte[bytesPrim.length];
		int i = 0;
		for (byte b : bytesPrim){
			bytes[i++] = b;
		}
		return bytes;
	}
	/**
	 * Turn array of Byte[] to byte[]
	 * @param oBytes
	 * @return
	 */
	private static byte[] toPrimitives(Byte[] oBytes) {

		byte[] bytes = new byte[oBytes.length];
		for(int i = 0; i < oBytes.length; i++){
			bytes[i] = oBytes[i];
		}
		return bytes;

	}
}
