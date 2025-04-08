package br.ufal.ic.p2.jackut;

class Message {
	private String sender;
	private String content;
	private boolean read;

	public Message(String sender, String content) {
		this.sender = sender;
		this.content = content;
		this.read = false;
	}

	public String getSender() {
		return this.sender;
	}

	public String getContent() {
		return this.content;
	}

	public boolean isRead() {
		return this.read;
	}

	public void markAsRead() {
		this.read = true;
	}
}