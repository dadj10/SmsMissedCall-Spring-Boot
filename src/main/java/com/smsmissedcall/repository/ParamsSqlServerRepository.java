package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.ParamsSqlServer;

public interface ParamsSqlServerRepository extends JpaRepository<ParamsSqlServer, Long> {

	// Cette requête retourne un ParamsSqlServer par son codes
	@Query("SELECT c FROM ParamsSqlServer c WHERE c.etat=1")
	ParamsSqlServer findParamSqlServer();

	// Cette requête retourne un ParamsSqlServer par son codes
	@Query("SELECT c FROM ParamsSqlServer c WHERE c.id=?1 AND c.etat=1")
	ParamsSqlServer findSqlServerById(Long id);

}
