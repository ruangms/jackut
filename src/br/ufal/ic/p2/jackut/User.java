package br.ufal.ic.p2.jackut;
import java.util.*;

/**
 * Contains all the user's informations and operations
 * This is the main class that the program uses
 */ 

class User {
	private String login;
	private String senha;
	private String nome;
	private String descricao;
	private String estadoCivil;
	private String aniversario;
	private String filhos;
	private String idiomas;
	private String cidadeNatal;
	private String estilo;
	private String fumo;
	private String bebo;
	private String moro;
	private ArrayList<Friendship> friends;
	private UserMessageBox messages;
	private ArrayList<Community> communities;
	private ArrayList<User> followers;
	private ArrayList<User> following;
	private ArrayList<User> crushes;
	private ArrayList<User> enemies;


	public User(String login, String senha, String nome) {
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.friends = new ArrayList<Friendship>();
		this.messages = new UserMessageBox();
		this.communities = new ArrayList<Community>();
		this.followers = new ArrayList<User>();
		this.following = new ArrayList<User>();
		this.crushes = new ArrayList<User>();
		this.enemies = new ArrayList<User>();
	}

	// ###########
	// # GETTERS #
	// ###########

	public String getLogin() {
		return this.login;
	}

	public String getSenha() {
		return this.senha;
	}

	public String getNome() {
		return this.nome;
	}
	
	public String getDescription() {
		return this.descricao;
	}

	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public String getAniversario() {
		return this.aniversario;
	}

	public String getFilhos() {
		return this.filhos;
	}

	public String getIdiomas() {
		return this.idiomas;
	}

	public String getCidadeNatal() {
		return this.cidadeNatal;
	}

	public String getEstilo() {
		return this.estilo;
	}

	public String getFumo() {
		return this.fumo;
	}

	public String getBebo() {
		return this.bebo;
	}

	public String getMoro() {
		return this.moro;
	}

	// ###################
	// # Friends getters #
	// ###################

	public int getFriendshipStatus(String friendId) {
		for (Friendship friend : this.friends) {
			if (friend.getFriend().equals(friendId)) {
				return friend.getStatus();
			}
		}
		return -1;
	}

	public String getFriendsList() {
		String friendsNames = "{";
		for (Friendship friend : this.friends) {
			if (friend.getStatus() == 1) {
				friendsNames += friend.getFriend() + ",";
			}
		}
		// Empty list of friends
		if (friendsNames.equals("{")) {
			friendsNames += "}";
		} else { // Replace last "," with a "}"
			friendsNames = friendsNames.replaceAll(",$", "}");
		}
		return friendsNames;
	}

	// ###################
	// # Message getters #
	// ###################

	public UserMessage getMessage() {
		return this.messages.getFirstUnreadMessage();
	}

	// #####################
	// # Community getters #
	// #####################

	public ListIterator<Community> getCommunitiesIterator() {
		return this.communities.listIterator();
	}

	public String getCommunitiesList() {
		String communitiesNames = "{";
		for (Community community : this.communities) {
			communitiesNames += community.getName() + ",";
		}
		// Empty list of friends
		if (communitiesNames.equals("{")) {
			communitiesNames += "}";
		} else { // Replace last "," with a "}"
			communitiesNames = communitiesNames.replaceAll(",$", "}");
		}
		return communitiesNames;
	}

	// ##############################
	// # Community messagee getters #
	// ##############################

	public CommunityMessage getCommunityMessage() {
		for (int i = 0; i < this.communities.size(); i++) {
			CommunityMessage message = this.communities.get(i).readMessage(this.getLogin());
			if (message != null) {
				return message;
			}
		}
		return null;
	}

	// ##################################
	// # Follower and following getters #
	// ##################################

	public boolean isFollower(User idol) {
		for (User f : this.following) {
			if (f.getLogin().equals(idol.getLogin())) {
				return true;
			}
		}
		return false;
	}

	public String getFollowers() {
		String followersNames = "{";
		for (User follower : this.followers) {
			followersNames += follower.getLogin() + ",";
		}
		// Empty list of friends
		if (followersNames.equals("{")) {
			followersNames += "}";
		} else { // Replace last "," with a "}"
			followersNames = followersNames.replaceAll(",$", "}");
		}
		return followersNames;
	}

	// #################
	// # Crush getters #
	// #################

	public boolean isCrush(User crush) {
		for (User user : this.crushes) {
			if (crush.getLogin().equals(user.getLogin())) {
				return true;
			}
		}
		return false;
	}

	public String getCrushes() {
		String crushesNames = "{";
		for (User crush : this.crushes) {
			crushesNames += crush.getLogin() + ",";
		}
		// Empty list of friends
		if (crushesNames.equals("{")) {
			crushesNames += "}";
		} else { // Replace last "," with a "}"
			crushesNames = crushesNames.replaceAll(",$", "}");
		}
		return crushesNames;
	}

	// #################
	// # Enemy getters #
	// #################

	public boolean isEnemy(User enemy) {
		for (User e : this.enemies) {
			if (e.getLogin().equals(enemy.getLogin())) {
				return true;
			}
		}
		return false;
	}

	// ###########
	// # SETTERS #
	// ###########

	public void setLogin(String value) {
		this.login = value;
	}

	public void setSenha(String value) {
		this.senha = value;
	}

	public void setNome(String value) {
		this.login = value;
	}

	public void setDescricao(String value) {
		this.descricao = value;
	}

	public void setEstadoCivil(String value) {
		this.estadoCivil = value;
	}

	public void setAniversario(String value) {
		this.aniversario = value;
	}

	public void setFilhos(String value) {
		this.filhos = value;
	}

	public void setIdiomas(String value) {
		this.idiomas = value;
	}

	public void setCidadeNatal(String value) {
		this.cidadeNatal = value;
	}

	public void setEstilo(String value) {
		this.estilo = value;
	}

	public void setFumo(String value) {
		this.fumo = value;
	}	

	public void setBebo(String value) {
		this.bebo = value;
	}

	public void setMoro(String value) {
		this.moro = value;
	}

	public void setAttribute(String attribute, String value) {
		if (attribute.equals(login)) {
			this.setLogin(value);
		}
		else if (attribute.equals(senha)) {
			this.setSenha(value);
		}
		else if (attribute.equals(nome)) {
			this.setNome(value);
		}
		else if (attribute.equals(descricao)) {
			this.setDescricao(value);
		}
		else if (attribute.equals(estadoCivil)) {
			this.setEstadoCivil(value);
		}
		else if (attribute.equals(aniversario)) {
			this.setAniversario(value);
		}
		else if (attribute.equals(filhos)) {
			this.setFilhos(value);
		}
		else if (attribute.equals(idiomas)) {
			this.setIdiomas(value);
		}
		else if (attribute.equals(cidadeNatal)) {
			this.setCidadeNatal(value);
		}
		else if (attribute.equals(estilo)) {
			this.setEstilo(value);
		}
		else if (attribute.equals(fumo)) {
			this.setFumo(value);
		}
		else if (attribute.equals(bebo)) {
			this.setBebo(value);
		}
		else if (attribute.equals(moro)) {
			this.setMoro(value);
		}
	}

	// ###################
	// # Friends setters #
	// ###################

	public void addFriend(String friend) {
		this.friends.add(new Friendship(friend));
	}

	// Add a new friendship and updates its status to know if bots users befriended each others
	public void updateFriendshipStatus(User friend) {
		this.friends.add(new Friendship(friend.getLogin()));
		boolean statusUpdated = false;
		for (int i = 0; i < friend.friends.size(); i++) {
			if (friend.friends.get(i).getFriend().equals(this.getLogin())) {
				friend.friends.get(i).setStatus(1);
				statusUpdated = true;
				break;
			}
		}
		if (statusUpdated) {
			for (int i = 0; i < this.friends.size(); i++) {
				if (this.friends.get(i).getFriend().equals(friend.getLogin())) {
					this.friends.get(i).setStatus(1);
				}
			}
		}
	}

	// ####################
	// # Messages setters #
	// ####################

	public void addMessage(String sender, String content) {
		this.messages.addMessage(sender, content);
	}

	public void markLastMessageAsRead() {
		this.messages.markLastMessageAsRead();
	}

	// #####################
	// # Community setters #
	// #####################
	
	public void addCommunity(Community community) {
		this.communities.add(community);
		community.addMember(this.getLogin());
	}

	// ##################################
	// # Follower and following getters #
	// ##################################

	public void follow(User user) {
		this.following.add(user);
		user.addFollower(this);
	}

	public void addFollower(User user) {
		this.followers.add(user);
	}

	// #################
	// # Crush setters #
	// #################

	public boolean addCrush(User crush) {
		this.crushes.add(crush);
		if (crush.isCrush(this)) {
			return true;	
		}
		return false;
	}

	// #################
	// # Enemy setters #
	// #################

	public boolean addEnemy(User enemy) {
		if (this.isEnemy(enemy)) {
			return false;
		}
		this.enemies.add(enemy);
		return true;
	}
}