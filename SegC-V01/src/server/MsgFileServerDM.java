package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import communication.OpCode;
import facade.exceptions.ApplicationException;
import facade.services.FileService;
import facade.services.MessageService;
import facade.services.UserService;
import facade.startup.MsgFileServerApp;

//Servidor myServer

public class MsgFileServerDM{
	
	private MsgFileServerApp app;
	private FileService fileService;
	private MessageService msgService;
	private UserService userService;
	
	public MsgFileServerDM() {
		this.app = new MsgFileServerApp();
		this.fileService = new FileService(app.getDownloadFileHandler(), 
				app.getListFilesHandler(), app.getRemoveFilesHandler(), 
				app.getStoreFileHandler());
		this.msgService = new MessageService(app.getCollectMessagesHandler(), 
				app.getSendMessageHandler());
		this.userService = new UserService(app.getTrustUsersHandler(), app.getUntrustUsersHandler());
	}

	public static void main(String[] args) {
		if(args.length == 1) {
			int port;
			try {
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e){
				System.out.println( "Server failed: Invalid port");
				return;
			}
			System.out.println("Initializing server on port: " + args[0]);
			MsgFileServerDM server = new MsgFileServerDM();
			server.startServer(port);
		}else {
			System.out.println("Server failed: <port> is the only argument required");
		}
	}

	public void startServer (int port){
		ServerSocket sSoc = null;
        
		try {
			sSoc = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
         
		while(true) {
			try {
				Socket inSoc = sSoc.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
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
 			
				//TODO: refazer
				//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
				//nao faz qualquer tipo de autenticacao
				if (user.length() != 0){
					outStream.writeObject(OpCode.OP_SUCCESSFUL);
				}
				else {
					outStream.writeObject(OpCode.OP_ERROR);
				}
				Skeleton skel = new Skeleton(user, socket, fileService, msgService, userService);
				while(true) {
					skel.communicate(outStream, inStream);
				}

				//outStream.close();
				//inStream.close();
 			
				//socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ApplicationException e) {//remover
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}