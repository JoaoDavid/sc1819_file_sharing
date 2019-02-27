package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;

//Servidor MsgFileServer

public class MsgFileServer{

	private static final String CLASS_NAME = MsgFileServer.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	private Manager accM = new Manager("usersInfo.txt");

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


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			logger.log(Level.CONFIG, "Thread created by server");
		}

		public void run(){
			try {
				boolean isReady = true;
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
					logger.log(Level.INFO, "Client connected, " + user + " logged in");
					outStream.writeObject(new Boolean(true));//envia true para o cliente a confirmar conecao
					
					try {
						while(isReady) {
							Object obj = inStream.readObject();
							
							if(obj == null || !(obj instanceof Message)) {
								isReady = false;
							}
							Message msg = (Message) obj;
							
							if(!isReady || OpCode.END_CONNECTION == msg.getOpCode()) {
								isReady = false;
							}else {
								//processar msg
								Skel.invoke(msg);
							}
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();//rever esta excecao
					}
				}
				else {
					System.out.println("Password errada");
					outStream.writeObject(new Boolean(false));//envia false para o cliente a rejeitar conexao
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