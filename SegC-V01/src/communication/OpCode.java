package communication;

public enum OpCode {
	
	STORE_FILES("store"), LIST_FILES("list"), REMOVE_FILES("remove"), 
	USERS("users"), TRUST_USERS("trusted"), UNTRUST_USERS("untrusted"),
	DOWNLOAD_FILE("download"), SEND_MSG("msg"), SHOW_MSG("collect"), 
	END_CONNECTION("exit"), OP_ERROR("error"), OP_SUCCESSFUL("sucess"),
	OP_HELP("help"), OP_INVALID("invalid"), ERR_NOT_FOUND("not found"), ERR_ALREADY_EXISTS("already exists");
	
	private final String val;
	/**
     * @param text
     */
    OpCode(final String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
