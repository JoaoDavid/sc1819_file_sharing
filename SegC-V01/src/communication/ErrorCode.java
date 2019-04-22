package communication;

public enum ErrorCode {
	OP_ERROR(-1),
	ERR_NOT_REGISTERED(-2),	
	ERR_NOT_TRUSTED(-3),
	ERR_ALREADY_EXISTS(-4),
	ERR_NOT_FOUND(-5);

	private final int id;
	/**
     * @param text
     */
	ErrorCode(int id) {
        this.id = id;
	}

	
	public int getId() {
		return this.id;
	}
	
	public static String getDesig(ErrorCode errorCode) {
		switch(errorCode) {
		case OP_ERROR:
			return "error";
			//break;
		case ERR_NOT_REGISTERED:
			return "not registered in the server";
			//break;
		case ERR_NOT_TRUSTED:
			return "not trusted";
			//break;
		case ERR_ALREADY_EXISTS:
			return "already exists";
			//break;
		case ERR_NOT_FOUND:
			return "not found";
			//break;
		default:
			break;
		
		}
		return null;
	}
}
