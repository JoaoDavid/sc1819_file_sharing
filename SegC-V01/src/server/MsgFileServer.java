package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import communication.Message;
import communication.OpCode;

//Servidor MsgFileServer

public class MsgFileServer{
	
	
	private Manager accM = new Manager("usersInfo.txt");

	public static void main(String[] args) {
		if(args.length == 1) {
			int port;
			try {
			   port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e){
			   System.out.print("Server failed: Invalid port");
			   return;
			}
			System.out.println("Initializing server on port: " + args[0]);
			MsgFileServer server = new MsgFileServer();
			server.startServer(port);
		}else {
			System.out.print("Server failed: <port> is the only argument required");
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
					System.out.println("thread: depois de receber a password e o user");//remover
					System.out.println("username:" + user + " password: " + passwd);//remover
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
					outStream.close();
					inStream.close();
	 			
					socket.close();
					System.out.println("Erro autenticacao");
					return;
				}
 			
				//TODO: refazer
				//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
				//nao faz qualquer tipo de autenticacao
				if (accM.login(user, passwd)){
					System.out.println(user + " Logged in");
					outStream.writeObject(new Boolean(true));//envia true para o cliente a confirmar conecao
					try {
						while(true) {
							Message msg = (Message) inStream.readObject();
							if(OpCode.END_CONNECTION == msg.getOpCode()) {
								break;
							}
							//processar msg
							Skel.invoke(msg);
							
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