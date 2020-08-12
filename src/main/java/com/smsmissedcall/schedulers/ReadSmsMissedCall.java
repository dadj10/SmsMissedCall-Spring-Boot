package com.smsmissedcall.schedulers;

import com.smsmissedcall.methods.Utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smsmissedcall.entities.CallMissed;
import com.smsmissedcall.entities.ParamsSqlServer;
import com.smsmissedcall.repository.CallMissedRepository;
import com.smsmissedcall.repository.ParamsSqlServerRepository;

@Component
public class ReadSmsMissedCall {

	@Autowired
	private CallMissedRepository callMissedRepos;

	private ParamsSqlServerRepository paramsSqlServerRepos;

	private Connection con = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@Scheduled(fixedDelay = 10000)
	public void ReadSqlServerJDBC() {
		// 1. J'initialise les params de connexion au SGBD
		String dbname = null;
		String host = null;
		int port;

		String username = null;
		String password = null;

		// 2. Je récupère les parametres du SGBD
		ParamsSqlServer params = null;
		params = paramsSqlServerRepos.findParamSqlServer();

		if (params != null) {

			dbname = params.getDbname();
			host = params.getHost();
			port = params.getPort();

			// 3. Je formate l'URL de la connexion JDBC
			String url = "jdbc:sqlserver://" + host + "\\SQLEXPRESS:" + port + ";databaseName=" + dbname + "";

			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

			username = params.getUsername();
			password = params.getPassword();

			try {
				// 4. Je Charge le driver de la connexion JDBC
				Class.forName(driver).newInstance();

				// 5. Je me connecte au SGBD
				con = DriverManager.getConnection(url, username, password);

				// 6. Je prepare 'statement' l'instence permettant d'excuteur des requêtes SQL
				// vers la base de données
				statement = con.createStatement();

				// 7. Je prépara la requète et puis je l'exécute
				String selectQuery = "SELECT * FROM CallMissed WHERE traite = 'False'";
				resultSet = statement.executeQuery(selectQuery);

				String codes, Postes, Dateheuredecroche, dest1, duree, profil;

				// 8. Je parcours le résultat de l'exécution
				while (resultSet.next()) {
					codes = resultSet.getString("codes");
					Postes = resultSet.getString("Postes");

					Dateheuredecroche = resultSet.getString("Dateheuredecroche");

					dest1 = resultSet.getString("dest1");
					duree = resultSet.getString("duree");
					int traite = resultSet.getByte("traite");

					profil = resultSet.getString("profil");

					// 9. Je verifie que le code n'est pas enregistré
					CallMissed callMissed = null;
					callMissed = callMissedRepos.findCallMissedByCode(codes);

					if (callMissed == null) {

						// 10. J'enregistre le call missed
						CallMissed call = new CallMissed();

						call.setCodes(codes);
						call.setPostes(Postes);
						call.setDateheuredecroche(Dateheuredecroche);
						call.setDest1(dest1);
						call.setDuree(duree);

						call.setTraite(0);

						call.setProfil(profil);

						call.setDateInsertion(new Date());
						call.setDateModification(new Date());

						call = callMissedRepos.save(call);

						// 11. Je mets a jour le CallMissed existant de Sql Server
						if (call != null) {
							// 12. Je prépare la requète de mise a jour
							String updateQuery = "UPDATE CallMissed SET traite=? WHERE codes=?";

							preparedStatement = con.prepareStatement(updateQuery);
							preparedStatement.setInt(1, 1);
							preparedStatement.setString(2, codes);

							// 13. J'exécute la requète de mise a jour
							int rowsUpdated = preparedStatement.executeUpdate();
							if (rowsUpdated > 0) {
								System.err.println(
										"Un CallMissed codes " + codes + " existant a été mis à jour avec succès !");
							}
						}
					} else {
						System.err.println(Utils.dateNow() + "CallMissed déja enregistré");
					}
				}

				// Je ferme la connexion
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

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
}
