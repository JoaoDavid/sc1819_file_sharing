package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
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
import security.ContentCipher;
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
				server.checkFilesIntegrity();
				System.out.println("Initializing server on port: " + args[0]);
				server.startServer(port, server.privKey, server.pubKey);
			}
			catch (NumberFormatException e){
				System.out.println( "Server failed: Invalid port");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}			

		}else {
			System.out.println("The valid args are:");
			System.out.println("<port> <keystoreLocation> <keystorePassword> <secKeyAlias> <secKeyPassword> <privPubAlias> <privPubPassword>");
		}
	}

	public void startServer (int port, PrivateKey privKey, PublicKey pubKey) throws Exception{
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
		while(serverOnline) {
			try {
				Socket inSoc = ss.accept();
				
				if(!macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
					throw new Exception("FILE WITH USER LOGIN INFO WAS COMPROMISED - ABORTING");
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
		if(!macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			throw new ApplicationException("FILE WITH USER LOGIN INFO WAS COMPROMISED - ABORTING");
		}
		List<String> listUser = UserValidation.listRegisteredUsers();
		List<String> listFiles = new ArrayList<String>();
		listFiles.add(FilePaths.FILE_NAME_TRUSTED);
		listFiles.add(FilePaths.FILE_NAME_MSG);
		for(String currUser : listUser) {
			for(String currFile : listFiles) {
				String path = FilePaths.FOLDER_SERVER_USERS + File.separator + currUser + File.separator + currFile;
				File file = new File(path);
				File fileSig = new File(path + FilePaths.FILE_NAME_SIG_SUFIX);
				File fileKey = new File(path + FilePaths.FILE_NAME_KEY_SUFIX);
				ContentCipher.checkFileIntegrity(file, fileSig, fileKey, this.privKey, this.pubKey);
			}
			/*File fileDirec = new File();
			String[] allFiles = fileDirec.list();
			for(String currFile : allFiles) {
			}*/
		}
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
		}

		public void run() {
			String user = null;
			String passwd = null;
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());



				//cifrar
				List<String> logInfo = Network.bufferToList(socket) ;
				if(logInfo != null) {
					user = logInfo.get(0);
					passwd = logInfo.get(1);
				}else {
					throw new ApplicationException("Error receiving log information");
				}
				/*user = (String)inStream.readObject();
				passwd = (String)inStream.readObject();*/

				if (userManagerHandler.validLogin(user, passwd)){
					System.out.println("Client connected: " + user + " logged in");
					outStream.writeObject(OpCode.OP_SUCCESSFUL);
					Skeleton skel = new Skeleton(user, socket, fileService, msgService, userService, privKey, pubKey, macM);
					boolean connected = true;
					while(connected) {
						connected = skel.communicate(outStream, inStream);
					}
					System.out.println("Client disconnected: " + user + " logged out");
				}
				else {
					System.out.println("Client failed: " + user + " failed to login");
					outStream.writeObject(OpResult.ERROR);
				}

				outStream.close();
				inStream.close();

				socket.close();

			} catch (IOException e) {
				System.out.println("Client disconnected: Connection lost with " + user);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}