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
	
	
	public Message(OpCode opCode, Byte[] arrByte) {
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
		this.opCode = opCode;
		this.arrStr = arrStrParam.clone();
	}
	
	public Message(OpCode opCode, ArrayList<String> arrListStr, ArrayList<Byte[]> arrListArrBytes) {
		this.opCode = opCode;
		this.arrListStr = arrListStr;
		this.arrListArrByte = arrListArrBytes;
	}
	
	public OpCode[] getArrCode() {
		return arrCode;
	}

	public String getStr() {
		return str;
	}

	public String[] getArrStr() {
		return arrStr;
	}
	
	public Byte[] getArrByte() {
		return this.arrByte;
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
		return this.arrStr.clone();
	}

	public ArrayList<String> getArrListStr() {
		return arrListStr;
	}

	public ArrayList<Byte[]> getArrListArrBytes() {
		return arrListArrByte;
	}
	
}
