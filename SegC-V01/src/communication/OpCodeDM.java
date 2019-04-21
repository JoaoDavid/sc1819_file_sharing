package communication;

public enum OpCodeDM {
	
	STORE_FILES("store"),
	LIST_FILES("list"),
	REMOVE_FILES("remove"), 
	USERS("users"),
	TRUST_USERS("trusted"),
	UNTRUST_USERS("untrusted"),
	DOWNLOAD_FILE("download"),
	SEND_MSG("msg"),
	COLLECT_MSG("collect"), 
	END_CONNECTION("exit"),
	
	OP_SUCCESSFUL("OK");

	
	private final String val;
	/**
     * @param text
     */
    OpCodeDM(final String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
