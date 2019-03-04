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
			logger.log(Level.CONFIG, "STORE_FILES");
			ArrayList<String> succ = new ArrayList<>();
			ArrayList<String> failed = new ArrayList<>();
			//atualiza lista dos files que foram carregados e não carregados
			svM.storeFiles(succ,failed,msg,connectedUser);
			//criação da reposta
			response = new Message();
			if(succ.size() == 0 && failed.size() > 0){
				response.setOpCode(OpCode.OP_ERROR);
			} else if(succ.size() != 0 && failed.size() == 0){
				response.setOpCode(OpCode.OP_SUCCESSFUL);
				response.setParam(succ);
			} else{
				response.setOpCode(OpCode.ERR_ALREADY_EXISTS);
				response.setParam(succ);
				response.setInbox(failed);
			}
			break;
		case LIST_FILES:
			logger.log(Level.CONFIG, "LIST_FILES");
			arrStrRes = svM.listFiles(connectedUser);
			if(arrStrRes != null) {
				response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}  	
			break;
		case REMOVE_FILES: //remove <files>
			logger.log(Level.CONFIG, "REMOVE_FILES");
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
			logger.log(Level.CONFIG, "USERS");
			arrStrRes = svM.listUsers();
			if(arrStrRes != null) {
				response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case TRUST_USERS: //trusted <trustedUserIDs>
			logger.log(Level.CONFIG, "TRUST_USERS");
			String[] usersTrust = msg.getArrStrParam();
			arrOpRes = svM.trusted(connectedUser, usersTrust);
			if(arrOpRes != null) {
				response = new Message(OpCode.OP_RES_ARRAY, arrOpRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case UNTRUST_USERS: //untrusted <untrustedUserIDs>
			logger.log(Level.CONFIG, "UNTRUST_USERS");
			String[] usersUntrust = msg.getArrStrParam();
			arrOpRes = svM.untrusted(connectedUser, usersUntrust);
			if(arrOpRes != null) {
				response = new Message(OpCode.OP_RES_ARRAY, arrOpRes);
			}else {
				response = new Message(OpCode.OP_ERROR);
			}
			break;
		case DOWNLOAD_FILE: //download <userID> <file>
			logger.log(Level.CONFIG, "DOWNLOAD_FILE");
			String[] users = msg.getArrStrParam();
			response = new Message();
			if(connectedUser.equals(users[0])){
				logger.log(Level.SEVERE, "Same user.");
				//erro -> é o user local
				response.setOpCode(OpCode.ERR_YOURSELF);
				response.setStr("Erro: Utilizador é o mesmo do pedido.");
			}else if(svM.friends(connectedUser, users[0])){
				//sao amigos
				logger.log(Level.SEVERE, "they are friends");
				Byte[] byteArray = svM.sendFileToClient(users[0],users[1]);
				if(byteArray == null){
					logger.log(Level.SEVERE, "Error to send the File by Server");
					response.setOpCode(OpCode.OP_ERROR);
					response.setStr("Error to send the File by Server");
				}else{
					response.setOpCode(OpCode.OP_SUCCESSFUL);
					ArrayList<Byte[]> bytes = new ArrayList<>();
					bytes.add(byteArray);
					response.setParamBytes(bytes);
					response.setStr(users[1]);
				}
			}else{
				//não sao amigos
				logger.log(Level.SEVERE, "Error, they are not friends");
				response.setOpCode(OpCode.ERR_NOT_FRIENDS);
				response.setStr("Não é amigo do utilizador: " + users[0]);
			}
			break;
		case SEND_MSG: //msg <userID> <msg>
			logger.log(Level.CONFIG, "SEND_MSG");
			String[] receiverText = msg.getArrStrParam();
			if(receiverText == null || receiverText.length == 0){
				response = new Message(OpCode.OP_ERROR);
			}else if(!svM.isRegistered(receiverText[0])) {
				response = new Message(OpCode.ERR_NOT_REGISTERED);
			}else if(!svM.friends(connectedUser,receiverText[0])) {
				response = new Message(OpCode.ERR_NOT_FRIENDS);
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
			logger.log(Level.CONFIG, "SHOW_MSG");
			ArrayList<String> inbox = svM.collectMsg(connectedUser);
			response = new Message();
			if(inbox == null){
				response.setOpCode(OpCode.OP_ERROR);
			}else{
				response.setOpCode(OpCode.OP_SUCCESSFUL);
				response.setInbox(inbox);
			}
			break;
		default:
			logger.log(Level.SEVERE, "Enum not recognized");
			break;
		}
		return response;

	}

}
