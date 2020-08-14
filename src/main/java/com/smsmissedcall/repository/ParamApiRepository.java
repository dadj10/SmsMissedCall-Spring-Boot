package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.ParamApi;

public interface ParamApiRepository extends JpaRepository<ParamApi, Long> {

	// Cette requête retourne un ParamApi
	@Query("SELECT c FROM ParamApi c WHERE c.Etat=1")
	ParamApi findOneParamApi();

	// Cette requête retourne un ParamApi par son codes
	@Query("SELECT c FROM ParamApi c WHERE c.id=?1 AND c.Etat=1")
	ParamApi findParamApiById(Long id);

}
