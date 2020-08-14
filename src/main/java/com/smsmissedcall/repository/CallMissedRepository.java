package com.smsmissedcall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.CallMissed;

public interface CallMissedRepository extends JpaRepository<CallMissed, Long> {

	// Cette requête retourne un CallMissed par son codes
	@Query("SELECT c FROM CallMissed c WHERE c.code_ticket= ?1")
	CallMissed findCallMissedByCode(String code_ticket);
	
	// Cette requête retourne une liste décroissante de CallMissed
	@Query("SELECT c FROM CallMissed c ORDER BY c.id DESC")
	List<CallMissed> findAllTicketOrderByDesc();
	
	// Cette requête retourne une liste CallMissed
	@Query("SELECT c FROM CallMissed c WHERE c.etat=0")
	List<CallMissed> findAllTicketForSendSms();

}
