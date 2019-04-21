package communication;

public enum ErrorCode {
	OP_ERROR("error",-1),
	ERR_NOT_REGISTERED("not registered in the server", -2),	
	ERR_NOT_TRUSTED("not trusted",-3),
	ERR_ALREADY_EXISTS("already exists",-4),
	ERR_NOT_FOUND("not found",-5);
	
	private final String desig;
	private final int id;
	/**
     * @param text
     */
	ErrorCode(int id) {
        this.desig = desig;
        this.id = id;
	}
	
	public String getDesig() {
		return this.desig;
	}
	
	public int getId() {
		return this.id;
	}
	
}
