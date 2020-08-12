package com.smsmissedcall.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "callmissed")
public class CallMissed {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String codes;
	private String Postes;

	private String Dateheuredecroche;

	private String dest1;
	private String duree;

	private int traite;
	private String profil;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private String dateheuralerte;

	private int etat = 1;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateInsertion;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateModification;

	// constructeur sans params
	public CallMissed() {
		super();
		// TODO Auto-generated constructor stub
	}

	// constructeur avec params
	public CallMissed(String codes, String postes, String dateheuredecroche, String dest1, String duree, int traite,
			String profil, String dateheuralerte, int etat, Date dateInsertion, Date dateModification) {
		super();
		this.codes = codes;
		Postes = postes;
		Dateheuredecroche = dateheuredecroche;
		this.dest1 = dest1;
		this.duree = duree;
		this.traite = traite;
		this.profil = profil;
		this.dateheuralerte = dateheuralerte;
		this.etat = etat;
		this.dateInsertion = dateInsertion;
		this.dateModification = dateModification;
	}

	// getters a setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public String getPostes() {
		return Postes;
	}

	public void setPostes(String postes) {
		Postes = postes;
	}

	public String getDateheuredecroche() {
		return Dateheuredecroche;
	}

	public void setDateheuredecroche(String dateheuredecroche) {
		Dateheuredecroche = dateheuredecroche;
	}

	public String getDest1() {
		return dest1;
	}

	public void setDest1(String dest1) {
		this.dest1 = dest1;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}

	public int isTraite() {
		return traite;
	}

	public void setTraite(int traite) {
		this.traite = traite;
	}

	public String getProfil() {
		return profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public String getDateheuralerte() {
		return dateheuralerte;
	}

	public void setDateheuralerte(String dateheuralerte) {
		this.dateheuralerte = dateheuralerte;
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
