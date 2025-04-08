package br.ufal.ic.p2.jackut;
import java.util.*;

/**
 * Container class for manipulating a list of community messages
 */

public class CommunityMessageBox {
	private ArrayList<CommunityMessage> messages;

	public CommunityMessageBox() {
		this.messages = new ArrayList<CommunityMessage>();
	}

	public void addMessage(String sender, String content, int members) {
		this.messages.add(new CommunityMessage(sender, content, members));
	}

	
	public CommunityMessage getFirstUnreadMessage(int idx) {
		for (int i = 0; i < this.messages.size(); i++) {
			if (this.messages.get(i).getStatesLength() > idx) {
				if (this.messages.get(i).getStatus(idx) == false) {
					this.messages.get(i).markAsRead(idx);
					return this.messages.get(i);
				}
			}
		}
		return null;
	}

	public void markLastMessageAsRead(int idx) {
		int lastIdx = this.messages.size() - 1;
		this.messages.get(lastIdx).markAsRead(idx);
	}

	public void addMember() {
		for (int i = 0; i < this.messages.size(); i++) {
			this.messages.get(i).addMember();
		}
	}
}