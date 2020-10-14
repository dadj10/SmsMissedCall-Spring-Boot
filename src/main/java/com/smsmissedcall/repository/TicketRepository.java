package com.smsmissedcall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	// Cette requête retourne un CallMissed par son codes
	@Query("SELECT c FROM Ticket c WHERE c.code_ticket=?1")
	Ticket findCallMissedByCode(String code_ticket);

	// Cette requête retourne une liste décroissante de CallMissed
	@Query("SELECT c FROM Ticket c ORDER BY c.id DESC")
	List<Ticket> findAllTicketOrderByDesc();

	// Cette requête retourne une liste CallMissed
	@Query("SELECT c FROM Ticket c WHERE c.etat=0")
	List<Ticket> findAllTicketForSendSms();

	// Requètes natives
	@Query(value = "SELECT groupeenvoi.id_groupe_envoi FROM utilisateur INNER JOIN groupeenvoi ON utilisateur.id_groupe_envoi = groupeenvoi.id_groupe_envoi WHERE LOWER(groupeenvoi.sender) = ?1 AND utilisateur.role = 'SmsMissedCall' AND utilisateur.etat = 1 AND groupeenvoi.etat = 1", nativeQuery = true)
	List<Object[]> findIdGroupeEnvoieSmsMissedCallBySender(String sender);

	// Requètes natives
	@Query(value = "SELECT username, token, sender, utilisateur.nom, groupeenvoi.id_groupe_envoi FROM utilisateur INNER JOIN groupeenvoi ON utilisateur.id_groupe_envoi = groupeenvoi.id_groupe_envoi WHERE utilisateur.role = 'SmsMissedCall' AND utilisateur.etat = 1 AND groupeenvoi.etat = 1", nativeQuery = true)
	List<Object[]> findAllSmsMissedCallApplicationAndGroupeEnvoi();

	@Query(value = "SELECT * FROM groupeenvoi WHERE id_groupe_envoi = ?1 AND etat = 1 LIMIT 1", nativeQuery = true)
	List<Object[]> findGroupeEnvoieById(Long idGroupEnvoi);

	@Query(value = "SELECT * FROM sms_missed_call_ticket WHERE poste=?1 AND destinataire =?2  ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Ticket findLastCall(String poste, String destinataire);

	// Je recupère les ticket du jour appartenant au groupe d'envois de
	// l'application.
	@Query(value = "SELECT * FROM sms_missed_call_ticket WHERE id_groupe_envoi=?1 AND DATE(date_insertion) = CURRENT_DATE AND etat = 0 ORDER BY id DESC", nativeQuery = true)
	List<Ticket> findAllTicketOfTodayByIdGroupEnvoi(Long id_groupe_envoi);
}
