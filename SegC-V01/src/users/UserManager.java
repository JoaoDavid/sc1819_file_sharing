package users;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class UserManager {

	public static void main(String[] args) throws Exception {//eliminar{
		if(args.length == 3) {
			FileOutputStream fos = new FileOutputStream("test");
			Mac mac = Mac.getInstance("HmacSHA1");
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream fis = new FileInputStream(args[2]);
			ks.load(fis,null);
			SecretKey key = (SecretKey) ks.getKey(args[0], args[1].toCharArray());
			mac.init(key);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			String data = "bla bla bla";
			byte buf[] = data.getBytes();
			mac.update(buf);
			oos.writeObject(data);
			oos.writeObject(mac.doFinal());
			fos.close();
		}else {
			System.out.println("args: <alias> <password> <keystore location>");
		}

	}

}
