package server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;

public class Skel {

	private static final String CLASS_NAME = Skel.class.getName();

	private final static Logger logger = Logger.getLogger(CLASS_NAME);
	//fazer padrao singleton para o manager
	//msg != null
	public static Message invoke(Message msg, Manager svM, String connectedUser) { //alterar no fim para retornar uma message que depois
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
			File file;
			for(int i = 0; i < msg.getParam().size();i++){
				file = new File(ServerConst.FOLDER_SERVER 
						+ File.pathSeparator + connectedUser 
						+ File.pathSeparator + msg.getParam().get(i));
				if(file.exists()){
					failed.add(file.getName());
				}else{
					try{
						FileOutputStream fos = new FileOutputStream(file);
						fos.write(toPrimitives(msg.getParamBytes().get(i)));
						fos.close();
						succ.add(file.getName());
					}catch(Exception e){
						logger.log(Level.SEVERE, "FAILED to store the file: " + file.getName());
						failed.add(file.getName());
					}
				}
			}
			response = new Message();
			if(succ.size() == 0 && failed.size() > 0){
				response.setOpCode(OpCode.OP_ERROR);
			} else if(succ.size() != 0 && failed.size() == 0){
				response.setOpCode(OpCode.OP_SUCCESSFUL);
				response.setParam(succ);
			} else{
				response.setOpCode(OpCode.OP_SUCC_ERROR);
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
				response = new Message(OpCode.OP_ERROR,"Error finding list of files");
			}  	
			break;
		case REMOVE_FILES: //remove <files>
			logger.log(Level.CONFIG, "REMOVE_FILES");
			String[] nameFile = msg.getArrStrParam();
			arrOpRes = new OpCode[nameFile.length];
			for(int i = 0; i < nameFile.length; i++) {
				boolean deleted = svM.deleteFile(nameFile[i]);
				if(deleted) {
					arrOpRes[i] = OpCode.OP_SUCCESSFUL;
				}else {
					arrOpRes[i] = OpCode.OP_ERROR;
				}
			}
			response = new Message(arrOpRes);
			break;
		case USERS:
			logger.log(Level.CONFIG, "USERS");
			arrStrRes = svM.listUsers();
			if(arrStrRes != null) {
				logger.log(Level.CONFIG, "OK:USERS");
				response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
			}else {
				response = new Message(OpCode.OP_ERROR,"Error finding list users");
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
			response = new Message(arrOpRes);
			break;
		case UNTRUST_USERS: //untrusted <untrustedUserIDs>
			logger.log(Level.CONFIG, "UNTRUST_USERS");
			String[] usersUntrust = msg.getArrStrParam();
			arrOpRes = new OpCode[usersUntrust.length];
			for(int i = 0; i < usersUntrust.length; i++) {
				/*boolean deleted = svM.trusted(msg.getStrParam(), usersUntrust[i]);
        		if(deleted) {
        			arrOpRes[i] = OpCode.OP_SUCCESSFUL;
        		}else {
        			arrOpRes[i] = OpCode.OP_ERROR;
        		}*/
				arrOpRes[i] = svM.untrusted(connectedUser, usersUntrust[i]);
			}
			response = new Message(arrOpRes);
			break;
		case DOWNLOAD_FILE: //download <userID> <file>
			logger.log(Level.CONFIG, "DOWNLOAD_FILE");
			break;
		case SEND_MSG: //msg <userID> <msg>
			logger.log(Level.CONFIG, "SEND_MSG");
			String[] receiverText = msg.getArrStrParam();
			boolean saved = svM.storeMsg(connectedUser, receiverText[0], receiverText[1]);
			if(saved) {
				response = new Message(OpCode.OP_SUCCESSFUL);
				return response;
			}else {
				response = new Message(OpCode.OP_ERROR);
				return response;
			}
		case COLLECT_MSG:
			logger.log(Level.CONFIG, "SHOW_MSG");

			break;
		default:
			logger.log(Level.SEVERE, "Enum not recognized");
			break;
		}
		return response;

	}
	private static byte[] toPrimitives(Byte[] oBytes) {

		byte[] bytes = new byte[oBytes.length];
		for(int i = 0; i < oBytes.length; i++){
			bytes[i] = oBytes[i];
		}
		return bytes;

	}
}
