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
			arrOpRes = new OpCode[usersTrust.length];
			for(int i = 0; i < usersTrust.length; i++) {
				/*boolean deleted = svM.trusted(msg.getStrParam(), usersTrust[i]);
        		if(deleted) {
        			arrOpRes[i] = OpCode.OP_SUCCESSFUL;
        		}else {
        			arrOpRes[i] = OpCode.OP_ERROR;
        		}*/
				arrOpRes[i] = svM.trusted(connectedUser, usersTrust[i]);
			}
			response = new Message(OpCode.OP_RES_ARRAY,arrOpRes);
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
