package br.ufal.ic.p2.jackut;

/**
 * Class for user messages 
 */

class UserMessage extends BaseMessage {
	private boolean read;

	public UserMessage(String sender, String content) {
		super(sender, content);
		this.read = false;
	}

	public boolean isRead() {
		return this.read;
	}

	public void markAsRead() {
		this.read = true;
	}
}