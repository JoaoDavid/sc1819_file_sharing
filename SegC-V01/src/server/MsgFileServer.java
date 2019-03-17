package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;

//Servidor MsgFileServer

public class MsgFileServer{

	private static final String CLASS_NAME = MsgFileServer.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	private Manager accM = Manager.getInstance();

	public static void main(String[] args) {
		if(args.length == 1) {
			int port;
			try {
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e){
				logger.log(Level.SEVERE, "Server failed: Invalid port");
				return;
			}
			logger.log(Level.INFO,"Initializing server on port: " + args[0]);
			MsgFileServer server = new MsgFileServer();
			server.startServer(port);
		}else {
			logger.log(Level.SEVERE, "Server failed: <port> is the only argument required");
		}
	}

	public void startServer (int port){
		ServerSocket sSoc = null;
		boolean isReady = true;

		try {
			sSoc = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			isReady = false;
			System.exit(-1);
		}

		while(isReady) {
			try {
				Socket inSoc = sSoc.accept();
				logger.log(Level.INFO, "Client connecting...");
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}

		if(sSoc != null) {
			try {
				sSoc.close();
			} catch (IOException e) {
				logger.log(Level.CONFIG, "Can not close the Socket Server");
			}
		}

	}

	//save users logged
	private static List<String> usersLogged = (List<String>) Collections.synchronizedList(new ArrayList<String>());

	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			logger.log(Level.CONFIG, "Thread created by server");
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
				}catch (ClassNotFoundException e1) {
					outStream.close();
					inStream.close();
					logger.log(Level.SEVERE ,"Erro autenticacao", e1);
					return;
				}

				if (accM.login(user, passwd)){

					boolean islogged = false; 
					synchronized (usersLogged) {
						if(!usersLogged.contains(user)){
							usersLogged.add(user);
							logger.log(Level.INFO, "Client connected: " + user + " logged in");
							outStream.writeObject(new Message(OpCode.OP_SUCCESSFUL));//envia true para o cliente a confirmar conecao
							islogged = true;
						}else{ 
							logger.log(Level.INFO, "Error: client " + user + " already logged");
							outStream.writeObject(new Message(OpCode.ERR_ALREADY_EXISTS));//envia true para o cliente a confirmar conecao
						}

					}
					

					try {
						while(islogged) {
							Object obj = inStream.readObject();

							if(obj == null || !(obj instanceof Message)) {
								break;
							}
							Message msgReceived = (Message) obj;

							if(OpCode.END_CONNECTION == msgReceived.getOpCode()) {
								Message msgSent = new Message(OpCode.OP_SUCCESSFUL);
								outStream.writeObject(msgSent);
								logger.log(Level.INFO, "Client disconnected: " + user + " logged out");
								break;
							}else {
								//processar msg
								Message msgSent = Skel.invoke(msgReceived, user);
								outStream.writeObject(msgSent);
							}
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();//rever esta excecao
					} catch (SocketException e) {//resolver lancamento de exception quando user e desligado abruptamente
						logger.log(Level.INFO, "Client disconnected: Connection lost with " + user);
					}finally{
						if(islogged){
							synchronized (usersLogged) {
								usersLogged.remove(user);
							}
						}
					}
				}
				else {
					System.out.println("Password errada " + user);
					outStream.writeObject(new Message(OpCode.OP_ERROR));//envia false para o cliente a rejeitar conexao
				}
				outStream.close();
				inStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}