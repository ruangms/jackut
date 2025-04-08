package br.ufal.ic.p2.jackut;
/*
 * This exception is used when the user doesn't exists or when the user is missing from the argument of a function 
 */  

public class UserNotFoundException extends Exception {
	public UserNotFoundException(String message) {
		super(message);
	}
}
