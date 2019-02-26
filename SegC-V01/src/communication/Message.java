package communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errMsg;//only used for error op code
	//private boolean opSucc;//used only for returning the operation result
	private OpCode opCode;
	
	private ArrayList<String> param;
	private ArrayList<Byte[]> paramBytes;
	
	private ArrayList<String> inbox;
	
	public Message (OpCode opCode) {
		this.opCode = opCode;
	}
	
	public Message (OpCode opCode, String errMsg) {
		this.opCode = opCode;
		this.errMsg = errMsg;
	}
	
	public OpCode getOpCode() {
		return this.opCode;
	}
	
	public String getErrMsg() {
		return this.errMsg;
	}
}
