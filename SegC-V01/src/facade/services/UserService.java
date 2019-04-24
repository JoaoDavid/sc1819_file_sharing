package facade.services;


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
	
	public boolean trustUser(String userName, String userNameTrusted) throws ApplicationException {
		return this.trustUserHandler.trustUser(userName, userNameTrusted);
	}
	
	public boolean untrustUser(String userName, String userNameUntrusted) throws ApplicationException {
		return this.untrusUserHandler.untrustUser(userName, userNameUntrusted);
	}
	
	public List<String> listUsers() throws ApplicationException {
		return this.listUserHandler.listUsers();
	}
	
	
}
