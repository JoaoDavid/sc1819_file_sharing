package communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private OpCode opCode;//operation code
	private OpCode[] arrCodeParam;//used for used operations that require different opcodes for every argument
	private String strParam;//used to send a string as message
	private String[] arrStrParam;	
	private ArrayList<String> param;
	private ArrayList<Byte[]> paramBytes;
	private ArrayList<String> inbox;
	
	
	public Message (OpCode opCode) {
		this.opCode = opCode;
	}
	
	public Message(OpCode[] arrCodeParam) {
		super();
		this.arrCodeParam = arrCodeParam;
	}
	
	public Message (OpCode opCode, String strParam) {
		this.opCode = opCode;
		this.strParam = strParam;
	}
	
	public Message(OpCode opCode, String[] arrStrParam) {
		super();
		this.opCode = opCode;
		this.arrStrParam = arrStrParam;
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
		this.strParam = strParam;
		this.opCode = opCode;
		this.param = param;
		this.paramBytes = paramBytes;
		this.inbox = inbox;
	}

	public OpCode getOpCode() {
		return this.opCode;
	}
	
	public String getStrParam() {
		return this.strParam;
	}
	
	public String[] getArrStrParam() {
		return this.arrStrParam;
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
