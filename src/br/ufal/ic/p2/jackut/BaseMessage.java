package br.ufal.ic.p2.jackut;

/**
 * Base class for messages
 * This class is extended by UserMessage and CommunityMessage
 */

class BaseMessage {
	protected String sender;
	protected String content;

	public BaseMessage(String sender, String content) {
		this.sender = sender;
		this.content = content;
	}

	public String getSender() {
		return this.sender;
	}

	public String getContent() {
		return this.content;
	}
}