package client;

import java.util.Scanner;

public class MsgFile {

	private static Scanner sc = new Scanner(System.in);

	//args <serverAddress> <localUserID> [password]
	//127.0.0.1:23456 fernando pass123
	public static void main(String[] args) {
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
			   System.out.print("Client failed: Invalid serverAddress");
			   return;
			}
			System.out.println(hostPort[0] + " " + port);
			Client client1 = new Client(args[1], passwd, hostPort[0], port);
			if(client1.connect()) {
				System.out.println("Connected to the server");
				parser(client1);
			}else{
				System.out.println("Login failed");
			}
			
			client1.disconnect();
			System.out.println("END");
			sc.close();
		}
		
	}
	
	public static void parser(Client client) {
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
	            	System.out.println("list");

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
	            	System.out.println("users");

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
	            	System.out.println("Command not recognized");
	            	break;
				}
		}
		sc.close();
	}

}
