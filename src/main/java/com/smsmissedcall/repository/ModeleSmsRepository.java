package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.ModeleSms;

public interface ModeleSmsRepository extends JpaRepository<ModeleSms, Long> {

	// Cette requête retourne un CallMissed par son codes
	@Query("SELECT c FROM ModeleSms c")
	ModeleSms findModelSms();

}
