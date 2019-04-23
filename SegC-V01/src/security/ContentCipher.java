package security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class ContentCipher {

	private KeyGenerator kg;
	private Key key;
	private String cipherAltorithm;
	
	
	public ContentCipher(String keyGenAalgorithm, String cipherAltorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
		kg = KeyGenerator.getInstance(keyGenAalgorithm);
		key = kg.generateKey();
		this.cipherAltorithm = cipherAltorithm;
	}
	
	public byte[] encrypt(byte[] input) {
		try {
			Cipher c = Cipher.getInstance(cipherAltorithm);
			c.init(Cipher.ENCRYPT_MODE, key);
			return c.doFinal(input);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] input) {
		try {
			Cipher c = Cipher.getInstance(cipherAltorithm);
			c.init(Cipher.DECRYPT_MODE, key);
			return c.doFinal(input);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
}
