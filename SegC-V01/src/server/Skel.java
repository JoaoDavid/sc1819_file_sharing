package server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;

public class Skel {

	private static final String CLASS_NAME = Skel.class.getName();
	private static Manager svM = Manager.getInstance();

	private final static Logger logger = Logger.getLogger(CLASS_NAME);
	//fazer padrao singleton para o manager
	//msg != null
	public static Message invoke(Message msg, String connectedUser) { //alterar no fim para retornar uma message que depois
		//e enviada no ciclo do msgFileServer
		Message response = null;
		String[] arrStrRes;
		OpCode[] arrOpRes;
		OpCode code = msg.getOpCode();
		switch (code) {
		case STORE_FILES: //store <files>
			logger.log(Level.CONFIG, "STORE_FILES user: " + connectedUser);
			arrOpRes = svM.storeFiles(msg.getArrListStr(), msg.getArrListArrBytes(), connectedUser);
			if(arrOpRes != null) {
				response = new Message(OpCode.OP_RES_ARRAY, arrOpRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case LIST_FILES:
			logger.log(Level.CONFIG, "LIST_FILES user: " + connectedUser);
			arrStrRes = svM.listFiles(connectedUser);
			if(arrStrRes != null) {
				response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}  	
			break;
		case REMOVE_FILES: //remove <files>
			logger.log(Level.CONFIG, "REMOVE_FILES user: " + connectedUser);
			String[] nameFile = msg.getArrStrParam();
			arrOpRes = new OpCode[nameFile.length];
			for(int i = 0; i < nameFile.length; i++) {
				boolean deleted = svM.deleteFile(connectedUser,nameFile[i]);
				if(deleted) {
					arrOpRes[i] = OpCode.OP_SUCCESSFUL;
				}else {
					arrOpRes[i] = OpCode.ERR_NOT_FOUND;
				}
			}
			response = new Message(OpCode.OP_RES_ARRAY,arrOpRes);
			break;
		case USERS:
			logger.log(Level.CONFIG, "USERS user: " + connectedUser);
			arrStrRes = svM.listUsers();
			if(arrStrRes != null) {
				response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case TRUST_USERS: //trusted <trustedUserIDs>
			logger.log(Level.CONFIG, "TRUST_USERS user: " + connectedUser);
			String[] usersTrust = msg.getArrStrParam();
			arrOpRes = svM.trusted(connectedUser, usersTrust);
			if(arrOpRes != null) {
				response = new Message(OpCode.OP_RES_ARRAY, arrOpRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case UNTRUST_USERS: //untrusted <untrustedUserIDs>
			logger.log(Level.CONFIG, "UNTRUST_USERS user: " + connectedUser);
			String[] usersUntrust = msg.getArrStrParam();
			arrOpRes = svM.untrusted(connectedUser, usersUntrust);
			if(arrOpRes != null) {
				response = new Message(OpCode.OP_RES_ARRAY, arrOpRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case DOWNLOAD_FILE: //download <userID> <file>
			logger.log(Level.CONFIG, "DOWNLOAD_FILE user: " + connectedUser);
			//users name account that has the file and name of the file
			String[] ownerFile = msg.getArrStrParam();
			if(connectedUser.equals(ownerFile[0])){
				logger.log(Level.SEVERE, "Same user.");
				//erro -> é o user local
				response = new Message(OpCode.ERR_YOURSELF);
				//response.setStr("Erro: Utilizador é o mesmo do pedido.");
			}else if(!svM.isRegistered(ownerFile[0])) {
				logger.log(Level.INFO, "user is not registered");
				response = new Message(OpCode.ERR_NOT_REGISTERED);
			}else if(svM.friends(connectedUser, ownerFile[0])){
				//sao amigos
				logger.log(Level.INFO, "they are friends");
				Byte[] byteArray = svM.sendFileToClient(ownerFile[0],ownerFile[1]);
				if(byteArray == null){
					logger.log(Level.SEVERE, "File not found");
					response = new Message(OpCode.ERR_NOT_FOUND);
				}else{
					response = new Message(OpCode.OP_SUCCESSFUL, byteArray);
				}
			}else{
				//não sao amigos
				logger.log(Level.SEVERE, "Error, they are not friends");
				response = new Message(OpCode.ERR_NOT_TRUSTED);
			}
			break;
		case SEND_MSG: //msg <userID> <msg>
			logger.log(Level.CONFIG, "SEND_MSG user: " + connectedUser);
			String[] receiverText = msg.getArrStrParam();
			if(receiverText == null || receiverText.length == 0){
				response = new Message(OpCode.OP_ERROR);
			}else if(!svM.isRegistered(receiverText[0])) {
				response = new Message(OpCode.ERR_NOT_REGISTERED);
			}else if(!svM.friends(connectedUser,receiverText[0])) {
				response = new Message(OpCode.ERR_NOT_TRUSTED);
			}else{
				boolean saved = svM.storeMsg(connectedUser, receiverText[0], receiverText[1]);
				if(saved) {
					response = new Message(OpCode.OP_SUCCESSFUL);
				}else {
					response = new Message(OpCode.OP_ERROR);
				}
			}
			break;
		case COLLECT_MSG:
			logger.log(Level.CONFIG, "SHOW_MSG user: " + connectedUser);
			ArrayList<String> inbox = svM.collectMsg(connectedUser);
			if(inbox == null){
				response = new Message(OpCode.OP_ERROR);
			}else{
				response = new Message(OpCode.OP_SUCCESSFUL, inbox);
			}
			break;
		default:
			logger.log(Level.SEVERE, "Enum not recognized");
			break;
		}
		return response;

	}

}
