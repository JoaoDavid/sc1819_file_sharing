package communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private OpCode opCode;//operation code
	private OpCode[] arrCode;//used for used operations that require different opcodes for every argument
	private String str;//used to send a string as message
	private String[] arrStr;	
	private ArrayList<String> arrListStr;
	private ArrayList<Byte[]> arrListArrByte;
	private Byte[] arrByte;
	private ArrayList<String> inbox;
	
	
	public Message(OpCode opCode, Byte[] arrByte) {
		super();
		this.opCode = opCode;
		this.arrByte = arrByte;
	}

	public Message (OpCode opCode) {
		this.opCode = opCode;
	}
	
	public Message(OpCode[] arrCodeParam) {
		this.arrCode = arrCodeParam;
	}
	
	public Message(OpCode opCode, ArrayList<String> param) {
		this.opCode = opCode;
		this.arrListStr = param;
	}

	public Message(OpCode opCode, OpCode[] arrCodeParam) {
		this.opCode = opCode;
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
	
	public Message(OpCode opCode, ArrayList<String> arrListStr, ArrayList<Byte[]> arrListArrBytes) {
		super();
		this.opCode = opCode;
		this.arrListStr = arrListStr;
		this.arrListArrByte = arrListArrBytes;
	}

	public Message() {
		super();
	}
	
	public OpCode[] getArrCode() {
		return arrCode;
	}

	public void setArrCode(OpCode[] arrCode) {
		this.arrCode = arrCode;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String[] getArrStr() {
		return arrStr;
	}
	
	public Byte[] getArrByte() {
		return this.arrByte;
	}

	public void setArrStr(String[] arrStr) {
		this.arrStr = arrStr;
	}

	public void setOpCode(OpCode opCode) {
		this.opCode = opCode;
	}

	public void setParam(ArrayList<String> param) {
		this.arrListStr = param;
	}

	public void setParamBytes(ArrayList<Byte[]> paramBytes) {
		this.arrListArrByte = paramBytes;
	}

	public void setInbox(ArrayList<String> inbox) {
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

	public ArrayList<String> getArrListStr() {
		return arrListStr;
	}

	public ArrayList<Byte[]> getArrListArrBytes() {
		return arrListArrByte;
	}

	public ArrayList<String> getInbox() {
		return inbox;
	}
	
}
