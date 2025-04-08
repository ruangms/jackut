package br.ufal.ic.p2.jackut;
/*
 * This exception is used when User1 is trying to send a friend request or follow
 * or send a message or add as crush User2 and User1 is in the enemy list of User2 
 */  

public class EnemyException extends Exception {
	public EnemyException(String message) {
		super(message);
	}
}
