package communication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import facade.exceptions.ApplicationException;
import security.ContentCipher;

public class Network {

	public static final int MAX_SIZE_BUFFER = 2048;



	public static void sendInt(int num, Socket socket) {
		byte[] byeNum = ByteBuffer.allocate(4).putInt(num).array();
		try {
			socket.getOutputStream().write(byeNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int receiveInt(Socket socket) throws IOException {
		byte[] buffLenByte = new byte[4];
		socket.getInputStream().read(buffLenByte);
		return ByteBuffer.wrap(buffLenByte).getInt();
	}

	public static void listToBuffer(List<String> list, Socket socket) {
		try {
			byte[] result;
			ByteArrayOutputStream result2 = new ByteArrayOutputStream();
			byte[] listLen = ByteBuffer.allocate(4).putInt(list.size()).array();
			result2.write(listLen);
			for(String curr : list) {
				byte[] byteCurrStr = curr.getBytes();
				byte[] strByteLen = ByteBuffer.allocate(4).putInt(byteCurrStr.length).array();
				result2.write(strByteLen);
				result2.write(byteCurrStr);
			}
			result = result2.toByteArray();

			//First send the number of bytes that will be sent
			Network.sendInt(result.length, socket);
			//Then sends those bytes
			socket.getOutputStream().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<String> bufferToList(Socket socket) throws IOException {
		List<String> result = new ArrayList<String>();			

		byte[] buffLenByte = new byte[4];
		socket.getInputStream().read(buffLenByte);
		int buffLen = ByteBuffer.wrap(buffLenByte).getInt();
		System.out.println("buffLen:" +buffLen);

		byte[] buff = new byte[buffLen];
		int read = socket.getInputStream().read(buff);
		if(read != buffLen) {//information lost
			throw new IOException("Information incomplete");
		}

		int i = 0;
		byte[] strLenBytes = Arrays.copyOfRange(buff, i, i + 4);
		int nStrs = ByteBuffer.wrap(strLenBytes).getInt();
		System.out.println("num strings:"+nStrs);
		i+=4;
		String[] result2 = new String[nStrs];
		int index = 0;
		while(i < buffLen) {
			strLenBytes = Arrays.copyOfRange(buff, i, i + 4);
			int currStrNumBytes = ByteBuffer.wrap(strLenBytes).getInt();
			i+=4;
			String str = new String(Arrays.copyOfRange(buff, i, i+currStrNumBytes));
			i+=currStrNumBytes;
			result2[index] = str;
			index++;
			result.add(str);
		}
		return result;
	}

	public static void sendFile(File file, Socket socket) throws IOException {
		int buffSize = (int)file.length();
		byte[] buff;
		ByteArrayOutputStream firstArrByte = new ByteArrayOutputStream();

		//First send the size of the buffer
		//including fileNameLen in bytes, fileName and finally fileBytes
		byte[] byteName = file.getName().getBytes();
		buffSize += byteName.length + 4;
		firstArrByte.write(ByteBuffer.allocate(4).putInt(buffSize).array());
		socket.getOutputStream().write(firstArrByte.toByteArray());
		firstArrByte.reset();
		//sending the buffer
		if(buffSize > MAX_SIZE_BUFFER) {
			//cycle
		}else {//<=
			byte [] buffFile = Files.readAllBytes(file.toPath());
			firstArrByte.write(ByteBuffer.allocate(4).putInt(byteName.length).array());
			firstArrByte.write(byteName);
			firstArrByte.write(buffFile);
			socket.getOutputStream().write(firstArrByte.toByteArray());
		}



	}

	public static boolean receiveFile(String path, Socket socket, boolean replace) {
		try {
			byte[] buffLenByte = new byte[4];
			socket.getInputStream().read(buffLenByte);
			int buffLen = ByteBuffer.wrap(buffLenByte).getInt();
			System.out.println("buffLen:" +buffLen);
			if(buffLen < 0) {
				return false;
			}

			byte[] buff = new byte[buffLen];
			int read = socket.getInputStream().read(buff);
			if(read != buffLen) {//information lost
				throw new IOException("Information incomplete");
			}

			int i = 0;			
			byte[] strLenByte = Arrays.copyOfRange(buff, i, i + 4);
			i+=4;
			int strLen = ByteBuffer.wrap(strLenByte).getInt();
			String fileName = new String(Arrays.copyOfRange(buff, i, i + strLen));
			i+=strLen;
			File file = new File(path + fileName);
			if(file.exists() && !replace) {
				return false;
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(buff, i, buffLen - i);
			fos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean receiveFileAndCipher(String path, Socket socket, boolean replace, PublicKey pubKey) {
		try {
			byte[] buffLenByte = new byte[4];
			socket.getInputStream().read(buffLenByte);
			int buffLen = ByteBuffer.wrap(buffLenByte).getInt();
			System.out.println("buffLen:" +buffLen);
			if(buffLen < 0) {
				return false;
			}

			byte[] buff = new byte[buffLen];
			int read = socket.getInputStream().read(buff);
			if(read != buffLen) {//information lost
				throw new IOException("Information incomplete");
			}

			int i = 0;			
			byte[] strLenByte = Arrays.copyOfRange(buff, i, i + 4);
			i+=4;
			int strLen = ByteBuffer.wrap(strLenByte).getInt();
			String fileName = new String(Arrays.copyOfRange(buff, i, i + strLen));
			i+=strLen;
			File file = new File(path + fileName);
			if(file.exists() && !replace) {
				return false;
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ContentCipher contentCipher = new ContentCipher("AES",128);
			byte[] fileInBytes = Arrays.copyOfRange(buff, i, buffLen - i);
			fos.write(contentCipher.encrypt(fileInBytes));
			File fileWithKey = new File(path + fileName + ".key");
			fileWithKey.createNewFile();
			Cipher c = Cipher.getInstance(pubKey.getAlgorithm());
			c.init(Cipher.WRAP_MODE, pubKey);
			FileOutputStream fosK = new FileOutputStream(fileWithKey);
			fosK.write(c.wrap(contentCipher.getKey()));
			fos.close();
			fosK.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
