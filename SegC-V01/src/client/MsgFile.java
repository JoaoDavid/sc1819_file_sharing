package client;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			
			//check if valueOf(null or string not present in enum) is possible
			OpCode param = OpCode.OP_INVALID;
			try {
				param = OpCode.valueOf(parsedInput[0]);
			} catch (Exception e) {
				logger.log(Level.CONFIG,"Invalid convert of command line");
			}
					
			switch (param) {
			case STORE_FILES: //store <files>
				System.out.println("store");
				for(int i = 1; i < parsedInput.length; i++) {
					System.out.println(parsedInput[i]);
					//send parsedInput[i]
					//print result for parsedInput[i] if added or not
				}
				break;
			case LIST_FILES:
				System.out.println("list");

				break;
			case REMOVE_FILES: //remove <files>
				System.out.println("remove");
				for(int i = 1; i < parsedInput.length; i++) {
					System.out.println(parsedInput[i]);
					//send parsedInput[i]
					//print result for parsedInput[i] if removed or not
				}
				break;
			case USERS:
				System.out.println("users");

				break;
			case TRUST_USERS: //trusted <trustedUserIDs>
				System.out.println("trusted");
				for(int i = 1; i < parsedInput.length; i++) {
					System.out.println(parsedInput[i]);
					//send parsedInput[i]
					//print result for parsedInput[i] if trusted or not
				}
				break;
			case UNTRUST_USERS: //untrusted <untrustedUserIDs>
				System.out.println("untrusted");
				for(int i = 1; i < parsedInput.length; i++) {
					System.out.println(parsedInput[i]);
					//send parsedInput[i]
					//print result for parsedInput[i] if untrusted or not
				}
				break;
			case DOWNLOAD_FILE: //download <userID> <file>
				System.out.println("download");
				if(parsedInput.length == 3) {
					String user = parsedInput[1];
					String nameFile = parsedInput[2];
					//download file
				}else {

				}
				break;
			case SEND_MSG: //msg <userID> <msg>
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
				}else {

				}

				break;
			case SHOW_MSG:
				System.out.println("collect");

				break;
			case OP_HELP:
				System.out.println("help");

				break;
			case END_CONNECTION:
				client.disconnect();
				onLoop = false;
				break;
			default:
				System.out.println("Command not recognized");
				break;
			}
		}
		sc.close();
	}

}
