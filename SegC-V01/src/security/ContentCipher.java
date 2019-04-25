package security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import facade.exceptions.ApplicationException;
import server.business.util.ConstKeyStore;

public class ContentCipher {

	private KeyGenerator kg;
	private Key key;
	private String cipherAltorithm;


	public ContentCipher(String cipherAltorithm, int keySize) throws NoSuchAlgorithmException {
		kg = KeyGenerator.getInstance(cipherAltorithm);
		kg.init(keySize);
		key = kg.generateKey();
		this.cipherAltorithm = cipherAltorithm;
	}

	public Key getKey() {
		return this.key;
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


	/*public static void sigAndEcryptFile(byte[], File fileKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		FileOutputStream fos2 = new FileOutputStream(file);
		ContentCipher contentCipher = new ContentCipher(ConstKeyStore.SYMMETRIC_KEY_ALGORITHM,ConstKeyStore.SYMMETRIC_KEY_SIZE);
		byte[] fileInBytes = Files.readAllBytes(file.toPath());
		fos2.write(contentCipher.encrypt(fileInBytes));				
		Cipher c = Cipher.getInstance(pubKey.getAlgorithm());
		c.init(Cipher.WRAP_MODE, pubKey);
		FileOutputStream fosK = new FileOutputStream(fileKey);
		fosK.write(c.wrap(contentCipher.getKey()));
		fos2.close();
		fosK.close();
	}*/

	public static void ecryptFile(File file, File fileKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		FileOutputStream fos2 = new FileOutputStream(file);
		ContentCipher contentCipher = new ContentCipher(ConstKeyStore.SYMMETRIC_KEY_ALGORITHM,ConstKeyStore.SYMMETRIC_KEY_SIZE);
		byte[] fileInBytes = Files.readAllBytes(file.toPath());
		fos2.write(contentCipher.encrypt(fileInBytes));				
		Cipher c = Cipher.getInstance(pubKey.getAlgorithm());
		c.init(Cipher.WRAP_MODE, pubKey);
		FileOutputStream fosK = new FileOutputStream(fileKey);
		fosK.write(c.wrap(contentCipher.getKey()));
		fos2.close();
		fosK.close();
	}


	public static void sigAndEcryptFile(File file, File fileSig, File fileKey, PrivateKey privKey, PublicKey pubKey, byte[] content) throws Exception {
		FileOutputStream fosBefore = new FileOutputStream(file);
		fosBefore.write(content);
		fosBefore.close();
		byte[] fileInBytes = Files.readAllBytes(file.toPath());
		//signs the content of file in the file fileSig
		Signature s = Signature.getInstance(ConstKeyStore.SIGNATURE_ALGORITHM);
		s.initSign(privKey);
		s.update(fileInBytes);
		FileOutputStream fos = new FileOutputStream(fileSig);
		fos.write(s.sign());
		fos.close();
		//Encrypt the content of file
		FileOutputStream fosEncrypted = new FileOutputStream(file);
		ContentCipher contentCipher = new ContentCipher(ConstKeyStore.SYMMETRIC_KEY_ALGORITHM,ConstKeyStore.SYMMETRIC_KEY_SIZE);
		fosEncrypted.write(contentCipher.encrypt(fileInBytes));				
		//Wrap file used to encrypt file and save it in fileKey
		Cipher c = Cipher.getInstance(pubKey.getAlgorithm());
		c.init(Cipher.WRAP_MODE, pubKey);
		FileOutputStream fosK = new FileOutputStream(fileKey);
		fosK.write(c.wrap(contentCipher.getKey()));
		fosEncrypted.close();
		fosK.close();
	}

	public static void sigAndEcryptFile(File file, File fileSig, File fileKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		byte[] fileInBytes = Files.readAllBytes(file.toPath());
		//signs the content of file in the file fileSig
		Signature s = Signature.getInstance(ConstKeyStore.SIGNATURE_ALGORITHM);
		s.initSign(privKey);
		s.update(fileInBytes);
		FileOutputStream fos = new FileOutputStream(fileSig);
		fos.write(s.sign());
		fos.close();
		//Encrypt the content of file
		FileOutputStream fosEncrypted = new FileOutputStream(file);
		ContentCipher contentCipher = new ContentCipher(ConstKeyStore.SYMMETRIC_KEY_ALGORITHM,ConstKeyStore.SYMMETRIC_KEY_SIZE);
		fosEncrypted.write(contentCipher.encrypt(fileInBytes));				
		//Wrap file used to encrypt file and save it in fileKey
		Cipher c = Cipher.getInstance(pubKey.getAlgorithm());
		c.init(Cipher.WRAP_MODE, pubKey);
		FileOutputStream fosK = new FileOutputStream(fileKey);
		fosK.write(c.wrap(contentCipher.getKey()));
		fosEncrypted.close();
		fosK.close();
	}

	public static byte[] decryptFileAndCheckSig(File file, File fileSig, File fileKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		Cipher c = Cipher.getInstance(privKey.getAlgorithm());
		c.init(Cipher.UNWRAP_MODE, privKey);
		//FileInputStream fisK = new FileInputStream(fileWithKey);
		Key key = c.unwrap(Files.readAllBytes(fileKey.toPath()), ConstKeyStore.SYMMETRIC_KEY_ALGORITHM, Cipher.SECRET_KEY);
		Cipher cipher = Cipher.getInstance(key.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, key);			
		byte [] buffFile = cipher.doFinal(Files.readAllBytes(file.toPath()));
		byte [] sigInFile = Files.readAllBytes(fileSig.toPath());
		Signature s = Signature.getInstance(ConstKeyStore.SIGNATURE_ALGORITHM);
		s.initVerify(pubKey);
		s.update(buffFile);
		if (s.verify(sigInFile)) {
			return buffFile;
			/*String inFile = new String(buffFile);
			return Arrays.asList(inFile.split("/n"));*/
		}else {
			throw new Exception("FILES CORRUPTED");
		}
	}

	public static boolean checkFileIntegrity(File file, File fileSig, File fileKey, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		try {
			Cipher c = Cipher.getInstance(privKey.getAlgorithm());
			c.init(Cipher.UNWRAP_MODE, privKey);
			//FileInputStream fisK = new FileInputStream(fileWithKey);
			Key key = c.unwrap(Files.readAllBytes(fileKey.toPath()), ConstKeyStore.SYMMETRIC_KEY_ALGORITHM, Cipher.SECRET_KEY);
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key);			
			byte [] buffFile = cipher.doFinal(Files.readAllBytes(file.toPath()));
			byte [] sigInFile = Files.readAllBytes(fileSig.toPath());
			Signature s = Signature.getInstance(ConstKeyStore.SIGNATURE_ALGORITHM);
			s.initVerify(pubKey);
			s.update(buffFile);
			return s.verify(sigInFile);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException |
				IOException | IllegalBlockSizeException | BadPaddingException | SignatureException e) {
			throw new ApplicationException("CONTROL FILES WERE COMPROMISED - ABORTING");
		}

	}





}
