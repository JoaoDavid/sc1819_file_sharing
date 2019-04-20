package facade.services;

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
	
	public List<String> collectMessages(String userName) throws ApplicationException {
		return collectMessagesHandler.collectMessages(userName);
	}
	
	public boolean storeMsg(String userSender, String userReceiver, String msg) throws ApplicationException {
		return sendMessageHandler.storeMsg(userSender, userReceiver, msg);
	}
}
