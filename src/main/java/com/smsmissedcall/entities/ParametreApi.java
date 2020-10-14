package com.smsmissedcall.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "parametreApi")
public class ParametreApi {
	
	private static final long serialVersionUID = 1L;

	// Attributs
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String Url;
	private String Flash;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateCreation;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date DateModification;
	private int Etat = 1;

	// constructeur sans params
	public ParametreApi() {
		super();
	}

	public ParametreApi(String url, String flash, Date dateCreation, Date dateModification, int etat) {
		super();
		Url = url;
		Flash = flash;
		this.dateCreation = dateCreation;
		DateModification = dateModification;
		Etat = etat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getFlash() {
		return Flash;
	}

	public void setFlash(String flash) {
		Flash = flash;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateModification() {
		return DateModification;
	}

	public void setDateModification(Date dateModification) {
		DateModification = dateModification;
	}

	public int getEtat() {
		return Etat;
	}

	public void setEtat(int etat) {
		Etat = etat;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
