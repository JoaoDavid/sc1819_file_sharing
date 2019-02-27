package server;

import communication.Message;
import communication.OpCode;

public class Skel {
	//fazer padrao singleton para o manager
	//msg != null
	public static void invoke(Message msg, Manager svM) { //alterar no fim para retornar uma message que depois
								//e enviada no ciclo do msgFileServer

		Message response;
		String[] arrStrRes;
		OpCode[] arrOpRes;
		OpCode code = msg.getOpCode();
		switch (code) {
        case STORE_FILES: //store <files>
        	System.out.println("STORE_FILES");
        	
            break;
        case LIST_FILES:
        	System.out.println("LIST_FILES");
        	arrStrRes = svM.listFiles(msg.getStrParam());
        	if(arrStrRes != null) {
        		response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
        		//return response;
        	}else {
        		response = new Message(OpCode.OP_ERROR,"Error finding list of files");
        		//return response;
        	}  	
            break;
        case REMOVE_FILES: //remove <files>
        	System.out.println("REMOVE_FILES");
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
        	System.out.println("USERS");
        	arrStrRes = svM.listUsers();
        	if(arrStrRes != null) {
        		response = new Message(OpCode.OP_SUCCESSFUL, arrStrRes);
        		//return response;
        	}else {
        		response = new Message(OpCode.OP_ERROR,"Error finding list users");
        		//return response;
        	}
            break;
        case TRUST_USERS: //trusted <trustedUserIDs>
        	System.out.println("TRUST_USERS");
        	String[] usersTrust = msg.getArrStrParam();
        	arrOpRes = new OpCode[usersTrust.length];
        	for(int i = 0; i < usersTrust.length; i++) {
        		/*boolean deleted = svM.trusted(msg.getStrParam(), usersTrust[i]);
        		if(deleted) {
        			arrOpRes[i] = OpCode.OP_SUCCESSFUL;
        		}else {
        			arrOpRes[i] = OpCode.OP_ERROR;
        		}*/
        		arrOpRes[i] = svM.trusted(msg.getStrParam(), usersTrust[i]);
        	}
        	response = new Message(arrOpRes);
            break;
        case UNTRUST_USERS: //untrusted <untrustedUserIDs>
        	System.out.println("UNTRUST_USERS");
        	String[] usersUntrust = msg.getArrStrParam();
        	arrOpRes = new OpCode[usersUntrust.length];
        	for(int i = 0; i < usersUntrust.length; i++) {
        		/*boolean deleted = svM.trusted(msg.getStrParam(), usersUntrust[i]);
        		if(deleted) {
        			arrOpRes[i] = OpCode.OP_SUCCESSFUL;
        		}else {
        			arrOpRes[i] = OpCode.OP_ERROR;
        		}*/
        		arrOpRes[i] = svM.untrusted(msg.getStrParam(), usersUntrust[i]);
        	}
        	response = new Message(arrOpRes);
            break;
        case DOWNLOAD_FILE: //download <userID> <file>
        	System.out.println("DOWNLOAD_FILE");
            break;
        case SEND_MSG: //msg <userID> <msg>
        	System.out.println("SEND_MSG");

            break;
        case SHOW_MSG:
        	System.out.println("SHOW_MSG");

            break;
        default:
        	System.out.println("Enum not recognized");
        	break;
		}
		
	}
}
