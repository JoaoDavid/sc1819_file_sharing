package server;

import communication.Message;
import communication.OpCode;

public class Skel {

	
	public static void invoke(Message msg) {
		/*STORE_FILES, LIST_FILES, REMOVE_FILES, USERS, TRUST_USERS, 
	UNTRUST_USERS, DOWNLOAD_FILE, SEND_MSG, SHOW_MSG, 
	END_CONNECTION,
	OP_ERROR, OP_SUCCESSFUL*/
		OpCode code = msg.getOpCode();
		switch (code) {
        case STORE_FILES: //store <files>
        	System.out.println("STORE_FILES");

            break;
        case LIST_FILES:
        	System.out.println("LIST_FILES");

            break;
        case REMOVE_FILES: //remove <files>
        	System.out.println("REMOVE_FILES");

            break;
        case USERS:
        	System.out.println("USERS");

            break;
        case TRUST_USERS: //trusted <trustedUserIDs>
        	System.out.println("TRUST_USERS");
            break;
        case UNTRUST_USERS: //untrusted <untrustedUserIDs>
        	System.out.println("UNTRUST_USERS");
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
