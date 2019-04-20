package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.server.RemoteRef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;
import facade.exceptions.ApplicationException;


public class MsgFileDM {

	private static final String CLASS_NAME = MsgFileDM.class.getName();

	private final static Logger logger = Logger.getLogger(CLASS_NAME);
	
	private String userName;
	private Stub stub;
	
	
	public MsgFileDM(String userName, String host, int port) {
		this.userName = userName;
		this.stub = new Stub(userName, host, port);
	}
	
	//args <serverAddress> <localUserID> [password]
	//127.0.0.1:23456 fernando pass123
	public static void main(String[] args) throws ApplicationException {
		Scanner sc = new Scanner(System.in);
		if(args.length == 2 || args.length == 3) {
			String[] hostPort = args[0].split(":");
			//System.out.print(hostPort[0] + hostPort[1]);
			int port;
			String passwd;

			if(args.length == 3) {
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
			MsgFileDM app = new MsgFileDM(args[1], hostPort[0], port);
			if(app.connect(passwd)) {
				System.out.println("Connected to the server");
				System.out.println("Welcome " + args[1]);
				app.startParser();
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
			System.out.println("Valid args: <serverAddress> <localUserID> [password]");
			System.out.println("example: 127.0.0.1:23456 fernando pass");
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
			System.out.print(">>>");
			String rawInput = sc.nextLine();
			String[] parsedInput = rawInput.split("(\\s)+");

			switch (parsedInput[0]) {
			case "store": //store <files>
				if(parsedInput.length > 1) {

				}else {
					incompleteCommand();
				}
				break;
			case "list":
				if(parsedInput.length == 1) {
					List<String> result = this.stub.remoteList();
					for(String curr : result) {
						System.out.println(curr);
					}
				}else {
					incompleteCommand();
				}
				break;
			case "remove": //remove <files>
				if(parsedInput.length > 1) {
					List<String> files = Arrays.asList(Arrays.copyOfRange(parsedInput, 1, parsedInput.length));
					List<String> res = this.stub.remoteRemoveFiles(files);
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
						            	
				}else {
					incompleteCommand();
				}
				break;
			case "trusted": //trusted <trustedUserIDs>
				if(parsedInput.length > 1) {
					List<String> users = Arrays.asList(Arrays.copyOfRange(parsedInput, 1, parsedInput.length));
					List<String> res = this.stub.remoteProcedureCall(OpCode.TRUST_USERS, users);
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
					List<String> res = this.stub.remoteProcedureCall(OpCode.UNTRUST_USERS, users);
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
					
				}else {
					incompleteCommand();
				}
				break;
			case "msg": //msg <userID> <msg>
				logger.log(Level.CONFIG, "msg");
				if(parsedInput.length >= 3) {
					
				}else {
					incompleteCommand();
				}
				break;
			case "collect":
				if(parsedInput.length == 1) {
					
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
