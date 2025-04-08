package br.ufal.ic.p2.jackut;
/*
 * This exception is used when trying to login with the wrong credentials 
 */  

public class LoginFailedException extends Exception {
	public LoginFailedException(String message) {
		super(message);
	}
}
