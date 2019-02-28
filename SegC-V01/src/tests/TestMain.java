package tests;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.Manager;

public class TestMain {

	public static void main(String[] args) throws IOException {
		Manager accM = Manager.getInstance();
		/*boolean res = accM.createAccount("fernando", "pass5353");
		accM.createAccount("fernangdsgdo", "pasdagadgs5353");
		accM.createAccount("fernanadgdgdo", "pasadgags5353");
		System.out.println(res);*/
		/*System.out.println(accM.login("fernando", "batata"));
		System.out.println(accM.login("joao", "cenoura"));
		System.out.println(accM.login("rita", "alface"));
		System.out.println(accM.login("ana", "alface"));
		
		
		System.out.println(accM.trusted("fernando", "rita"));
		System.out.println(accM.isRegistered("fernando"));*/
		/*
		String test = "ola";
		String[] vetorTest = test.split("(\\s)+");
		for(String ola : vetorTest) {
			System.out.println(ola);
		}*/
		/*accM.storeMsg("joca", "fernando", "ola fernando mekie?");
		accM.storeMsg("miguelito", "fernando", "easy peasy lemon squezy");
		ArrayList<String> batata =  accM.collectMsg("fernando");
		for(String ola : batata) {
			System.out.println(ola);
		}*/
		for(String ola : accM.listFiles("fernando")) {
			System.out.println(ola);
		}
		String palavra = "ola";
		Pattern p = Pattern.compile("[a-z]");
		Matcher m = p.matcher(palavra);
		if (m.find()) {
			System.out.println(palavra + " matches");
		}else {
			System.out.println(palavra + " DOES NOT match");
		}
	}

}
