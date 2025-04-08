package br.ufal.ic.p2.jackut;
/*
 * This exception is used when:
 *     - The user is trying to send a follow himself or a user that he is already following this exception is also used for crushes
 */  

public class UserFollowException extends Exception {
	public UserFollowException(String message) {
		super(message);
	}
}
