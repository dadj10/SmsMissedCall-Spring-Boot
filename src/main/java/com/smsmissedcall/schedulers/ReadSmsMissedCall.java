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
import com.smsmissedcall.entities.CallMissed;
import com.smsmissedcall.entities.ModeleSms;
import com.smsmissedcall.entities.ParamApi;
import com.smsmissedcall.entities.ParamsSqlServer;
import com.smsmissedcall.repository.CallMissedRepository;
import com.smsmissedcall.repository.ModeleSmsRepository;
import com.smsmissedcall.repository.ParamApiRepository;
import com.smsmissedcall.repository.ParamsSqlServerRepository;
import com.smsmissedcall.sms.BulkSms;
import com.smsmissedcall.sms.Message;
import com.smsmissedcall.utils.FormatNumero;
import com.smsmissedcall.utils.Utils;

@Component
public class ReadSmsMissedCall {

	// Inversion de control
	@Autowired
	private CallMissedRepository callMissedRepos;

	@Autowired
	private ParamsSqlServerRepository paramsSqlServerRepos;

	@Autowired
	private ModeleSmsRepository modeleSmsRepos;

	@Autowired
	private ParamApiRepository paramApiRepos;

	private Connection con = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	/**
	 * Cette tache a pour role de se connecter a la base de données MySql easy via
	 * une connexion JSBC pour récupérer les tickets générés après un appl manqué
	 * 
	 * @param dbname,
	 *            host, port, username, password
	 * @param driver,
	 *            url
	 */
	@Scheduled(fixedDelay = 10000)
	public void ReadSqlServerJDBC() {

		System.err.println(Utils.dateNow() + " Run ReadSqlServerJDBC");

		// 1. J'initialise les params de connexion au SGBD
		String host, instance, dbname, url, driver, username, password = null;

		// 2. Je récupère les parametres du SGBD
		ParamsSqlServer params = null;
		params = paramsSqlServerRepos.findParamSqlServer();

		if (params != null) {

			host = params.getHost();
			instance = params.getInstance();
			int port = params.getPort();
			dbname = params.getDbname();

			url = params.getUrl();

			// 3. Je formate l'URL de la connexion JDBC
			// String url =
			// "jdbc:sqlserver://10.10.130.67\\MSSQLSERVER:1433;databaseName=smsmissedcall";
			// String url =
			// "jdbc:sqlserver://[HOST]\\[INSTANCE]:[PORT];databaseName=[DBNAME]";
			url = url.replace("[HOST]", host);
			url = url.replace("[INSTANCE]", instance);
			url = url.replace("[PORT]", String.valueOf(port));
			url = url.replace("[DBNAME]", dbname);

			// driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			driver = params.getDriver();

			username = params.getUsername();
			password = params.getPassword();
			int delai = Integer.parseInt(params.getDelai());

			try {
				// 4. Je Charge le driver de la connexion JDBC
				Class.forName(driver).newInstance();

				// 5. Je me connecte au SGBD
				con = DriverManager.getConnection(url, username, password);

				// 6. Je prepare 'statement' l'instence permettant d'excuteur des requêtes SQL
				// vers la base de données
				statement = con.createStatement();

				// 7. Je prépara la requète et puis je l'exécute
				String selectQuery = "SELECT dbo.CONTACTS.nom AS nom, dbo.CONTACTS.CODE_ENTREPRISE AS code_entreprise, dbo.CONTACTS.linedirect AS ligne_direct, dbo.CallMissed.codes AS code_ticket, dbo.CallMissed.Dateheuredecroche AS date_heure_decroche, dbo.CallMissed.dest1 AS destinataire, dbo.CallMissed.duree AS duree, dbo.CallMissed.traite AS traite, dbo.CallMissed.profil AS profil, dbo.CallMissed.dateheuralerte AS date_heure_alerte, dbo.CONTACTS.poste AS poste, dbo.CONTACTS.standart AS standard FROM dbo.CONTACTS INNER JOIN dbo.CallMissed ON (dbo.CONTACTS.poste = dbo.CallMissed.Postes) WHERE dbo.CallMissed.traite = 0";

				resultSet = statement.executeQuery(selectQuery);

				// 8. J'initialise les variables de récupération
				String nom, code_entreprise, ligne_direct, code_ticket, date_heure_decroche, destinataire, duree,
						profil, date_heure_alerte, poste, standard;

				if (!resultSet.next()) { // if resultSet.next() retourne false
					System.err.println(Utils.dateNow() + " Aucun enregistrement trouvé");
				} else {
					do {
						// 9. Je parcours le résultat de l'exécution.
						nom = resultSet.getString("nom");
						code_entreprise = resultSet.getString("code_entreprise");
						ligne_direct = resultSet.getString("ligne_direct");
						code_ticket = resultSet.getString("code_ticket");

						date_heure_decroche = resultSet.getString("date_heure_decroche");
						destinataire = FormatNumero.number_F(resultSet.getString("destinataire"));
						duree = resultSet.getString("duree");
						// int traite = resultSet.getByte("traite");

						profil = resultSet.getString("profil");
						date_heure_alerte = resultSet.getString("date_heure_alerte");

						poste = resultSet.getString("poste");
						standard = resultSet.getString("standard");

						/*
						 * System.out.println("nom: " + nom); System.out.println("code_entreprise: " +
						 * code_entreprise); System.out.println("ligne_direct: " + ligne_direct);
						 * System.out.println("date_heure_decroche: " + date_heure_decroche);
						 * System.out.println("destinataire: " + date_heure_decroche);
						 * System.out.println("duree: " + duree); System.out.println("traite: " +
						 * traite); System.out.println("profil: " + profil);
						 * System.out.println("date_heure_alerte: " + date_heure_alerte);
						 * System.out.println("poste: " + poste); System.out.println("standard: " +
						 * standard);
						 */

						// 10. Je verifie que le code n'est pas enregistré
						CallMissed callMissed = null;
						callMissed = callMissedRepos.findCallMissedByCode(code_ticket);

						if (callMissed == null) {

							// 11. Je recherche le dernier appel recu par le destinataire.
							boolean repEcart = findEcartDernierTicket(code_ticket, poste, destinataire,
									date_heure_alerte, delai);
							// ecart > delai
							if (repEcart) {

								// 12. J'enregistre le call missed
								CallMissed call = new CallMissed();

								call.setNom(nom);
								call.setCode_entreprise(code_entreprise);
								call.setLigne_direct(ligne_direct);
								call.setCode_ticket(code_ticket);
								call.setDate_heure_decroche(date_heure_decroche);
								call.setDestinataire(destinataire);
								call.setDuree(duree);

								call.setTraite(1);

								call.setProfil(profil);
								call.setDate_heure_alerte(date_heure_alerte);
								call.setPoste(poste);
								call.setStandard(standard);

								call.setEtat(0);

								call.setDateInsertion(new Date());
								call.setDateModification(new Date());

								call = callMissedRepos.save(call);

								// 13. Si l'object call n'est pas null alors,
								if (call != null) {
									// 13. Je mets a jour le CallMissed existant de Sql Server
									int statusTraite = 1;
									updatedTicket(con, statusTraite, code_ticket);
								}
							} else {
								// Je met a jour le ticket
								int statusTraite = -1;
								updatedTicket(con, statusTraite, code_ticket);
							}
						} else {
							System.err.println(Utils.dateNow() + " CallMissed déja enregistré");
						}

					} while (resultSet.next());
				}

				// 15. Je ferme la connexion
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println(Utils.dateNow() + " Aucun paramètre de server sql trouvé");
		}
	}

	/**
	 * Cette fonction permet determiner l'ecart entre deux appels
	 * 
	 * @param String
	 * @param String
	 * 
	 * @return boolean
	 */
	public boolean findEcartDernierTicket(String code_ticket, String poste, String destinataire,
			String date_heure_alerte, int delai) {

		boolean reponse = false;

		// 1. Je recherche le dernier ticket enregistré le poste et le destinataire.
		CallMissed call = null;
		call = callMissedRepos.findLastCall(poste, destinataire);

		// 2. S'il existe un ticket du meme poste et du meme destinataire alors...
		if (call != null) {

			// 3. Je compare les dates d'alerte
			Date date1 = Utils.stringToDate(call.getDate_heure_alerte()); // date de l'ancien ticket
			Date date2 = Utils.stringToDate(date_heure_alerte); // date du nouveau ticket

			// 4. Je calcule le nombre de minutes entre des deux dates.
			Long diff = date2.getTime() - date1.getTime();
			int ecart = (int) (diff / (60 * 1000)); // en minutes.

			// 5. Si le delai est superieur a la difference alors j'enregistre le ticket.
			if (ecart > delai) {
				reponse = true;
			} else { // si non je n'enregistre pas
				reponse = false;
				System.err.println(Utils.dateNow() + " Un ticket d'appel vers ce destinataire a déja été enregistré");
			}
		} else {
			reponse = true;
			System.err.println(Utils.dateNow() + " Il n'existe pas un ticket du meme poste et du meme destinataire");
		}

		return reponse;
	}

	/**
	 * Cette fonction permet de mettre à jour (Etat à 1) les tickets de la table
	 * CallMissed de la base donnes smsmissedcall de Sql Server
	 * 
	 * @param Connection
	 * @param String
	 * 
	 * @return boolean
	 */
	public boolean updatedTicket(Connection con, int statusTraite, String code_ticket) {

		boolean reponse = false;

		try {
			// 1. Je prépare la requète de mise a jour
			String updateQuery = "UPDATE CallMissed SET traite=? WHERE codes=?";

			preparedStatement = con.prepareStatement(updateQuery);
			preparedStatement.setInt(1, statusTraite);
			preparedStatement.setString(2, code_ticket);

			// 2. J'exécute la requète de mise a jour
			int rowsUpdated = preparedStatement.executeUpdate();
			if (rowsUpdated > 0) {
				reponse = true;
				System.err.println(Utils.dateNow() + " Un CallMissed codes " + code_ticket
						+ " existant a été mis à jour avec succès !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reponse;
	}

	// Cette fonction permet de fermer toutes les instances
	private void close() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
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

	/**
	 * Cette tache sendBulkSms permet d'envoyer les SMS via l'api par la method
	 * BulkSms (Chaque destiantaire reçoit son contenu de SMS)
	 * 
	 * @return void
	 */
	@Scheduled(fixedDelay = 10000)
	public void sendBulkSms() {

		System.err.println(Utils.dateNow() + " Run sendBulkSms");

		// http://10.10.130.76:8080/api/addOneSms?Username=justine&Token=$2a$10$bbqS7kicnAPCFkjbCSPN1OGOhMxavdsMOnRPir7Q39vQeu4msF5y6&Dest=22584046064&Sms=ok&Flash=0&Sender=CIE&Titre=TESTER;

		// 1. Je recherche les paramètres de l'api.
		ParamApi api = null;
		api = paramApiRepos.findOneParamApi();
		System.err.println(api);

		if (api != null) {

			String username, token, sender, flash, title = null;

			// String username = "justine";
			// String token =
			// "$2a$10$bbqS7kicnAPCFkjbCSPN1OGOhMxavdsMOnRPir7Q39vQeu4msF5y6";
			// String sender = "CIE";
			// String flash = "0";
			// String title = TESTER;

			// 2. Je récupère les paramètres de l'api.
			username = api.getUsername();
			token = api.getToken();
			sender = api.getSender();
			flash = api.getFlash();
			title = api.getTitle();

			// String url = "http://10.10.130.76:8080/api";
			String url = api.getUrl();

			// 3. Je crée une instance de Gson().
			Gson gson = new Gson();

			// 4. Je crée une instance de BulkSms().
			BulkSms bulkSms = new BulkSms();

			// 4. Je crée une instance de RestTemplate().
			RestTemplate restTemplate = new RestTemplate();

			// 6. Je crée une instance de HttpHeaders().
			HttpHeaders headers = new HttpHeaders();

			// 7. Je définis le header (en-tête) pour retouner du JSON.
			headers.setContentType(MediaType.APPLICATION_JSON);

			// 8. J'attribut le username et le token de l'objet BulkSms().
			bulkSms.setUsername(username);
			bulkSms.setToken(token);
			bulkSms.setTitle(title);

			// 9. Je crée une instance de ArrayList<Message>.
			ArrayList<Message> messageString = new ArrayList<Message>();

			// 10. Je recupere la liste des tickets.
			List<CallMissed> callMissed = null;
			callMissed = callMissedRepos.findAllTicketForSendSms();

			// 11. Je recherche les modele de SMS.
			ModeleSms modeleSms = null;
			modeleSms = modeleSmsRepos.findModelSms();

			// 12. Je verifie que les tickets (CallMissed) et modele de SMS ne sont pas
			// null.
			System.err.println("callMissed.size(): " + callMissed.size());
			if (callMissed.size() != 0 && modeleSms != null) {

				// 13. Je parcours la lite de tickets.
				for (CallMissed call : callMissed) {

					// 14. Je crée une instance de Message().
					Message message = new Message();

					// 15. J'initialise les variables du message.
					String sms = null;
					String dest = null;

					// 16. Je recheche le modèle de SMS approprié au type de ticket.
					sms = fondModeleSms(call, modeleSms);
					dest = call.getDestinataire();

					// Si le numéro eexiste alors...
					if (dest != null) {

						// 17. Je charge les attribut de l'objet message.
						message.setDest(dest);
						message.setSms(sms);

						message.setFlash(flash);
						message.setSender(sender);

						messageString.add(message);
					}
				}

				System.err.println(Utils.dateNow() + " messageString " + messageString.size());
				if (messageString.size() != 0) {

					// 18. Je charge Mssg de l'objet BulkSms.
					bulkSms.setMssg(messageString);

					// 19. J'encode l'objet BulkSms en JSON.
					HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(bulkSms), headers);

					// 20. J'envoi le sms au destinataire finaux.
					ResponseEntity<String> response = restTemplate.postForEntity(url + "/addBulkSms", entity,
							String.class);
					System.err.println(Utils.dateNow() + " CodeReponse : " + response.getStatusCodeValue());

					// 21. Je vérifie le code de la réponse retrournée.
					if (response.getStatusCodeValue() == 200) {
						// Je mets a jour les tickets nofifiés
						updateBulkSms(callMissed);
						System.err.println(Utils.dateNow() + " Success SMS envoyé avec succès.");
					} else {
						System.err.println(
								Utils.dateNow() + " Erreur lors de l'envoi du SMS, veuillez réessayer à nouveau.");
					}
				}
			} else {
				if (callMissed.size() == 0) {
					System.err.println(Utils.dateNow() + " Aucun ticket d'appels manqué disponible");
				} else {
					System.err.println(Utils.dateNow() + " Aucun modèle de SMS disponible");
				}
			}
		} else {
			System.err.println(Utils.dateNow() + " Aucun paramètre Api disponible");
		}
	}

	/**
	 * Cette fonction retourne de SMS en s'appuyant sur les modele de SMS adapatés a
	 * chaque type (profil) du ticket.
	 * 
	 * @param call
	 * @param modeleSms
	 * @return sms
	 * 
	 */
	public String fondModeleSms(CallMissed call, ModeleSms modeleSms) {

		String sms = null, modele, profil;
		// 1. Je récupère le profil du ticket.
		profil = call.getProfil();

		// De l'intérieur vers l'extérieur (outgoing)
		if (profil.contains("outgoing")) {
			modele = modeleSms.getSms_appel_sortant_ld();
			modele = modele.replace("[ENTREPRISE]", call.getCode_entreprise());
			sms = modele.replace("[LIGNEDIRECTE]", call.getLigne_direct());

			System.err.println(Utils.dateNow() + " ticket " + call.getCode_ticket() + " profil: " + profil);
		} else if (profil == "incoming") {

		}

		return sms;
	}

	/**
	 * Cette fonction permet de meettre a jour les tickets traités
	 * 
	 * @param List<CallMissed>
	 * @return void
	 * 
	 */
	public void updateBulkSms(List<CallMissed> callMissed) {

		if (callMissed != null) {
			for (CallMissed c : callMissed) {
				c.setEtat(1);
				c.setDateModification(new Date());

				callMissedRepos.save(c);
			}
			System.err.println(Utils.dateNow() + " Tous les tickets ont été mis a jour");
		}
	}

}
