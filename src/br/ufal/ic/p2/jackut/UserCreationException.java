package br.ufal.ic.p2.jackut;
/*
 * This exception is used when trying to create a new account. Here's the case when this exceptiong might be thrown:
 *   - The user already exists in the database
 *   - The user hasn't enterd a password (senha)
 *   - The user hans't enterd a id (login)  
 */  

public class UserCreationException extends Exception {
	public UserCreationException(String message) {
		super(message);
	}
}
