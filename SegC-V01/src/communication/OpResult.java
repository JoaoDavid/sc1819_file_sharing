package communication;

public class OpResult {
	public static final int SUCCESS = -1;
	public static final int ERROR = -2;
	public static final int NOT_REGISTERED = -3;	
	public static final int NOT_TRUSTED = -4;
	public static final int ALREADY_EXISTS = -5;
	public static final int NOT_FOUND = -6;


	
	public static String getDesig(int errorCode) {
		switch(errorCode) {
		case SUCCESS:
			return "SUCCESS";
			//break;
		case ERROR:
			return "error";
			//break;
		case NOT_REGISTERED:
			return "not registered in the server";
			//break;
		case NOT_TRUSTED:
			return "not trusted";
			//break;
		case ALREADY_EXISTS:
			return "already exists";
			//break;
		case NOT_FOUND:
			return "not found";
			//break;
		default:
			break;
		
		}
		return "invalid code";
	}
}
