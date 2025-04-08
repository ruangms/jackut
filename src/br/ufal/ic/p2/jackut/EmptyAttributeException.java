package br.ufal.ic.p2.jackut;
/*
 * This exception is used when trying to access an empty attribute of a user 
 */  

public class EmptyAttributeException extends Exception {
	public EmptyAttributeException(String message) {
		super(message);
	}
}
