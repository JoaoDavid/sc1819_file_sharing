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

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import communication.OpCode;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.services.UserService;
import facade.startup.MsgFileServerApp;
import users.UserManagerHandler;

//Servidor myServer

public class MsgFileServerDM{

	private MsgFileServerApp app;
	private FileService fileService;
	private MessageService msgService;
	private UserService userService;
	private UserManagerHandler userManagerHandler;
	
	private PrivateKey privKey;
	private PublicKey pubKey;

	public MsgFileServerDM(String keystoreLocation, String keystorePassword, String secKeyAlias, String keyPassword, PrivateKey privKey, PublicKey pubKey) throws Exception {
		this.app = new MsgFileServerApp();
		this.fileService = new FileService(app.getDownloadFileHandler(), 
				app.getListFilesHandler(), app.getRemoveFilesHandler(), 
				app.getStoreFileHandler());
		this.msgService = new MessageService(app.getCollectMessagesHandler(), 
				app.getSendMessageHandler());
		this.userService = new UserService(app.getTrustUsersHandler(), app.getUntrustUsersHandler(), app.getListUsersHandler());
		this.userManagerHandler = new UserManagerHandler(keystoreLocation, keystorePassword, secKeyAlias, keyPassword);
		this.privKey = privKey;
		this.pubKey = pubKey;
		
	}


	public static void main(String[] args) {
		if(args.length == 7) {
			int port;
			MsgFileServerDM server;
			try {
				System.setProperty("javax.net.ssl.keyStore", "keystore" + File.separator + "myServer.keyStore");
				System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
				System.setProperty("javax.net.ssl.keyStorePassword", "batata");
				port = Integer.parseInt(args[0]);
				
				FileInputStream kfile = new FileInputStream(args[1]);
				KeyStore kstore = KeyStore.getInstance("JCEKS");
				kstore.load(kfile, args[2].toCharArray());
				PrivateKey privKey = (PrivateKey) kstore.getKey(args[5], args[6].toCharArray());
				PublicKey pubKey = kstore.getCertificate(args[5]).getPublicKey();
				System.out.println(privKey == null);
				System.out.println(pubKey == null);
				System.out.println(pubKey.toString());
				server = new MsgFileServerDM(args[1], args[2], args[3], args[4], privKey, pubKey);
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
			System.out.println("thread do server para cada cliente");
		}

		public void run(){
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

				String user = null;
				String passwd = null;

				try {
					user = (String)inStream.readObject();
					passwd = (String)inStream.readObject();
					System.out.println(user);
					System.out.println(passwd);
					System.out.println("thread: depois de receber a password e o user");
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				if (userManagerHandler.validLogin(user, passwd)){
					outStream.writeObject(OpCode.OP_SUCCESSFUL);
					Skeleton skel = new Skeleton(user, socket, fileService, msgService, userService, privKey, pubKey);
					boolean connected = true;
					while(connected) {
						connected = skel.communicate(outStream, inStream);
					}
				}
				else {
					outStream.writeObject(OpCode.OP_ERROR);
				}
				System.out.println("SAIU");
				outStream.close();
				inStream.close();

				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}