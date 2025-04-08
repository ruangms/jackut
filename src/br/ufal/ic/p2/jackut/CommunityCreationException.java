package br.ufal.ic.p2.jackut;

/*
 * This exception is used when the user is creating a community which name is already taken by another one
 */

class CommunityCreationException extends Exception {
	public CommunityCreationException(String message) {
		super(message);
	}
}
