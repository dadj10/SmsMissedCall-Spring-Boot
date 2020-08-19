package com.smsmissedcall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.CallMissed;

public interface CallMissedRepository extends JpaRepository<CallMissed, Long> {

	// Cette requête retourne un CallMissed par son codes
	@Query("SELECT c FROM CallMissed c WHERE c.code_ticket=?1")
	CallMissed findCallMissedByCode(String code_ticket);

	// Cette requête retourne une liste décroissante de CallMissed
	@Query("SELECT c FROM CallMissed c ORDER BY c.id DESC")
	List<CallMissed> findAllTicketOrderByDesc();

	// Cette requête retourne une liste CallMissed
	@Query("SELECT c FROM CallMissed c WHERE c.etat=0")
	List<CallMissed> findAllTicketForSendSms();

	// Cette requête retourne un CallMissed par son codes
	// @Query("SELECT c FROM CallMissed c WHERE c.poste=?1 AND c.destinataire =?2 ORDER BY c.id DESC")
	@Query(value = "SELECT * FROM callmissed WHERE poste=?1 AND destinataire =?2  ORDER BY id DESC LIMIT 1", nativeQuery = true)
	CallMissed findLastCall(String poste, String destinataire);

}
