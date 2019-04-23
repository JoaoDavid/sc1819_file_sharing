package server.business.util;

import java.io.File;

public class FilePaths {

	public static final String FILE_NAME_USERS_PASSWORDS = "usersInfo.txt";
	
	public static final String FILE_NAME_USERS_DEACTIVATED = "deactivatedUsers.txt";
	
	public static final String FILE_NAME_USERS_PASSWORDS_MAC = "usersInfoMAC.txt";

	public static final String FOLDER_SERVER = System.getProperty("user.home") + File.separator + "MsgFileG51" + File.separator + "server";
	
	public static final String FOLDER_FILES = "files";
	
	public static final String FILE_NAME_MSG = "msg.txt";
	
	public static final String FILE_NAME_TRUSTED = "trusted.txt";
	
	public static final String FOLDER_SERVER_USERS = FOLDER_SERVER + File.separator + "users";

	public static final String FILE_USERS_PASSWORDS = FOLDER_SERVER + File.separator + FILE_NAME_USERS_PASSWORDS;
	
	public static final String FILE_USERS_PASSWORDS_MAC = FOLDER_SERVER + File.separator + FILE_NAME_USERS_PASSWORDS_MAC;
	
	public static final String FILE_USERS_DEACTIVATED = FOLDER_SERVER + File.separator + FILE_NAME_USERS_DEACTIVATED;
	
	public static final String DEACTIVATED_USER = "DEACTIVATED";
	
}