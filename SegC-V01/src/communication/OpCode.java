package communication;

public enum OpCode {
	
	STORE_FILES("store"), LIST_FILES("list"), REMOVE_FILES("remove"), 
	USERS("users"), TRUST_USERS("trusted"), UNTRUST_USERS("untrusted"),
	DOWNLOAD_FILE("download"), SEND_MSG("msg"), COLLECT_MSG("collect"), 
	END_CONNECTION("exit"), OP_ERROR("error"), OP_SUCCESSFUL("sucess"), 
	OP_RES_ARRAY("op res in array"), OP_HELP("help"), OP_INVALID("invalid"),
	ERR_NOT_FOUND("err not found"), ERR_ALREADY_EXISTS("err already exists"),
	ERR_NOT_FRIENDS("err not friends"), ERR_YOURSELF("err yourself");
	
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
