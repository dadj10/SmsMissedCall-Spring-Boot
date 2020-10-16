package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.ModeleSms;

public interface ModeleSmsRepository extends JpaRepository<ModeleSms, Long> {

	/* Cette requete permet de retourner la liste des modeles de sms */
	@Query("SELECT c FROM ModeleSms c")
	ModeleSms findModelSms();

}
