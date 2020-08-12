package com.smsmissedcall.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
@Table(name = "param_sql_server")
public class ParamsSqlServer {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dbname;

	private String host;
	private int port;

	@NotNull
	private String username;
	@NotNull
	private String password;

	private int etat = 1;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateInsertion;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateModification;

	// constructeur sans params
	public ParamsSqlServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	// constructeur avec params
	public ParamsSqlServer(String dbname, String host, int port, String username, String password, int etat,
			Date dateInsertion, Date dateModification) {
		super();
		this.dbname = dbname;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.etat = etat;
		this.dateInsertion = dateInsertion;
		this.dateModification = dateModification;
	}

	// getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public Date getDateInsertion() {
		return dateInsertion;
	}

	public void setDateInsertion(Date dateInsertion) {
		this.dateInsertion = dateInsertion;
	}

	public Date getDateModification() {
		return dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
