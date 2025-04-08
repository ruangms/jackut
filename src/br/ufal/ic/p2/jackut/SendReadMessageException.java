package br.ufal.ic.p2.jackut;
/*
 * This exception is used when:
 *     - The user is trying to send a message to himself
 *     - The user is trying to read a new message but there are no message to be read 
 */  

public class SendReadMessageException extends Exception {
	public SendReadMessageException(String message) {
		super(message);
	}
}
