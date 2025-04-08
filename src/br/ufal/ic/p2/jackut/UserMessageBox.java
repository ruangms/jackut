package br.ufal.ic.p2.jackut;
import java.util.*;

/**
 * Container class for manipulating a list of user messages
 */ 


public class UserMessageBox {
	private ArrayList<UserMessage> messages;

	public UserMessageBox() {
		this.messages = new ArrayList<UserMessage>();
	}

	public void addMessage(String sender, String content) {
		this.messages.add(new UserMessage(sender, content));
	}

	public UserMessage getFirstUnreadMessage() {
		for (int i = 0; i < this.messages.size(); i++) {
			if (!this.messages.get(i).isRead()) {
				this.messages.get(i).markAsRead();
				return this.messages.get(i);
			}
		}
		return null;
	}

	public void markLastMessageAsRead() {
		int lastIdx = this.messages.size() - 1;
		this.messages.get(lastIdx).markAsRead();
	}
}