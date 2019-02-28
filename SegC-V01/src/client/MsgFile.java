package client;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
				System.out.println("Write your password please");
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
			/*String parsedInput = rawInput;
			int lastIndex = rawInput.indexOf(' ');
			if(lastIndex != -1) {
				parsedInput = rawInput.substring(0, lastIndex);
			}*/
			String[] parsedInput = rawInput.split("(\\s)+");
			
			switch (parsedInput[0]) {
	            case "store": //store <files>
	            	System.out.println("store");
	            	/*
	            	 * 
	            	 * falta fazer, arranjar maneira de enviar os ficheiros
	            	 * dentro do messages
	            	 */
	                break;
	            case "list":
	            	if(parsedInput.length == 1) {
	            		System.out.println("list");
		            	msgSent = new Message(OpCode.LIST_FILES);
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
		            	if (msgResponse != null) {
		            		for(String curr : msgResponse.getArrStrParam()) {
		            			System.out.println(curr);
		            		}
		            	}
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "remove": //remove <files>
	            	if(parsedInput.length > 1) {
	            		System.out.println("remove");
	            		String[] arrSent = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);
	            		msgSent = new Message(OpCode.REMOVE_FILES,arrSent);
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
		            	OpCode[] arrCodes = msgResponse.getOpCodeArr();
		            	for(int i = 0; i < arrSent.length; i++) {
		            		System.out.println(arrSent[i] + ":" + arrCodes[i].toString());
		            	}
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "users":
	            	if(parsedInput.length == 1) {
	            		System.out.println("users");
		            	msgSent = new Message(OpCode.USERS);
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
		            	if (msgResponse != null) {
		            		for(String curr : msgResponse.getArrStrParam()) {
		            			System.out.println(curr);
		            		}
		            	}		            	
	            	}else {
	            		incompleteCommand();
	            	}

	                break;
	            case "trusted": //trusted <trustedUserIDs>
	            	if(parsedInput.length > 1) {
	            		System.out.println("trusted");
	            		String[] arrSent = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);
	            		msgSent = new Message(OpCode.TRUST_USERS,arrSent);
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
		            	OpCode[] arrCodes = msgResponse.getOpCodeArr();
		            	for(int i = 0; i < arrSent.length; i++) {
		            		System.out.println(arrSent[i] + ":" + arrCodes[i].toString());
		            	}
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "untrusted": //untrusted <untrustedUserIDs>
	            	if(parsedInput.length > 1) {
	            		System.out.println("untrusted");
	            		String[] arrSent = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);
	            		msgSent = new Message(OpCode.UNTRUST_USERS,arrSent);
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "download": //download <userID> <file>
	            	System.out.println("download");
	            	if(parsedInput.length == 3) {
	            		String[] arrSent = new String[2];
	            		arrSent[0] = parsedInput[1];//users name account that has the file
	            		arrSent[1] = parsedInput[2];//name of the file
	            		//download file
	            		msgSent = new Message(OpCode.DOWNLOAD_FILE, arrSent);
	            		msgResponse = client.sendMsg(msgSent);
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "msg": //msg <userID> <msg>
	            	System.out.println("msg");
	            	if(parsedInput.length >= 3) {
	            		String userReceiver = parsedInput[1];
	            		String msg = rawInput.substring(3+2+userReceiver.length());
	            		//send message
	            		System.out.println("destino:" + userReceiver + " msg:" + msg);
	            		String[] arrSent = new String[2];
	            		arrSent[0] = userReceiver;//receiver
	            		arrSent[1] = msg;//text message
	            		msgSent = new Message(OpCode.SEND_MSG, arrSent);
	            		msgResponse = client.sendMsg(msgSent);
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "collect":
	            	if(parsedInput.length == 1) {
	            		System.out.println("collect");
		            	msgSent = new Message(OpCode.COLLECT_MSG, client.getUsername());
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "help":
	            	System.out.println("help");

	                break;
	            case "exit":
	            	if(parsedInput.length == 1) {
	            		System.out.println("exit");
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
		System.out.println("Error: unrecognized or incomplete command line");
	}


}
