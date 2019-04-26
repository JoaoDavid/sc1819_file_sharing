package security;

import java.io.File;
import java.io.FileNotFoundException;
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
			e.printStackTrace();
		}

	}

	public void updateMacFile(String filePath, String filePathMAC) {
		try (FileOutputStream usersFileMAC = new FileOutputStream(filePathMAC);){
			byte[] newMACBytes = mac.doFinal(Files.readAllBytes(Paths.get(filePath)));
			usersFileMAC.write(newMACBytes);
			usersFileMAC.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public boolean validRegistFile(String filePath, String filePathMAC) throws ApplicationException  {
		try {
			Mac otherMAC = Mac.getInstance(this.macAlgorithm);
			otherMAC.init(secKey);
			File userRegistFile = new File(filePath);
			File userRegistFileMAC = new File(filePathMAC);
			byte[] otherMACfinal = otherMAC.doFinal(Files.readAllBytes(userRegistFile.toPath())); 
			byte[] savedMAC = Files.readAllBytes(userRegistFileMAC.toPath());
			return Arrays.equals(otherMACfinal, savedMAC);
		} catch (Exception e) {
			throw new ApplicationException("FILE WITH USER LOGIN INFO WAS COMPROMISED - ABORTING");
		}
	}

}
