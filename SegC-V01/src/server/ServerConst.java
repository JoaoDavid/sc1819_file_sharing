package server;

import java.io.File;

public class ServerConst {
	
	public static final String FILE_NAME_USERS_PASSWORDS = "usersInfo.txt";
	/** SERVER **/
	public static final String FOLDER_SERVER = "resources" + File.separator + "server";
	
	public static final String FOLDER_FILES = "files";
	
	public static final String FILE_NAME_MSG = "msg.txt";
	
	public static final String FILE_NAME_TRUSTED = "trusted.txt";
	
	public static final String FOLDER_SERVER_USERS = FOLDER_SERVER + File.separator + "users";
	/** File Users&Passwords **/
	public static final String FILE_USERS_PASSWORDS = FOLDER_SERVER + File.separator + FILE_NAME_USERS_PASSWORDS;
	
	public static final String FOLDER_CLIENT = "resources" + File.separator + "client";
}
