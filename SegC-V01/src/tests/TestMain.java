package tests;

import java.io.IOException;
import java.util.ArrayList;

import communication.Message;
import communication.OpCode;

public class TestMain {

	public static void main(String[] args) throws IOException {
		/*ArrayList<String> nameFiles = new ArrayList<>();
		nameFiles.add("UM");
		String[] vetor = new String[5];
		Message msg = new Message(OpCode.OP_SUCCESSFUL, vetor);
		String[] vetor2 = msg.getArrStr();
		vetor2[2] = "batata";
		for (String str : vetor) {
			System.out.println(str);
		}*/
		String currentUsersHomeDir = System.getProperty("user.home");
		System.out.println(currentUsersHomeDir);
	}

}
