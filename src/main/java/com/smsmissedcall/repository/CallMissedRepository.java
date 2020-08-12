package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.CallMissed;

public interface CallMissedRepository extends JpaRepository<CallMissed, Long> {

	// Cette requête retourne un CallMissed par son codes
	@Query("SELECT c FROM CallMissed c WHERE c.codes= ?1")
	CallMissed findCallMissedByCode(String codes);

}
