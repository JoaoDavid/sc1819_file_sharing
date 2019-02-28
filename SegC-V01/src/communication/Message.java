package communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private OpCode opCode;//operation code
	private OpCode[] arrCode;//used for used operations that require different opcodes for every argument
	private String str;//used to send a string as message
	private String[] arrStr;	
	private ArrayList<String> param;
	private ArrayList<Byte[]> paramBytes;
	private ArrayList<String> inbox;
	
	
	public Message (OpCode opCode) {
		this.opCode = opCode;
	}
	
	public Message(OpCode[] arrCodeParam) {
		super();
		this.arrCode = arrCodeParam;
	}
	
	public Message (OpCode opCode, String strParam) {
		this.opCode = opCode;
		this.str = strParam;
	}
	
	public Message(OpCode opCode, String[] arrStrParam) {
		super();
		this.opCode = opCode;
		this.arrStr = arrStrParam;
	}	

	public Message(OpCode opCode, ArrayList<String> param, ArrayList<Byte[]> paramBytes) {
		super();
		this.opCode = opCode;
		this.param = param;
		this.paramBytes = paramBytes;
	}
	
	public Message(OpCode opCode, ArrayList<String> param, ArrayList<Byte[]> paramBytes, ArrayList<String> inbox) {
		super();
		this.opCode = opCode;
		this.param = param;
		this.paramBytes = paramBytes;
		this.inbox = inbox;
	}

	public Message(String strParam, OpCode opCode, ArrayList<String> param, ArrayList<Byte[]> paramBytes,
			ArrayList<String> inbox) {
		super();
		this.str = strParam;
		this.opCode = opCode;
		this.param = param;
		this.paramBytes = paramBytes;
		this.inbox = inbox;
	}

	public OpCode getOpCode() {
		return this.opCode;
	}
	
	public OpCode[] getOpCodeArr() {
		return this.arrCode;
	}
	
	public String getStrParam() {
		return this.str;
	}
	
	public String[] getArrStrParam() {
		return this.arrStr;
	}

	public ArrayList<String> getParam() {
		return param;
	}

	public ArrayList<Byte[]> getParamBytes() {
		return paramBytes;
	}

	public ArrayList<String> getInbox() {
		return inbox;
	}
	
}
