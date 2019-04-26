package server.business.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import facade.exceptions.ApplicationException;
import security.ContentCipher;
import server.business.util.FileManager;
import server.business.util.FilePaths;
import server.business.util.UserValidation;

public class UntrustUsersHandler {

	private FileManager fileMan;

	public UntrustUsersHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean untrustUser(String userName, String userNameUntrusted, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FILE_NAME_TRUSTED;
		boolean untrusted = false;
		if(UserValidation.userNameRegisteredAndActive(userNameUntrusted) && !userName.equals(userNameUntrusted)) {
			File trustedFile = fileMan.acquireFile(filePath);
			try {
				synchronized(trustedFile){
					File trustedFileSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
					File trustedFileKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
					byte[] result = ContentCipher.decryptFileAndCheckSig(trustedFile, trustedFileSig, trustedFileKey, privKey, pubKey);
					String inFile = new String(result);
					List<String> list = Arrays.asList(inFile.split("\n"));
					ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
					for(String currLine : list) {
						if(currLine.equals(userNameUntrusted)) {
							untrusted = true;
						}else {
							byteArr.write((currLine + "\n").getBytes());
						}
					}
					if(untrusted) { //userNameUntrusted was deleted from the file
						ContentCipher.sigAndEcryptFile(trustedFile, trustedFileSig, trustedFileKey, privKey, pubKey, byteArr.toByteArray());
					}//else, does not exist in the file, so there is no need to rewrite the file
					return untrusted;
				}
			} catch (FileNotFoundException e) {
				throw new ApplicationException("File not found at: " + filePath);
			} catch (IOException e) {
				throw new ApplicationException("IOException in untrustUser");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fileMan.releaseFile(filePath);
			}
		}
		return untrusted;
	}
	
	
}



