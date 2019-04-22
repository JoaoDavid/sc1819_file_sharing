package users;

import java.util.Scanner;


public class UserManager {

	private Handler handler;


	public UserManager(String alias, String password, String keystoreLocation, String keystorePassword) throws Exception {
		this.handler = new Handler(alias, password, keystoreLocation, keystorePassword);
	}

	public static void main(String[] args) throws Exception {//eliminar
		for(String curr : args) {
			System.out.println(curr);
		}
		if(args.length == 4) {
			UserManager userManager = new UserManager(args[0],args[1],args[2],args[3]);
			userManager.startParser();			
		}else {
			System.out.println("args: <alias> <password> <keystore location> <keystore password>");
		}

	}

	public void startParser() throws Exception {
		Scanner sc = new Scanner(System.in);
		boolean onLoop = true;
		while(onLoop) {
			System.out.print(">>>");
			String rawInput = sc.nextLine();
			String[] parsedInput = rawInput.split("(\\s)+");
			for(String curr : parsedInput) {
				System.out.println(curr);
			}
			String userName;
			String password;
			//fazer try aqui
			switch (parsedInput[0]) {//command <user> <password>
			case "create":
				if(parsedInput.length == 3) {
					userName = parsedInput[1];
					password = parsedInput[2];
					this.createUser(userName, password);
				}else {

				}
				break;

			case "remove":
				if(parsedInput.length == 3) {
					userName = parsedInput[0];
					this.removeUser(userName);
				}else {

				}
				break;

			case "update":
				if(parsedInput.length == 3) {
					userName = parsedInput[0];
					password = parsedInput[1];
					this.updateUser(userName, password);
				}else {

				}
				break;
			case "exit":
				if(parsedInput.length == 1) {
					onLoop = false;
				}else {

				}
				break;
			default:
				System.out.println("Command is not recognized");
				break;
			}
		}
		sc.close();
	}


	private void createUser(String userName, String password) throws Exception {
		this.handler.createUser(userName, password);

	}
	
	private void removeUser(String userName) {
		this.handler.removeUser(userName);

	}
	
	private void updateUser(String userName, String password) {
		this.handler.updateUser(userName, password);
	}
}



