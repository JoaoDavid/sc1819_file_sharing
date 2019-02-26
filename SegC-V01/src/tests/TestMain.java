package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import server.Manager;

public class TestMain {

	public static void main(String[] args) throws IOException {
		Manager accM = new Manager("ola.txt");
		/*boolean res = accM.createAccount("fernando", "pass5353");
		accM.createAccount("fernangdsgdo", "pasdagadgs5353");
		accM.createAccount("fernanadgdgdo", "pasadgags5353");
		System.out.println(res);*/
		/*System.out.println(accM.login("fernando", "batata"));
		System.out.println(accM.login("joao", "cenoura"));
		System.out.println(accM.login("rita", "alface"));
		System.out.println(accM.login("ana", "alface"));
		
		for(String ola : accM.listUsers()) {
			System.out.println(ola);
		}
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
	}

}
