package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.ParametreApi;

public interface ParametreApiRepository extends JpaRepository<ParametreApi, Long> {

	/* Cette requête retourne un ParametreApi */
	@Query("SELECT c FROM ParametreApi c WHERE c.Etat=1")
	ParametreApi findOneParametreApi();

}
