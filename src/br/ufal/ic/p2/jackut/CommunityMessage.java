package br.ufal.ic.p2.jackut;
import java.util.*;

/**
 * Class for community messages
 */

class CommunityMessage extends BaseMessage {
	private ArrayList<Boolean> status;

	public CommunityMessage(String sender, String content, int members) {
		super(sender, content);
		this.status = new ArrayList<Boolean>(Arrays.asList(new Boolean[members]));
		Collections.fill(this.status, false);
	}

	public boolean getStatus(int idx) {
		return this.status.get(idx);
	}

	public int getStatesLength() {
		return this.status.size();
	}

	public void markAsRead(int idx) {
		this.status.set(idx, true);
	}

	public void addMember() {
		this.status.add(false);
	}
}