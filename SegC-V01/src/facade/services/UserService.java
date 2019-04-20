package facade.services;


import facade.exceptions.ApplicationException;
import server.business.handlers.TrustUsersHandler;
import server.business.handlers.UntrustUsersHandler;

public class UserService {


	private TrustUsersHandler trustUserHandler;
	private UntrustUsersHandler untrusUserHandler;
	
	public UserService(TrustUsersHandler trustUserHandler, UntrustUsersHandler untrusUserHandler) {
		this.trustUserHandler = trustUserHandler;
		this.untrusUserHandler = untrusUserHandler;
	}
	
	public boolean trustUser(String userName, String userNameTrusted) throws ApplicationException {
		return this.trustUserHandler.trustUser(userName, userNameTrusted);
	}
	
	public boolean untrustUser(String userName, String userNameUntrusted) throws ApplicationException {
		return this.untrusUserHandler.untrustUser(userName, userNameUntrusted);
	}
	
	
	
}
