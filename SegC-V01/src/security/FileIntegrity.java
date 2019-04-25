package security;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import facade.exceptions.ApplicationException;
import server.business.util.FilePaths;

public class FileIntegrity {

	private PrivateKey privKey;
	private PublicKey pubKey;
	private String userName;

	public FileIntegrity(MacManager macM, PrivateKey privKey, PublicKey pubKey, String userName) {
		this.privKey = privKey;
		this.pubKey = pubKey;
		this.userName = userName;
	}


	public boolean checkControlFiles() throws ApplicationException  {
		List<String> controlFiles = new ArrayList<String>();
		controlFiles.add(FilePaths.FILE_NAME_TRUSTED);
		controlFiles.add(FilePaths.FILE_NAME_MSG);
		for(String currFile : controlFiles) {
			String path = FilePaths.FOLDER_SERVER_USERS + File.separator + this.userName + File.separator + currFile;
			File file = new File(path);
			File fileSig = new File(path + FilePaths.FILE_NAME_SIG_SUFIX);
			File fileKey = new File(path + FilePaths.FILE_NAME_KEY_SUFIX);
			ContentCipher.checkFileIntegrity(file, fileSig, fileKey, this.privKey, this.pubKey);
		}
		return true;
	}


}
