package br.ufal.ic.p2.jackut;
/*
 * This exception is used when:
 *     - A user is trying to send a friend request to himself 
 *     - A user is trying to send a request to someone that is already on the user freind list
 *     - The user is trying to send a request to someone that has already recieved a request from him
 */  

public class FriendRequestException extends Exception {
	public FriendRequestException(String message) {
		super(message);
	}
}
