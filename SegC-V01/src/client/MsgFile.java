package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;

/**
 * MSG FILE
 *
 */
public class MsgFile {
	/* Logger*/
	
	private static final String CLASS_NAME = MsgFile.class.getName();

	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	//args <serverAddress> <localUserID> [password]
	//127.0.0.1:23456 fernando pass123
	/**
	 * main
	 * @param args
	 */
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
	 * Parser of commands line
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
				logger.log(Level.CONFIG, "store");
				File file;
				ArrayList<String> fileName = new ArrayList<>();
				ArrayList<Byte[]> byteFiles = new ArrayList<>();

				for(int i = 1; i < parsedInput.length ; i++){
					file = new File(parsedInput[i]);
					if(file.exists()){

						try {
							byteFiles.add(toObjects(Files.readAllBytes(file.toPath())));
							fileName.add(file.getName());
						} catch (IOException e) {
							logger.log(Level.SEVERE, "Não foi possivel converter para bytes", e);
						}
					}
				}
				if(fileName.isEmpty() && parsedInput.length > 1){
					logger.log(Level.SEVERE, "Não foi possivel enviar ficheiros: " + parsedInput.toString());
				}else if(parsedInput.length == 1){
					incompleteCommand();
				}else{
					msgSent = new Message(OpCode.STORE_FILES);
					msgSent.setParam(fileName);
					msgSent.setParamBytes(byteFiles);
					msgResponse = client.sendMsg(msgSent);
					if(msgResponse == null || msgResponse.getOpCode() == OpCode.OP_ERROR){
						logger.log(Level.SEVERE, "Erro ao receber a resposta do servidor");
					}else{
						if(msgResponse.getOpCode() == OpCode.ERR_ALREADY_EXISTS){
							logger.log(Level.SEVERE, "Os seguintes ficheiros já existiam no servidor: " 
									+ msgResponse.getInbox().toString());
						}
						logger.log(Level.INFO, "Os seguintes ficheiros foram carregados no servidor: " 
								+ msgResponse.getParam().toString());
					}
				}
				break;
			case "list":
				if(parsedInput.length == 1) {
					logger.log(Level.CONFIG, "list");
					msgSent = new Message(OpCode.LIST_FILES);
					//send message method
					msgResponse = client.sendMsg(msgSent);
					if (msgResponse != null) {
						for(String curr : msgResponse.getArrStrParam()) {
							logger.log(Level.INFO, curr);
						}
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
					if (msgResponse != null){
						OpCode[] arrCodes = msgResponse.getOpCodeArr();
						for(int i = 0; i < arrSent.length; i++) {
							logger.log(Level.INFO, arrSent[i] + ":" + arrCodes[i].toString());
						}
					} else {
						logger.log(Level.SEVERE, "ERROR to recieve response");
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
					if (msgResponse != null) {
						for(String curr : msgResponse.getArrStrParam()) {
							logger.log(Level.INFO, curr);
						}
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
					if (msgResponse != null){
						OpCode[] arrCodes = msgResponse.getOpCodeArr();
						for(int i = 0; i < arrSent.length; i++) {
							logger.log(Level.INFO, arrSent[i] + ":" + arrCodes[i].toString());
						}
					} else{
						incompleteCommand();
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
					if(msgResponse == null){
						incompleteCommand();
					}else{
						if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL){
							logger.log(Level.INFO, "Friends removed: " + msgResponse.getStr());
						}else{
							logger.log(Level.SEVERE, msgResponse.getStr());
						}
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
					if(msgResponse == null){
						incompleteCommand();
					}{
						if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL){
							try {
								File fileTemp = new File(msgResponse.getStr());
								FileOutputStream fos = new FileOutputStream(fileTemp);
								fos.write(toPrimitives(msgResponse.getParamBytes().get(0)));
								fos.close();
								logger.log(Level.INFO, "Download file done: " + msgResponse.getStr());
							} catch (Exception e) {
								logger.log(Level.SEVERE, "Error to create the file " + msgResponse.getStr() 
								+ "in machine of client");
							}
						}else{
							logger.log(Level.SEVERE, msgResponse.getStr());
						}
					}
				}else {
					incompleteCommand();
				}
				break;
			case "msg": //msg <userID> <msg>
				logger.log(Level.CONFIG, "msg");
				if(parsedInput.length >= 3) {
					String userReceiver = parsedInput[1];
					String msg = rawInput.substring(rawInput.indexOf(userReceiver) + userReceiver.length() + 1);
					//send message
					logger.log(Level.CONFIG, "destino:" + userReceiver + " msg:" + msg);
					String[] arrSent = new String[2];
					arrSent[0] = userReceiver;//receiver
					arrSent[1] = msg;//text message
					msgSent = new Message(OpCode.SEND_MSG, arrSent);
					msgResponse = client.sendMsg(msgSent);
					if(msgResponse == null){
						incompleteCommand();
					}else{
						if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL){
							logger.log(Level.INFO, msgResponse.getStr());
						}else{
							logger.log(Level.SEVERE, msgResponse.getStr());
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
					if(msgResponse != null){
						if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL 
								&& msgResponse.getInbox() != null && !msgResponse.getInbox().isEmpty()){
							for(String msgToClient : msgResponse.getInbox()){
								logger.log(Level.INFO, msgToClient);
							}
							//done your job! -> mostrou as mensagens
						}else{
							logger.log(Level.INFO, "Empty mail box.");
						}
					}else{
						incompleteCommand();
					}
				}else {
					incompleteCommand();
				}
				break;
			case "help":
				logger.log(Level.CONFIG, "help");
				logger.log(Level.INFO, "store <files>");
				logger.log(Level.INFO, "list");
				logger.log(Level.INFO, "users");
				logger.log(Level.INFO, "trusted <trustedUserIDs>");
				logger.log(Level.INFO, "untrusted <untrustedUserIDs>");
				logger.log(Level.INFO, "download <userID> <file>");
				logger.log(Level.INFO, "msg <userID> <msg>");
				logger.log(Level.INFO, "collect");
				logger.log(Level.INFO, "exit");
				break;
			case "exit":
				if(parsedInput.length == 1) {
					logger.log(Level.CONFIG, "exit");
					msgSent = new Message(OpCode.END_CONNECTION);
					msgResponse = client.sendMsg(msgSent);
					if(msgResponse.getOpCode() == OpCode.OP_SUCCESSFUL) {
						client.disconnect();
						onLoop = false;
					}else{
						incompleteCommand();
					}
				}else {
					incompleteCommand();
				}
				break;
			default:
				logger.log(Level.INFO, "Command is not recognized");
				break;
			}
		}
		sc.close();
	}
	/**
	 * Message with error/incomplete request
	 */
	public static void incompleteCommand() {
		logger.log(Level.SEVERE,"Error: unrecognized or incomplete command line");
	}
	/**
	 * Convert to Object
	 * @param bytesPrim
	 * @return byte[] to Byte[]
	 */
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
