package br.ufal.ic.p2.jackut;

/**
 * Creates a friend relationship with another user and has a status that can be:
 * 0 => user A is friend of B but user B is not friend of A
 * 1 => both user A and B are friends
 */ 


class Friendship {
	private String friend;
	private int status;

	public Friendship (String friend) {
		this.friend = friend;
		this.status = 0;
	}

	public String getFriend() {
		return this.friend;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}