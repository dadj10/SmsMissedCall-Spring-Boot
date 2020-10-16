package com.smsmissedcall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smsmissedcall.entities.ParametreSqlServer;

public interface ParametreSqlServerRepository extends JpaRepository<ParametreSqlServer, Long> {

	// Cette requête retourne un ParamsSqlServer
	@Query("SELECT c FROM ParametreSqlServer c WHERE c.etat=1")
	ParametreSqlServer findParamSqlServer();

	// Cette requête retourne un ParamsSqlServer par son codes
	@Query("SELECT c FROM ParametreSqlServer c WHERE c.id=?1 AND c.etat=1")
	ParametreSqlServer findSqlServerById(Long id);

}
