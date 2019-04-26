package users;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.SecretKey;

import facade.exceptions.ApplicationException;
import server.business.util.ConstKeyStore;
import server.business.util.FilePaths;



public class UserManager {

	private UserManagerHandler handler;


	public UserManager(SecretKey secKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		this.handler = new UserManagerHandler(secKey, privKey, pubKey);
	}

	//args example
	//      .\keystore\myServer.keyStore batata secKey batata keyRSA batata
	public static void main(String[] args) throws Exception {
		if(args.length == 6) {
			FileInputStream kfile = new FileInputStream(args[0]);
			KeyStore keyStore = KeyStore.getInstance(ConstKeyStore.KEYSTORE_TYPE);
			keyStore.load(kfile, args[1].toCharArray());
			SecretKey secKey = (SecretKey) keyStore.getKey(args[2], args[3].toCharArray());
			PrivateKey privKey = (PrivateKey) keyStore.getKey(args[4], args[5].toCharArray());
			PublicKey pubKey = keyStore.getCertificate(args[4]).getPublicKey();
			UserManager userManager = new UserManager(secKey, privKey, pubKey);
			if(userManager.boot()) {
				userManager.startParser();	
			}else {
				System.out.println("ERROR BOOTING UP : FILE CONTAINING USER LOGIN INFO WAS CORRUPTED");
				System.out.println("if you wish to do a fresh start, delete all files inside " + FilePaths.FOLDER_SERVER);
				System.out.println("and wait a few seconds before starting again");
			}					
		}else {
			System.out.println("valid args: <keystoreLocation> <keystorePassword> <secKeyAlias> <secKeyPassword> <privPubAlias> <privPubPassword>");
		}

	}

	public void startParser() throws Exception {
		Scanner sc = new Scanner(System.in);
		boolean onLoop = true;
		System.out.println("USER MANAGEMENT   use help for the list of possible commands");
		while(onLoop) {
			try {				
				System.out.print(">>>");
				String rawInput = sc.nextLine();
				String[] parsedInput = rawInput.split("(\\s)+");
				String userName;
				String password;
				//fazer try aqui
				switch (parsedInput[0]) {//command <user> <password>
				case "create":
					if(parsedInput.length == 3) {
						userName = parsedInput[1];
						password = parsedInput[2];						
						System.out.println(this.createUser(userName, password));
					}else {
						incompleteCommand();
					}
					break;

				case "remove":
					if(parsedInput.length == 2) {
						userName = parsedInput[1];						
						System.out.println(this.removeUser(userName));
					}else {
						incompleteCommand();
					}
					break;

				case "update":
					if(parsedInput.length == 3) {
						userName = parsedInput[1];
						password = parsedInput[2];
						System.out.println(this.updateUser(userName, password));
					}else {
						incompleteCommand();
					}
					break;
				case "exit":
					if(parsedInput.length == 1) {
						onLoop = false;
					}else {
						incompleteCommand();
					}
					break;
				case "help":
					if(parsedInput.length == 1) {
						System.out.println("create <user> <password>");
						System.out.println("remove <user>");
						System.out.println("update <user> <password>");
						System.out.println("login <user> <password>");
					}else {
						incompleteCommand();
					}
					break;
				case "login":
					if(parsedInput.length == 3) {
						userName = parsedInput[1];
						password = parsedInput[2];
						System.out.println(this.loginUser(userName, password));
					}else {
						incompleteCommand();
					}
					break;
				default:
					System.out.println("Command is not recognized");
					break;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
		sc.close();
	}
	
	private boolean boot() throws ApplicationException {
		return handler.boot();
	}


	private boolean loginUser(String userName, String password) {
		return this.handler.validLogin(userName, password);
	}

	private boolean createUser(String userName, String password) throws Exception {
		return this.handler.createUser(userName, password);

	}

	private boolean removeUser(String userName) throws Exception {
		return this.handler.removeUser(userName);

	}

	private boolean updateUser(String userName, String password) throws Exception {
		return this.handler.updateUser(userName, password);
	}
	
	public static void incompleteCommand() {
		System.out.println("ERROR: incomplete command line");
	}
}



