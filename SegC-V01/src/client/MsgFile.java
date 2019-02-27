package client;

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
	            	for(int i = 1; i < parsedInput.length; i++) {
	            		System.out.println(parsedInput[i]);
	            		//send parsedInput[i]
	            		//print result for parsedInput[i] if added or not
	            	}
	                break;
	            case "list":
	            	if(parsedInput.length == 1) {
	            		System.out.println("list");
		            	msgSent = new Message(OpCode.LIST_FILES, client.getUsername());
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
	            	}else {
	            		incompleteCommand();
	            	}
	                break;
	            case "remove": //remove <files>
	            	System.out.println("remove");
	            	for(int i = 1; i < parsedInput.length; i++) {
	            		System.out.println(parsedInput[i]);
	            		//send parsedInput[i]
	            		//print result for parsedInput[i] if removed or not
	            	}
	                break;
	            case "users":
	            	if(parsedInput.length == 1) {
	            		System.out.println("users");
		            	msgSent = new Message(OpCode.USERS);
		            	//send message method
		            	msgResponse = client.sendMsg(msgSent);
	            	}else {
	            		incompleteCommand();
	            	}

	                break;
	            case "trusted": //trusted <trustedUserIDs>
	            	System.out.println("trusted");
	            	for(int i = 1; i < parsedInput.length; i++) {
	            		System.out.println(parsedInput[i]);
	            		//send parsedInput[i]
	            		//print result for parsedInput[i] if trusted or not
	            	}
	                break;
	            case "untrusted": //untrusted <untrustedUserIDs>
	            	System.out.println("untrusted");
	            	for(int i = 1; i < parsedInput.length; i++) {
	            		System.out.println(parsedInput[i]);
	            		//send parsedInput[i]
	            		//print result for parsedInput[i] if untrusted or not
	            	}
	                break;
	            case "download": //download <userID> <file>
	            	System.out.println("download");
	            	if(parsedInput.length == 3) {
	            		String user = parsedInput[1];
	            		String nameFile = parsedInput[2];
	            		//download file
	            	}else {
	            		
	            	}
	                break;
	            case "msg": //msg <userID> <msg>
	            	System.out.println("msg");
	            	if(parsedInput.length >= 3) {
	            		String userReceiver = parsedInput[1];
	            		/*StringBuilder msgBuilder = new StringBuilder();
	            		for(int i = 2; i < parsedInput.length; i++) {
		            		msgBuilder.append(parsedInput[i] + " ");
		            	}
	            		String msg = msgBuilder.toString();*/
	            		String msg = rawInput.substring(3+2+userReceiver.length());
	            		//send message
	            		System.out.println("destino:" + userReceiver + " msg:" + msg);
	            		String[] arrSent = new String[3];
	            		arrSent[0] = client.getUsername();
	            		arrSent[1] = userReceiver;
	            		arrSent[2] = msg;
	            		msgSent = new Message(OpCode.SEND_MSG, arrSent);
	            	}else {
	            		
	            	}

	                break;
	            case "collect":
	            	System.out.println("collect");

	                break;
	            case "help":
	            	System.out.println("help");

	                break;
	            case "exit":
	            	client.disconnect();
	            	onLoop = false;
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
