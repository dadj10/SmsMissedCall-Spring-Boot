package com.smsmissedcall.schedulers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.smsmissedcall.config.FormatNumero;
import com.smsmissedcall.config.Utils;
import com.smsmissedcall.entities.BulkSms;
import com.smsmissedcall.entities.Message;
import com.smsmissedcall.entities.ModeleSms;
import com.smsmissedcall.entities.ParametreApi;
import com.smsmissedcall.entities.ParametreSqlServer;
import com.smsmissedcall.entities.ResponseFormatNumber;
import com.smsmissedcall.entities.Ticket;
import com.smsmissedcall.repository.ModeleSmsRepository;
import com.smsmissedcall.repository.ParametreApiRepository;
import com.smsmissedcall.repository.ParametreSqlServerRepository;
import com.smsmissedcall.repository.TicketRepository;

@Component
public class ReadSmsMissedCallScheduler {

	// Inversion de control
	@Autowired
	private TicketRepository ticketRepos;

	@Autowired
	private ParametreSqlServerRepository paramsSqlServerRepos;

	@Autowired
	private ModeleSmsRepository modeleSmsRepos;

	@Autowired
	private ParametreApiRepository parametreApiRepos;

	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	/*
	 * La fonction ReadSqlServerJDBC a pour role de recuper les tickets d'appels
	 * manqués dans la DB SQL Server smsmissedcall via une connexion JDBC et de les
	 * enregistrer la table call_missed de la database Postgres du moniteur. Avant
	 * d'enregistrer les tickets dans postgres on s'assure que les tickets recupérés
	 * ont un sender sur la plateforme pour l'envoi
	 */
	@Scheduled(fixedDelay = 10000)
	public void ReadSqlServerJDBC() {

		// Je ferme les connexions.
		close();

		// Je déclare les variables locals.
		List<Object[]> groupeEnvoi = null;

		System.out.println(Utils.dateNow() + " Run ReadSqlServerJDBC...");

		// Je récupère les parametres du SGBD SQL Server.
		ParametreSqlServer sqlServer = null;
		sqlServer = paramsSqlServerRepos.findParamSqlServer();

		// Si le params sql server n'est pas null alors...
		if (sqlServer != null) {

			// Je formate l'URL de la connexion JDBC.
			// jdbc:sqlserver://[HOST]\\[INSTANCE]:[PORT];databaseName=[DBNAME]
			String url = Utils.formatUrl(sqlServer);

			// Je recupère le driver.
			String driver = sqlServer.getDriver();

			// Je récupère le username & le password de connexion a SQL Server.
			String username = sqlServer.getUsername();
			String password = sqlServer.getPassword();

			// Je récupère le délai entre 2 appels du meme poste.
			int delai = Integer.parseInt(sqlServer.getDelai());

			try {
				// Je charge le driver de la connexion JDBC en créant une instance.
				Class.forName(driver).newInstance();

				// Je me connecte au SGBD.
				connection = DriverManager.getConnection(url, username, password);

				/*
				 * Je prépare 'statement' l'instance permettant d'excuteur des requêtes SQL vers
				 * la base de données sql server smsmissedcall.
				 */
				statement = connection.createStatement();

				// Je prépare le script de la requète puis je l'exécute avec statement.
				String selectQuery = "SELECT dbo.CONTACTS.nom AS nom, dbo.CONTACTS.CODE_ENTREPRISE AS code_entreprise, dbo.CONTACTS.sender AS sender, dbo.CONTACTS.linedirect AS ligne_direct, dbo.CallMissed.codes AS code_ticket, dbo.CallMissed.Dateheuredecroche AS date_heure_decroche, dbo.CallMissed.dest1 AS destinataire, dbo.CallMissed.duree AS duree, dbo.CallMissed.traite AS traite, dbo.CallMissed.profil AS profil, dbo.CallMissed.dateheuralerte AS date_heure_alerte, dbo.CONTACTS.poste AS poste, dbo.CONTACTS.standart AS standard FROM dbo.CONTACTS INNER JOIN dbo.CallMissed ON (dbo.CONTACTS.poste = dbo.CallMissed.Postes) WHERE dbo.CallMissed.traite = 0";
				resultSet = statement.executeQuery(selectQuery);

				if (!resultSet.next()) { // if resultSet.next() retourne false.
					System.out.println(Utils.dateNow() + " Aucun ticket trouvé");
				} else {
					do {
						/*
						 * Je format le numero du destinataire ayant reçu l'appel (donnée récupérée du
						 * champ 'destinataire')
						 */
						ResponseFormatNumber rfn = FormatNumero.number_F(resultSet.getString("destinataire"));

						// Je récupère le code du ticket.
						String code_ticket = resultSet.getString("code_ticket");

						// Si le numero est correct alors...
						if (rfn.isResponse()) {

							// Je recupère le sender du ticket récupéré.
							String sender = resultSet.getString("sender").toLowerCase();

							/*
							 * Je vérifique que le sender existe dans la base de données du moniteur et est
							 * affecté a un groupe d'envoi (Actif) ayant un utilisateur (Actif) de role
							 * SmsMissedCall (si le sender est affilié a un groupe d'envoi ayant un
							 * utilisateur de role SmsMissedCall dans la base de données).
							 * ------------OBJECTIF VISE : Mapper l'identifiant du groupe d'envoi ayant un
							 * utilisateur de role SmsMissedCall au ticket.
							 */
							groupeEnvoi = ticketRepos.findIdGroupeEnvoieSmsMissedCallBySender(sender);

							// Si un groupe d'envoi correspond au sender alors...
							if (groupeEnvoi.size() != 0) {

								// Je récupère le numero du destinataire bien formaté.
								String destinataire = rfn.getNumero();

								// Je parcours le résultat de l'exécution pour recupérer les autres données.
								String nom = resultSet.getString("nom");
								String code_entreprise = resultSet.getString("code_entreprise");

								String ligne_direct = resultSet.getString("ligne_direct");
								String date_heure_decroche = resultSet.getString("date_heure_decroche");

								String duree = resultSet.getString("duree");
								// int traite = resultSet.getByte("traite");

								String profil = resultSet.getString("profil");
								String date_heure_alerte = resultSet.getString("date_heure_alerte");

								String poste = resultSet.getString("poste");
								String standard = resultSet.getString("standard");

								// Je verifie que le code n'est pas enregistré.
								Ticket ticket = null;
								ticket = ticketRepos.findCallMissedByCode(code_ticket);

								// Si le code du ticket n'est pas enregistrer alors...
								if (ticket == null) {

									// Je recherche le dernier appel recu par le destinataire.
									boolean reponseEcart = findEcartDernierTicket(poste, destinataire,
											date_heure_alerte, delai);

									/*
									 * Si l'ecart en seconde entre deux appels manqués emit par le mème poste est
									 * respecté (ecart > delai) alors...
									 */
									if (reponseEcart) {

										// J'enregistre le ticket call missed (appel manqué)
										Ticket t = new Ticket();

										t.setNom(nom);
										t.setCode_entreprise(code_entreprise);

										// Je recupère l'identifiant du groupe d'envoi en position 0 de objects
										Long idGroupeEnvoi = null;
										for (Object[] objects : groupeEnvoi) {
											idGroupeEnvoi = Long.parseLong(objects[0].toString());
											System.out.println(objects[0]);
										}
										t.setIdGroupeEnvoi(idGroupeEnvoi); // Groupe d'envoi du ticket.

										t.setLigne_direct(ligne_direct);
										t.setCode_ticket(code_ticket);
										t.setDate_heure_decroche(date_heure_decroche);
										t.setDestinataire(destinataire);
										t.setDuree(duree);

										t.setTraite(1);

										t.setProfil(profil);
										t.setDate_heure_alerte(date_heure_alerte);
										t.setPoste(poste);
										t.setStandard(standard);

										t.setEtat(0);

										t.setDateInsertion(new Date());
										t.setDateModification(new Date());

										t = ticketRepos.save(t);

										// Si l'object call n'est pas null alors...
										if (t != null) {
											// Je mets à jour le ticket call missed existant de Sql Server.
											int statusTraite = 1;
											updateTicket(connection, statusTraite, code_ticket);
										}
									} else {
										/*
										 * Je mets à jour le ticket call missed existant de Sql Server ne depassant pas
										 * de delai.
										 */
										int statusTraite = -1;
										updateTicket(connection, statusTraite, code_ticket);
									}
								}
							}
						} else {
							/*
							 * Je mets a jour le ticket call missed existant de Sql Server pour numero
							 * incorrect.
							 */
							int statusTraite = -1;
							updateTicket(connection, statusTraite, code_ticket);
						}
					} while (resultSet.next());
				}

				// Je ferme les connexions.
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Cette tache sendBulkSms permet d'envoyer les SMS via l'api par la method */
	@Scheduled(fixedDelay = 5000)
	public void SendBulkSms() {

		System.out.println(Utils.dateNow() + " Run SendBulkSms...");

		// Je déclare les variables locals.
		List<Object[]> applications = null;
		ParametreApi api = null;
		List<Ticket> tickets = null;
		ModeleSms modeleSms = null;
		Long idGroupeEnvoi = null;

		/*
		 * Je recupère la liste des appilications ayant le role exeptionnel
		 * SmsMissedCall crée spécialement ansi que leur groupe d'envoi.
		 */
		applications = ticketRepos.findAllSmsMissedCallApplicationAndGroupeEnvoi();

		// Si la liste des applications n'est pas vide alors...
		if (applications != null) {

			// Je recupère le params Api.
			api = parametreApiRepos.findOneParametreApi();

			// Je recupere les modeles de sms.
			modeleSms = modeleSmsRepos.findModelSms();

			// Si le parametre API et le modele de SMS ne sont pas null alors...
			if (api != null && modeleSms != null) {

				String url = api.getUrl();
				String flash = api.getFlash();

				String username = null;
				String token = null;
				String sender = null;
				String title = null;

				for (Object[] application : applications) {

					username = application[0].toString();
					token = application[1].toString();
					sender = application[2].toString();
					title = application[3].toString();

					idGroupeEnvoi = Long.parseLong(application[4].toString());

					// Je recupère les tickets du jour appartenant au groupe d'envois de
					// l'application.
					tickets = ticketRepos.findAllTicketOfTodayByIdGroupEnvoi(idGroupeEnvoi);

					if (tickets.size() != 0) {

						// Je crée une instance de ArrayList<Message>.
						ArrayList<Message> messageString = new ArrayList<Message>();

						for (Ticket ticket : tickets) {

							// Je crée une instance de Message().
							Message message = new Message();

							// Je charge les attributs de l'objet message.
							message.setDest(ticket.getDestinataire());
							message.setSms(fondModeleSms(ticket, modeleSms));
							message.setFlash(flash);
							message.setSender(sender);

							messageString.add(message);
						}

						if (messageString != null) {

							// Je crée une instance de BulkSms().
							BulkSms bulkSms = new BulkSms();

							// J'attribut le username, le token et le titre de l'objet BulkSms().
							bulkSms.setUsername(username);
							bulkSms.setToken(token);
							bulkSms.setTitle(title);

							// Je charge Mssg de l'objet BulkSms.
							bulkSms.setMssg(messageString);

							// Je crée une instance de Gson().
							Gson gson = new Gson();

							// Je crée une instance de HttpHeaders().
							HttpHeaders headers = new HttpHeaders();

							// Je définis le header (en-tête) pour retouner du JSON.
							headers.setContentType(MediaType.APPLICATION_JSON);

							// Je crée une instance de RestTemplate().
							RestTemplate restTemplate = new RestTemplate();

							// J'encode bulkSms en JSON.
							HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(bulkSms), headers);

							// J'envoi le sms au destinataire finaux.
							ResponseEntity<String> response = restTemplate.postForEntity(url + "/addBulkSms", entity,
									String.class);
							System.err.println(
									Utils.dateNow() + " SendBulkSms CodeReponse : " + response.getStatusCodeValue());

							// Je vérifie le code de la réponse retrournée.
							if (response.getStatusCodeValue() == 200) {
								// Je mets a jour les tickets (Etat = 1) notifiés.
								updateBulkSms(tickets);
							} else {
								System.out.println(Utils.dateNow() + " Erreur lors de l'envoi des SMS.");
							}
						}
					}
				}
			}
		}
	}

	/*
	 * Cette fonction permet determiner si l'ecart en minute entre deux appels
	 * manqué émit pas un meme poste vers un meme destinataire est respecté (meme
	 * poste, meme destinataire).
	 */
	public boolean findEcartDernierTicket(String poste, String destinataire, String date_heure_alerte, int delai) {

		boolean reponse = false;

		// Je recherche le dernier ticket enregistré le poste et le destinataire.
		Ticket ticket = null;
		ticket = ticketRepos.findLastCall(poste, destinataire);

		// S'il existe un ticket du meme poste et du meme destinataire alors...
		if (ticket != null) {

			// Je compare les dates d'alerte.
			Date date1 = Utils.stringToDate(ticket.getDate_heure_alerte()); // date de l'ancien ticket.
			Date date2 = Utils.stringToDate(date_heure_alerte); // date du nouveau ticket.

			// Je calcule le nombre de minutes entre des deux dates.
			Long diff = date2.getTime() - date1.getTime();
			int ecart = (int) (diff / (60 * 1000)); // en minutes.

			// Si le delai est superieur a la difference alors j'enregistre le ticket.
			if (ecart > delai) {
				reponse = true;
			} else { // si non je n'enregistre pas.
				reponse = false;
				System.out.println(Utils.dateNow() + " Un ticket d'appel vers ce destinataire a déja été enregistré");
			}
		} else {
			reponse = true;
			System.out.println(Utils.dateNow() + " Il n'existe pas un ticket du meme poste et du meme destinataire");
		}

		return reponse;
	}

	/*
	 * La fonction updateTicket() a pour role de mettre à jour (Etat à 1) les
	 * tickets de la table CallMissed de la base donnes smsmissedcall de Sql Server
	 */
	public boolean updateTicket(Connection connection, int statusTraite, String code_ticket) {

		boolean reponse = false;

		try {
			// Je prépare la requète de mise à jour.
			String updateQuery = "UPDATE CallMissed SET traite=? WHERE codes=?";

			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setInt(1, statusTraite);
			preparedStatement.setString(2, code_ticket);

			// J'exécute la requète de mise à jour.
			int rowsUpdated = preparedStatement.executeUpdate();
			if (rowsUpdated > 0) {
				reponse = true;
				System.out.println(Utils.dateNow() + " Un CallMissed code ticket " + code_ticket
						+ " existant a été mis à jour avec succès !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reponse;
	}

	/* La fonction close() permet de fermer toutes les instances. */
	protected void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (preparedStatement != null && !preparedStatement.isClosed()) {
				preparedStatement.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * La fonction fondModeleSms() permet de formater le SMS à envoyer en s'appuyant
	 * sur les modèle de SMS adapatés a chaque type (profil) du ticket missed call.
	 */
	public String fondModeleSms(Ticket ticket, ModeleSms modeleSms) {

		String sms = null;
		// Je récupère le profil du ticket.
		String profil = ticket.getProfil().toLowerCase();

		// De l'intérieur vers l'extérieur (outgoing).
		if (profil.contains("outgoing")) {
			String modele = modeleSms.getSms_appel_sortant_ld();
			modele = modele.replace("[ENTREPRISE]", ticket.getCode_entreprise());

			// Je verifie que le la ligne direct n'est pas null.
			if (ticket.getLigne_direct() != null) {
				sms = modele.replace("[LIGNEDIRECTE]", ticket.getLigne_direct());
			} else {
				sms = modele.replace("[LIGNEDIRECTE]", ticket.getStandard());
			}
		}

		return sms;
	}

	/*
	 * La fonction updateBulkSms() permet de mettre à jour (etat à 1) les tickets
	 * dont le sms a été envoyé.
	 */
	public void updateBulkSms(List<Ticket> tickets) {

		if (tickets != null) {
			for (Ticket t : tickets) {
				t.setEtat(1);
				t.setDateModification(new Date());
				ticketRepos.save(t);
			}
		}
	}

}
