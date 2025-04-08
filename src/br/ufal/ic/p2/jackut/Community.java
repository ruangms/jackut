package br.ufal.ic.p2.jackut;
import java.util.*;

/**
 * Class for community
 */

public class Community {
	private String owner;
	private String name;
	private String description;
	private ArrayList<String> members;
	private CommunityMessageBox messages;

	public Community(String owner, String name, String description) {
		this.owner = owner;
		this.name = name;
		this.description = description;
		this.members = new ArrayList<String>();
		this.messages = new CommunityMessageBox();
	}

	public String getOwner() {
		return this.owner;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getMembers() {
		String membersNames = "{";
		for (String member : this.members) {
			membersNames += member + ",";
		}
		// Replace last "," with a "}"
		membersNames = membersNames.replaceAll(",$", "}");
		return membersNames;
	}

	public ListIterator<String> getMembersIterator() {
		return this.members.listIterator();
	}

	public void addMember(String userLogin) {
		if(!this.members.contains(userLogin)){
			this.members.add(userLogin);
		}
	}

	public boolean isMember(String userLogin) {
		for (String member : this.members) {
			if (member.equals(userLogin)) {
				return true;
			}
		}
		return false;
	}

	public void addMessage(String sender, String content) {
		this.messages.addMessage(sender, content, this.members.size());
	}

	public CommunityMessage readMessage(String loginId) {
		for (int i = 0; i < this.members.size(); i++) {
			if (loginId.equals(this.members.get(i))){
				return this.messages.getFirstUnreadMessage(i);
			}
		}
		return null;
	}
}