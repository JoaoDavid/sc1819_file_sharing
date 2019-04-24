package facade.services;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import facade.exceptions.ApplicationException;
import server.business.handlers.ListUsersHandler;
import server.business.handlers.TrustUsersHandler;
import server.business.handlers.UntrustUsersHandler;

public class UserService {


	private TrustUsersHandler trustUserHandler;
	private UntrustUsersHandler untrusUserHandler;
	private ListUsersHandler listUserHandler;
	
	public UserService(TrustUsersHandler trustUserHandler, UntrustUsersHandler untrusUserHandler, ListUsersHandler listUserHandler) {
		this.trustUserHandler = trustUserHandler;
		this.untrusUserHandler = untrusUserHandler;
		this.listUserHandler = listUserHandler;
	}
	
	public boolean trustUser(String userName, String userNameTrusted, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		return this.trustUserHandler.trustUser(userName, userNameTrusted, privKey, pubKey);
	}
	
	public boolean untrustUser(String userName, String userNameUntrusted, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		return this.untrusUserHandler.untrustUser(userName, userNameUntrusted, privKey, pubKey);
	}
	
	public List<String> listUsers() throws ApplicationException {
		return this.listUserHandler.listUsers();
	}
	
	
}
