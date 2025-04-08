package br.ufal.ic.p2.jackut;

/*
 * This exception is used when the user is tryig to follow a community that he already follows
 */

public class CommunitySubscriptionException extends Exception {
	public CommunitySubscriptionException(String message) {
		super(message);
	}
}