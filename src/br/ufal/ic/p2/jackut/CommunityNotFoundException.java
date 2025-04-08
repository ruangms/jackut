package br.ufal.ic.p2.jackut;

/*
 * This exception is used when the user is tryig to get information about a community that does not exists
 */

class CommunityNotFoundException extends Exception {
	public CommunityNotFoundException(String message) {
		super(message);
	}
}