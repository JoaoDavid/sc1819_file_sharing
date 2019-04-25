package security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import facade.exceptions.ApplicationException;


public class MacManager {
	
	private Mac mac;
	private SecretKey secKey;
	private String macAlgorithm;
	
	public MacManager(String macAlgorithm, SecretKey secKey) {
		this.macAlgorithm = macAlgorithm;
		this.secKey = secKey;
		try {			
			mac = Mac.getInstance(macAlgorithm);
			mac.init(secKey);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateMacFile(String filePath, String filePathMAC) throws Exception {
		FileOutputStream usersFileMAC = new FileOutputStream(filePathMAC);
		byte[] newMACBytes = mac.doFinal(Files.readAllBytes(Paths.get(filePath)));
		usersFileMAC.write(newMACBytes);
		usersFileMAC.close();
	}
	
	public boolean validRegistFile(String filePath, String filePathMAC) throws ApplicationException  {
		try {
			Mac otherMAC = Mac.getInstance(this.macAlgorithm);
			otherMAC.init(secKey);
			File userRegistFile = new File(filePath);
			File userRegistFileMAC = new File(filePathMAC);
			if(userRegistFileMAC.length() != 0) {
				byte[] otherMACfinal = otherMAC.doFinal(Files.readAllBytes(userRegistFile.toPath())); 
				byte[] savedMAC = Files.readAllBytes(Paths.get(filePathMAC));
				return Arrays.equals(otherMACfinal, savedMAC);
			}/*else {
				updateMacFile(filePath, filePathMAC);
				return true;
			}*/
		} catch (Exception e) {
			throw new ApplicationException("FILE WITH USER LOGIN INFO WAS COMPROMISED - ABORTING");
		} 
		return false;
	}

}
