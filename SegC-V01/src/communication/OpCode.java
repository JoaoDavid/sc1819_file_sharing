package communication;

public enum OpCode {
	
	STORE_FILES("store"), LIST_FILES("list"), REMOVE_FILES("remove"), 
	USERS("users"), TRUST_USERS("trusted"), UNTRUST_USERS("untrusted"),
	DOWNLOAD_FILE("download"), SEND_MSG("msg"), COLLECT_MSG("collect"), 
	END_CONNECTION("exit"),
	
	OP_ERROR("error"), OP_SUCCESSFUL("OK"), OP_RES_ARRAY("op res in array"),
	
	ERR_NOT_REGISTERED("not registered in the server"), ERR_ALREADY_EXISTS("already exists"),
	ERR_NOT_TRUSTED("not trusted"), ERR_YOURSELF("it's yourself"),
	ERR_NOT_FOUND("not found"), STORE_FILES_I("store I");
	
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
