package facade.services;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import facade.exceptions.ApplicationException;
import server.business.handlers.CollectMessagesHandler;
import server.business.handlers.SendMessageHandler;

public class MessageService {
	
	private CollectMessagesHandler collectMessagesHandler;
	private SendMessageHandler sendMessageHandler;
	
	public MessageService(CollectMessagesHandler collectMessagesHandler, SendMessageHandler sendMessageHandler) {
		this.collectMessagesHandler = collectMessagesHandler;
		this.sendMessageHandler = sendMessageHandler;
	}
	
	public List<String> collectMessages(String userName, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		return collectMessagesHandler.collectMessages(userName, privKey, pubKey);
	}
	
	public void storeMsg(String userSender, String userReceiver, String msg, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		sendMessageHandler.storeMsg(userSender, userReceiver, msg, privKey, pubKey);
	}
}
