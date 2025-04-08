package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.*;


public class Facade {
	private ArrayList<User> users;
	private ArrayList<Community> communities;
	private DatabaseManager database;

	// Class initialization will connect to the database and load the data into memory
	public Facade() {
		this.database = new DatabaseManager("src/br/ufal/ic/p2/jackut/database/data.db");
		this.loadData();
	}

	public void loadData() { 
		this.loadUserData();
		this.loadFriendshipData();
		this.loadMessagesData();
		this.loadCommunitiesData();
		this.loadCommunityMessages();
		this.loadCommuntyMembers();		
		this.loadFollowersData();
		this.loadCrushData();
		this.loadEnemyData();
	}

	// ################
	// # Data Loaders #
	// ################

	// Loads users into memory
	public void loadUserData() {
		this.users = new ArrayList<User>();
		ArrayList<String> attributes = new ArrayList<>(List.of("descricao", "estadoCivil", "aniversario", "filhos", "idiomas", "cidadeNatal", "estilo", "fumo", "bebo", "moro"));
		
		String sql = "SELECT * FROM Users";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				User newUser = new User(rs.getString("login"), rs.getString("senha"), rs.getString("nome"));
				for (String attribute : attributes) {
					newUser.setAttribute(attribute, rs.getString(attribute));
				}
				this.users.add(newUser);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads friendships into memory
	public void loadFriendshipData() {
		String sql = "SELECT * FROM friendship";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String user = rs.getString("user");
				String friend = rs.getString("friend");
				int userIdx = this.findUser(user);
				int friendIdx = this.findUser(friend);
				this.updateFriendshipStatus(userIdx, friendIdx);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads messages into memory
	public void loadMessagesData() {
		String sql = "SELECT * FROM messages";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String user = rs.getString("user");
				String sender = rs.getString("sender");
				String message = rs.getString("message");
				int read = rs.getInt("read");
				int userIdx = this.findUser(user);
				this.users.get(userIdx).addMessage(sender, message);
				if (read == 1) {
					this.users.get(userIdx).markLastMessageAsRead();
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads Communities into memory
	public void loadCommunitiesData() {
		this.communities= new ArrayList<Community>();
		
		String sql = "SELECT * FROM Community";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String owner = rs.getString("owner");
				String name = rs.getString("name");
				String description = rs.getString("description");
				int userIdx = this.findUser(owner);
				Community community = new Community(owner, name, description);
				this.users.get(userIdx).addCommunity(community);
				this.communities.add(community);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads community messages and add members
	public void loadCommunityMessages() {
		String sql = "SELECT id, count(id) FROM CommunityMessageJoin GROUP BY id ORDER BY count(id)";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				int count = rs.getInt("count(id)");
				if (count > 0) {
					sql = "SELECT member, community, read FROM CommunityMessageJoin WHERE id = ?";
					try (ResultSet rs1 = this.database.executeQuery(sql, id)) {
						int communityIdx = -1;
						ArrayList<Integer> usersIdx = new ArrayList<Integer>();
						ArrayList<Integer> states = new ArrayList<Integer>();
						while (rs1.next()) {
							int userIdx = this.findUser(rs1.getString("member"));
							communityIdx = this.findCommunity(rs1.getString("community"));
							this.users.get(userIdx).addCommunity(this.communities.get(communityIdx));
							usersIdx.add(userIdx);
							states.add(rs1.getInt("read"));
						}
						sql = "SELECT * FROM CommunityMessages WHERE id = ?";
						try (ResultSet rs2 = this.database.executeQuery(sql, id)) {
							while (rs2.next()) {
								this.communities.get(communityIdx).addMessage(rs2.getString("sender"), rs2.getString("message"));
								for (int i = 0; i < usersIdx.size(); i++) {
									if (states.get(i) == 1) {
										this.users.get(usersIdx.get(i)).getCommunityMessage();
									}
								}
							}
						} catch (SQLException e) {
							System.out.println(e.getMessage());
						}
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads community memberships data into memory
	public void loadCommuntyMembers() {
		String sql = "SELECT * FROM Membership";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String name = rs.getString("community");
				String member = rs.getString("member");
				int userIdx = this.findUser(member);
				int communityIdx = this.findCommunity(name);
				this.users.get(userIdx).addCommunity(this.communities.get(communityIdx));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads following/follower data into memory
	public void loadFollowersData() {
		String sql = "SELECT * FROM Follower";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String user = rs.getString("user");
				String follower = rs.getString("follower");
				int userIdx = this.findUser(user);
				int followerIdx = this.findUser(follower);
				this.users.get(followerIdx).follow(this.users.get(userIdx));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads crush data into memory
	public void loadCrushData() {
		String sql = "SELECT * FROM Crush";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String user = rs.getString("user");
				String crush = rs.getString("crush");
				int userIdx = this.findUser(user);
				int crushIdx = this.findUser(crush);
				this.users.get(userIdx).addCrush(this.users.get(crushIdx));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Loads enemy data into memory
	public void loadEnemyData() {
		String sql = "SELECT * FROM Enemy";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			while (rs.next()) {
				String user = rs.getString("user");
				String enemy = rs.getString("enemy");
				int userIdx = this.findUser(user);
				int enemyIdx = this.findUser(enemy);
				this.users.get(userIdx).addEnemy(this.users.get(enemyIdx));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Clears the data of a table in the database
	public void clearTable(String table) {
		String sql = "DELETE FROM " + table;
		try {
			this.database.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Clear the all tables
	public void zerarSistema() {
		this.users.clear();
		this.communities.clear();
		this.clearTable("Users");	
		this.clearTable("friendship");	
		this.clearTable("messages");
		this.clearTable("Community");
		this.clearTable("Membership");
		this.clearTable("CommunityMessages");
		this.clearTable("CommunityMessageStatus");		
		this.clearTable("Follower");		
		this.clearTable("Crush");		
		this.clearTable("Enemy");		
	}

	// #############
	// # Main Part #
	// #############

	// Looks into the users list to find the user index
	public int findUser(String login) {
		for (int i = 0; i < this.users.size(); i++) {
			User user = this.users.get(i);
			if (user != null) {
				if (user.getLogin().equals(login)) {
					return i;
				}
			}
		}
		return -1; // User not found
	}

	// Verify session with the user id
	public int verifySession (String id) throws UserNotFoundException {
		if (!id.equals("")) {
			int userIdx = Integer.parseInt(id);
			if (this.users.get(userIdx) != null) {
				return userIdx;
			}
		}
		throw new UserNotFoundException("Usuário não cadastrado.");
	}

	// Creates a new user
	public void criarUsuario(String login, String senha, String nome) throws UserCreationException {
		if (login == null) {
			throw new UserCreationException("Login inválido.");
		} else if (senha == null) {
			throw new UserCreationException("Senha inválida.");
		}
		String sql = "INSERT INTO Users(login, senha, nome) VALUES(?,?,?)";
		try {
			this.database.executeUpdate(sql, login, senha, nome);
			User newUser = new User(login, senha, nome);
			this.users.add(newUser);
		} catch (SQLException e) {
			throw new UserCreationException("Conta com esse nome já existe.");
		}
	}

	// Gets an atribute value from a selected user
	public String getAtributoUsuario(String login, String atributo) throws EmptyAttributeException, UserNotFoundException {
		int userIdx = this.findUser(login);
		if (userIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");
		}
		// For avoiding a gigantinc if, else if block the attribute will be fetched directly from the database
		String sql = "SELECT " + atributo + " FROM Users WHERE login = ?";
		try (ResultSet rs = this.database.executeQuery(sql, login)) {
			if (rs.next()) {
				if (rs.getString(atributo) != null) {
					return rs.getString(atributo);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		throw new EmptyAttributeException("Atributo não preenchido.");
	}

	// Starts a session and returns the user id
	public String abrirSessao(String login, String senha) throws LoginFailedException {	
		int userIdx = this.findUser(login);
		if (userIdx != -1) {
			User user = this.users.get(userIdx);
			if (user.getSenha().equals(senha)) {
				return Integer.toString(userIdx);
			}
		}
		throw new LoginFailedException("Login ou senha inválidos.");
	}

	// Edits a logged user's attribute
	public void editarPerfil (String id, String atributo, String valor) throws UserNotFoundException {
		int userIdx = this.verifySession(id);
		User user = this.users.get(userIdx);
		String sql = "UPDATE Users SET " + atributo + " = ? WHERE login = ?";
		try {
			this.database.executeUpdate(sql, valor, user.getLogin());
			user.setAttribute(atributo, valor);
			this.users.set(userIdx, user);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}	
	}

	// Checks if two users are friends
	public boolean ehAmigo(String login, String amigo) {
		int userIdx = this.findUser(login);
		User user = this.users.get(userIdx);
		if (user.getFriendshipStatus(amigo) == 1) {
			return true;
		}
		return false;
	}

	// Adds a new friend to the user and updates the frisndship status
	public void adicionarAmigo(String id, String amigo) throws FriendRequestException, UserNotFoundException, EnemyException {
		int friendIdx = this.findUser(amigo);
		int userIdx = this.verifySession(id); // Added
		if (friendIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");
		}
		User user = this.users.get(userIdx);
		if (userIdx == friendIdx) { // User tryng the befriend himself
			throw new FriendRequestException("Usuário não pode adicionar a si mesmo como amigo.");
		} else if (ehAmigo(user.getLogin(), amigo)) { // User and friend are already friends in the database
			throw new FriendRequestException("Usuário já está adicionado como amigo.");
		} else if (user.getFriendshipStatus(amigo) == 0) { // Pending request
			throw new FriendRequestException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
		} else if (this.users.get(friendIdx).isEnemy(user)) {
			throw new EnemyException("Função inválida: " + this.users.get(friendIdx).getNome() + " é seu inimigo.");
		} else {
			String sql = "INSERT INTO friendship(user,friend) VALUES(?, ?)";
			try {
				this.database.executeUpdate(sql, user.getLogin(), amigo);
				this.updateFriendshipStatus(userIdx, friendIdx);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// Adds and update friendship status of two users
	public void updateFriendshipStatus(int userIdx, int friendIdx) {
		User friend = this.users.get(friendIdx);
		User user = this.users.get(userIdx);
		user.updateFriendshipStatus(friend);
		this.users.set(friendIdx, friend);
		this.users.set(userIdx, user);
	}

	// Returns list of friends
	public String getAmigos (String login) {
		int userIdx = this.findUser(login);
		return this.users.get(userIdx).getFriendsList();
	}

	// Sends a message to a user
	public void enviarRecado(String id, String destinatario, String recado) throws SendReadMessageException, UserNotFoundException, EnemyException {
		int recipientIdx = this.findUser(destinatario);
		int userIdx = this.verifySession(id); // added
 		if (recipientIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");
		}
		User user = this.users.get(userIdx);
		if (userIdx == recipientIdx) {
			throw new SendReadMessageException("Usuário não pode enviar recado para si mesmo.");
		} else if (this.users.get(recipientIdx).isEnemy(this.users.get(userIdx))) {
			throw new EnemyException("Função inválida: " + this.users.get(recipientIdx).getNome() + " é seu inimigo.");	
		}
		String sql = "INSERT INTO messages(user, sender, message, read) VALUES(?, ?, ?, 0)";
		try {
			this.database.executeUpdate(sql, destinatario, user.getLogin(), recado);
			this.users.get(recipientIdx).addMessage(user.getLogin(), recado);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Reads first unread message
	public String lerRecado(String id) throws SendReadMessageException, UserNotFoundException {
		int userIdx = this.verifySession(id);
		User user = this.users.get(userIdx);
		UserMessage message = user.getMessage();
		if (message != null) {	
			String sql = "UPDATE messages SET read = 1 WHERE user = ? AND sender = ? AND message = ?";
			try {
				this.database.executeUpdate(sql, user.getLogin(), message.getSender(), message.getContent());
				return message.getContent();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		throw new SendReadMessageException("Não há recados.");
	}

	// Search for a community
	public int findCommunity(String nome) {
		for (int i = 0; i < this.communities.size(); i++) {
			if (nome.equals(this.communities.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}

	// Trow exception if commity is not found
	public int checkCommunityExists(String nome) throws CommunityNotFoundException {
		int idx = this.findCommunity(nome); 
		if (idx == -1) {
			throw new CommunityNotFoundException("Comunidade não existe.");			
		}
		return idx;
	}

	// Create a community
	public void criarComunidade(String sessao, String nome, String descricao) throws CommunityCreationException, UserNotFoundException {
		// Check if a community with the same name already exists
		if (this.findCommunity(nome) != -1) {
			throw new CommunityCreationException("Comunidade com esse nome já existe.");
		}
		String sql = "INSERT INTO Community(owner, name, description) VALUES(?, ?, ?)";
		try {
			int userIdx = this.verifySession(sessao);
			User user = this.users.get(userIdx);
			this.database.executeUpdate(sql, user.getLogin(), nome, descricao);
			Community community = new Community(user.getLogin(), nome, descricao);
			this.users.get(userIdx).addCommunity(community);
			this.communities.add(community);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Gets a community description
	public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
		int communityIdx = this.checkCommunityExists(nome); 
		return this.communities.get(communityIdx).getDescription();
	}

	// Gets the comunity owner
	public String getDonoComunidade(String nome) throws CommunityNotFoundException {
		int communityIdx = this.checkCommunityExists(nome);
		return this.communities.get(communityIdx).getOwner();
	}

	// Gets the list of members of a community
	public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
		int communityIdx = this.checkCommunityExists(nome);
		return this.communities.get(communityIdx).getMembers();
	}

	// Return list of communities
	public String getComunidades(String login) throws UserNotFoundException {
		int userIdx = this.findUser(login);
		if (userIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado."); 
		}
		User user = this.users.get(userIdx);
		return user.getCommunitiesList();
	}

	// Subscribe user to a community
	public void adicionarComunidade(String sessao, String nome) throws CommunitySubscriptionException, CommunityNotFoundException, UserNotFoundException {
		int communityIdx = this.checkCommunityExists(nome);
		int userIdx = this.verifySession(sessao);
		User user = this.users.get(userIdx);
		if (!this.communities.get(communityIdx).isMember(user.getLogin())) {
			String sql = "INSERT INTO Membership(community, member) VALUES(?, ?)";
			try {
				this.database.executeUpdate(sql, nome, user.getLogin());
				this.users.get(userIdx).addCommunity(this.communities.get(communityIdx));
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} else {
		throw new CommunitySubscriptionException("Usuario já faz parte dessa comunidade.");
		}
	}

	// Reads a community message
	public String lerMensagem(String id) throws UserNotFoundException, SendReadMessageException {
		int userIdx = this.verifySession(id);
		CommunityMessage message = this.users.get(userIdx).getCommunityMessage();
		if (message == null) {
			throw new SendReadMessageException("Não há mensagens.");
		}
		// Get message id from database
		int messageId = -1;
		String sql = "SELECT id FROM CommunityMessageJoin WHERE message = ? AND member = ? AND read = 0";
		try (ResultSet rs = this.database.executeQuery(sql, message.getContent(), this.users.get(userIdx).getLogin())) {
			if (rs.next()) {
				messageId = rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Update status to the database
		if (messageId != -1) {
			sql = "UPDATE CommunityMessageStatus SET read = 1 WHERE id = ?";
			try {
				this.database.executeUpdate(sql, messageId);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return message.getContent();
	}
	
	// Send a message to a community
	public void enviarMensagem(String id, String comunidade, String mensagem) throws CommunityNotFoundException, UserNotFoundException {
		int communityIdx = this.checkCommunityExists(comunidade);
		int userIdx = this.verifySession(id);
		User user = this.users.get(userIdx);
		
		// Save message to the CommunityMessages table
		String sql = "INSERT INTO CommunityMessages(community, sender, message) VALUES(?, ?, ?)";
		try {
			this.database.executeUpdate(sql, comunidade, user.getLogin(), mensagem);
			this.communities.get(communityIdx).addMessage(user.getLogin(), mensagem);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		// Get last message id
		int messageId = -1;
		sql = "SELECT id FROM CommunityMessages ORDER BY id DESC LIMIT 1";
		try (ResultSet rs = this.database.executeQuery(sql)) {
			if (rs.next()) {
				messageId = rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Set read status for all the member for this message
		if (messageId != -1) {
			ListIterator<String> members = this.communities.get(communityIdx).getMembersIterator();
			sql = "INSERT INTO CommunityMessageStatus(id, member, read) VALUES(?, ?, 0)";
			while (members.hasNext()) {
				try {
					String member = members.next();
					this.database.executeUpdate(sql, messageId, member);
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	// Check if user "login" follows "idolo"
	public boolean ehFa(String login, String idolo) throws UserNotFoundException {
		int userIdx = this.findUser(login);
		int idolIdx = this.findUser(idolo);
		if (userIdx == -1 || idolIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");			
		}
		User user = this.users.get(userIdx);
		User idol = this.users.get(idolIdx);
		return user.isFollower(idol);
	}

	// Logged user follows idolo and add follower to idolo
	public void adicionarIdolo(String id, String idolo) throws UserNotFoundException, UserFollowException, EnemyException {
		int idolIdx = this.findUser(idolo);
		int userIdx = this.verifySession(id);
		if (idolIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");			
		} else if (userIdx == idolIdx) {
			throw new UserFollowException("Usuário não pode ser fã de si mesmo.");
		} else if (this.users.get(idolIdx).isEnemy(this.users.get(userIdx))) {
			throw new EnemyException("Função inválida: " + this.users.get(idolIdx).getNome() + " é seu inimigo.");	
		} else if (this.users.get(userIdx).isFollower(this.users.get(idolIdx))) {
			throw new UserFollowException("Usuário já está adicionado como ídolo.");	
		}
		String sql = "INSERT INTO Follower(user, follower) VALUES(?, ?)";
		try {
			this.database.executeUpdate(sql, idolo, this.users.get(userIdx).getLogin());
			this.users.get(userIdx).follow(this.users.get(idolIdx));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Gets the string of users that "login" follows
	public String getFas(String login) throws UserNotFoundException {
		int userIdx = this.findUser(login);
		if (userIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");			
		}
		User user = this.users.get(userIdx);
		return user.getFollowers();	
	}

	// Gets if a user is crush of another
	public boolean ehPaquera(String id, String paquera) throws UserNotFoundException {
		int crushIdx = this.findUser(paquera);
		int userIdx = this.verifySession(id);
		if (crushIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");

		}
		return this.users.get(userIdx).isCrush(this.users.get(crushIdx));
	}

	// Adds crush to the user
	public void adicionarPaquera(String id, String paquera) throws UserNotFoundException, UserFollowException, EnemyException {
		int crushIdx = this.findUser(paquera);
		int userIdx = this.verifySession(id);
		if (crushIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");
		} else if (crushIdx == userIdx) {
			throw new UserFollowException("Usuário não pode ser paquera de si mesmo.");
		} else if (this.users.get(crushIdx).isEnemy(this.users.get(userIdx))) {
			throw new EnemyException("Função inválida: " + this.users.get(crushIdx).getNome() + " é seu inimigo.");	
		} else if (this.users.get(userIdx).isCrush(this.users.get(crushIdx))) {
			throw new UserFollowException("Usuário já está adicionado como paquera.");
		}

		boolean sendMessage = false;
		String sql = "INSERT INTO Crush(user, crush) VALUES(?, ?)";
		try {
			this.database.executeUpdate(sql, this.users.get(userIdx).getLogin(), paquera);
			sendMessage = this.users.get(userIdx).addCrush(this.users.get(crushIdx));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Send message to both users
		if (sendMessage) {
			String message = this.users.get(crushIdx).getNome() + " é seu paquera - Recado do Jackut.";
			sql = "INSERT INTO messages(user, sender, message, read) VALUES(?, ?, ?, 0)";
			try {
				this.database.executeUpdate(sql, this.users.get(userIdx).getLogin(), "System", message);
				this.users.get(userIdx).addMessage("System", message);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			message = this.users.get(userIdx).getNome() + " é seu paquera - Recado do Jackut.";
			sql = "INSERT INTO messages(user, sender, message, read) VALUES(?, ?, ?, 0)";
			try {
				this.database.executeUpdate(sql, this.users.get(crushIdx).getLogin(), "System", message);
				this.users.get(crushIdx).addMessage("System", message);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// Gets lists of users crushes
	public String getPaqueras(String id) throws UserNotFoundException {
		int userIdx = this.verifySession(id);
		return this.users.get(userIdx).getCrushes();
	}

	// Add enemey to the logged user
	public void adicionarInimigo(String id, String inimigo) throws UserNotFoundException, EnemyException {
		int enemyIdx = this.findUser(inimigo);
		int userIdx = this.verifySession(id);
		if (enemyIdx == -1) {
			throw new UserNotFoundException("Usuário não cadastrado.");
		} else if (userIdx == enemyIdx) {
			throw new EnemyException("Usuário não pode ser inimigo de si mesmo.");	
		} else if (this.users.get(userIdx).isEnemy(this.users.get(enemyIdx))) {
			throw new EnemyException("Usuário já está adicionado como inimigo.");
		}
		String sql = "INSERT INTO Enemy(user, enemy) VALUES(?, ?)";
		try {
			this.database.executeUpdate(sql, this.users.get(userIdx).getLogin(), inimigo);
			this.users.get(userIdx).addEnemy(this.users.get(enemyIdx));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Removes a user and all that is related to him
	public void removerUsuario(String id) throws UserNotFoundException {
		int userIdx = this.verifySession(id);
		String userLogin = this.users.get(userIdx).getLogin();
		// Delete friendship
		String sql = "DELETE FROM friendship WHERE user = ? OR friend = ?";
		try {
			this.database.executeUpdate(sql, userLogin, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Delete messages
		sql = "DELETE FROM messages WHERE user = ? OR sender = ?";
		try {
			this.database.executeUpdate(sql, userLogin, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Delete user community messages and memberships
		ListIterator<Community> communities =  this.users.get(userIdx).getCommunitiesIterator();
		while (communities.hasNext()) {
			Community community = communities.next();
			sql = "DELETE FROM CommunityMessages WHERE community = ?";
			try {
				this.database.executeUpdate(sql, community.getName());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			// Membership
			sql = "DELETE FROM Membership WHERE community = ? OR member = ?";
			try {
				this.database.executeUpdate(sql, community.getName(), userLogin);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		// Delete all community messages statuses.
		sql = "DELETE FROM CommunityMessageStatus WHERE member = ? OR id NOT IN (SELECT id FROM CommunityMessages)";
		try {
			this.database.executeUpdate(sql, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Delete all communities
		sql = "DELETE FROM Community WHERE owner = ?";
		try {
			this.database.executeUpdate(sql, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Delete all Following/followers
		sql = "DELETE FROM Follower WHERE user = ? OR follower = ?";
		try {
			this.database.executeUpdate(sql, userLogin, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Delete all crush
		sql = "DELETE FROM Crush WHERE user = ? OR crush = ?";
		try {
			this.database.executeUpdate(sql, userLogin, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Delete all Following/followers
		sql = "DELETE FROM Enemy WHERE user = ? OR enemy = ?";
		try {
			this.database.executeUpdate(sql, userLogin, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		this.loadData();
		// Delete user
		sql = "DELETE FROM Users WHERE login = ?";
		try {
			this.database.executeUpdate(sql, userLogin);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		this.users.set(userIdx, null);
	}

	// Close system
	public void encerrarSistema() {
		System.out.println("System closed");
	}
}
