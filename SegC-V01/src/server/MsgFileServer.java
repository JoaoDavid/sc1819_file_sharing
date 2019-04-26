package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.SecretKey;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import communication.Network;
import communication.OpCode;
import communication.OpResult;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.services.UserService;
import facade.startup.MsgFileServerApp;
import security.FileIntegrity;
import security.MacManager;
import server.business.util.ConstKeyStore;
import server.business.util.FilePaths;
import server.business.util.UserValidation;
import users.UserManagerHandler;

public class MsgFileServer{

	private MsgFileServerApp app;
	private FileService fileService;
	private MessageService msgService;
	private UserService userService;
	private UserManagerHandler userManagerHandler;

	private SecretKey secKey;
	private PrivateKey privKey;
	private PublicKey pubKey;
	private MacManager macM;

	public MsgFileServer(SecretKey secKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		this.app = new MsgFileServerApp();
		this.fileService = new FileService(app.getDownloadFileHandler(), 
				app.getListFilesHandler(), app.getRemoveFilesHandler(), 
				app.getStoreFileHandler());
		this.msgService = new MessageService(app.getCollectMessagesHandler(), 
				app.getSendMessageHandler());
		this.userService = new UserService(app.getTrustUsersHandler(), app.getUntrustUsersHandler(), app.getListUsersHandler());
		this.userManagerHandler = new UserManagerHandler(secKey, privKey, pubKey);
		this.secKey = secKey;
		this.privKey = privKey;
		this.pubKey = pubKey;		
		this.macM = new MacManager(ConstKeyStore.MAC_ALGORITHM, secKey);
	}

	//args:   23456 .\keystore\myServer.keyStore batata secKey batata keyRSA batata
	public static void main(String[] args) {
		if(args.length == 7) {
			int port;
			MsgFileServer server;
			try {
				/*System.setProperty("javax.net.ssl.keyStore", "keystore" + File.separator + "myServer.keyStore");
				System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
				System.setProperty("javax.net.ssl.keyStorePassword", "batata");*/
				System.setProperty("javax.net.ssl.keyStore", args[1]);
				System.setProperty("javax.net.ssl.keyStoreType", ConstKeyStore.KEYSTORE_TYPE);
				System.setProperty("javax.net.ssl.keyStorePassword", args[2]);
				port = Integer.parseInt(args[0]);

				FileInputStream kfile = new FileInputStream(args[1]);
				KeyStore kstore = KeyStore.getInstance("JCEKS");
				kstore.load(kfile, args[2].toCharArray());
				SecretKey secKey = (SecretKey) kstore.getKey(args[3], args[4].toCharArray());
				PrivateKey privKey = (PrivateKey) kstore.getKey(args[5], args[6].toCharArray());
				PublicKey pubKey = kstore.getCertificate(args[5]).getPublicKey();

				server = new MsgFileServer(secKey, privKey, pubKey);
				server.startServer(port, server.privKey, server.pubKey);
			}
			catch (NumberFormatException e){
				System.out.println( "Server failed: Invalid port");
			} catch (Exception e) {
				e.printStackTrace();
			}			

		}else {
			System.out.println("The valid args are:");
			System.out.println("<port> <keystoreLocation> <keystorePassword> <secKeyAlias> <secKeyPassword> <privPubAlias> <privPubPassword>");
			System.out.println("example: 23456 .\\keystore\\myServer.keyStore batata secKey batata keyRSA batata");
		}
	}

	public void startServer (int port, PrivateKey privKey, PublicKey pubKey) throws ApplicationException{
		ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
		SSLServerSocket ss = null;
		
		
		try { 
			ss = (SSLServerSocket) ssf.createServerSocket(port);
			ss.setNeedClientAuth(false);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		checkFilesIntegrity();
		boolean serverOnline = true;
		System.out.println("Initializing server on port: " + port);
		while(serverOnline) {
			try {
				Socket inSoc = ss.accept();
				
				if(!macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
					throw new ApplicationException("FILE WITH USER LOGIN INFO WAS COMPROMISED - ABORTING");
				}
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void checkFilesIntegrity() throws ApplicationException {
		System.out.println("Checking files integrity ...");
		macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
		List<String> listUser = UserValidation.listRegisteredUsers();
		
		for(String currUser : listUser) {
			FileIntegrity currFileIntegrity = new FileIntegrity(macM, privKey, pubKey, currUser);
			currFileIntegrity.checkControlFiles();
			if(!currFileIntegrity.checkUserFiles()) {
				throw new ApplicationException(currUser + "'s FILES WERE COMPROMISED - ABORTING");
			}
		}
		System.out.println("Files OK");
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
		}

		public void run() {
			String userName = null;
			String passwd = null;
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());



				//cifrar
				List<String> logInfo = Network.bufferToList(socket) ;
				if(logInfo != null) {
					userName = logInfo.get(0);
					passwd = logInfo.get(1);
				}else {
					throw new ApplicationException("Error receiving log information");
				}
				/*user = (String)inStream.readObject();
				passwd = (String)inStream.readObject();*/

				if (userManagerHandler.validLogin(userName, passwd)){
					System.out.println("Client connected: " + userName + " logged in");
					outStream.writeObject(OpCode.OP_SUCCESSFUL);
					Skeleton skel = new Skeleton(userName, socket, fileService, msgService, userService, privKey, pubKey, macM);
					boolean connected = true;
					FileIntegrity fileIntegrity = new FileIntegrity(macM, privKey, pubKey, userName);
					while(connected) {
						connected = skel.communicate(outStream, inStream, fileIntegrity);
					}
					System.out.println("Client disconnected: " + userName + " logged out");
				}
				else {
					System.out.println("Client failed: " + userName + " failed to login");
					outStream.writeObject(OpResult.ERROR);
				}

				outStream.close();
				inStream.close();

				socket.close();

			} catch (IOException e) {
				System.out.println("Client disconnected: Connection lost with " + userName);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}