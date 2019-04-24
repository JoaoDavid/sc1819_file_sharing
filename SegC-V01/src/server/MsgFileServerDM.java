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

import javax.crypto.SecretKey;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import communication.OpCode;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.services.UserService;
import facade.startup.MsgFileServerApp;
import server.business.util.ConstKeyStore;
import users.UserManagerHandler;

public class MsgFileServerDM{

	private MsgFileServerApp app;
	private FileService fileService;
	private MessageService msgService;
	private UserService userService;
	private UserManagerHandler userManagerHandler;

	private SecretKey secKey;
	private PrivateKey privKey;
	private PublicKey pubKey;

	public MsgFileServerDM(SecretKey secKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
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
	}


	public static void main(String[] args) {
		if(args.length == 7) {
			int port;
			MsgFileServerDM server;
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
				server = new MsgFileServerDM(secKey, privKey, pubKey);
			}
			catch (NumberFormatException e){
				System.out.println( "Server failed: Invalid port");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}			
			System.out.println("Initializing server on port: " + args[0]);
			server.startServer(port, server.privKey, server.pubKey);
		}else {
			System.out.println("The valid args are:");
			System.out.println("<port> <keystoreLocation> <keystorePassword> <secKeyAlias> <secKeyPassword> <privPubAlias> <privPubPassword>");
		}
	}

	public void startServer (int port, PrivateKey privKey, PublicKey pubKey){
		ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
		SSLServerSocket ss = null;

		//ServerSocket sSoc = null;

		try { 
			ss = (SSLServerSocket) ssf.createServerSocket(port);
			ss.setNeedClientAuth(false);//COLOCAR A TRUE
			//sSoc = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		while(true) {
			try {
				//Socket inSoc = sSoc.accept();
				Socket inSoc = ss.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
				//ss.close();
				//sSoc.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		//sSoc.close();
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
		}

		public void run(){
			String user = null;
			String passwd = null;
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());



				try {
					//cifrar
					user = (String)inStream.readObject();
					passwd = (String)inStream.readObject();
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				if (userManagerHandler.validLogin(user, passwd)){
					System.out.println("Client connected: " + user + " logged in");
					outStream.writeObject(OpCode.OP_SUCCESSFUL);
					Skeleton skel = new Skeleton(user, socket, fileService, msgService, userService, privKey, pubKey);
					boolean connected = true;
					while(connected) {
						connected = skel.communicate(outStream, inStream);
					}
					System.out.println("Client disconnected: " + user + " logged out");
				}
				else {
					System.out.println("Client failed: " + user + " failed to login");
					outStream.writeObject(OpCode.OP_ERROR);
				}

				outStream.close();
				inStream.close();

				socket.close();

			} catch (IOException e) {
				System.out.println("Client disconnected: Connection lost with " + user);
			}
		}
	}
}