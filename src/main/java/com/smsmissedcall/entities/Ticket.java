package com.smsmissedcall.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "smsMissedCallTicket")
public class Ticket {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nom;
	private String ligne_direct;
	private String code_entreprise;
	private String code_ticket;
	private String date_heure_decroche;
	private String destinataire;
	private String duree;
	private int traite;
	private String profil;
	private String date_heure_alerte;
	private String poste;
	private String standard;

	private int etat = 1;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateInsertion;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date dateModification;

	private int idGroupeEnvoi;

	public Ticket() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ticket(String nom, String ligne_direct, String code_entreprise, String code_ticket,
			String date_heure_decroche, String destinataire, String duree, int traite, String profil,
			String date_heure_alerte, String poste, String standard, int etat, Date dateInsertion,
			Date dateModification) {
		super();
		this.nom = nom;
		this.ligne_direct = ligne_direct;
		this.code_entreprise = code_entreprise;
		this.code_ticket = code_ticket;
		this.date_heure_decroche = date_heure_decroche;
		this.destinataire = destinataire;
		this.duree = duree;
		this.traite = traite;
		this.profil = profil;
		this.date_heure_alerte = date_heure_alerte;
		this.poste = poste;
		this.standard = standard;
		this.etat = etat;
		this.dateInsertion = dateInsertion;
		this.dateModification = dateModification;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLigne_direct() {
		return ligne_direct;
	}

	public void setLigne_direct(String ligne_direct) {
		this.ligne_direct = ligne_direct;
	}

	public String getCode_entreprise() {
		return code_entreprise;
	}

	public void setCode_entreprise(String code_entreprise) {
		this.code_entreprise = code_entreprise;
	}

	public String getCode_ticket() {
		return code_ticket;
	}

	public void setCode_ticket(String code_ticket) {
		this.code_ticket = code_ticket;
	}

	public String getDate_heure_decroche() {
		return date_heure_decroche;
	}

	public void setDate_heure_decroche(String date_heure_decroche) {
		this.date_heure_decroche = date_heure_decroche;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}

	public int getTraite() {
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

	public String getDate_heure_alerte() {
		return date_heure_alerte;
	}

	public void setDate_heure_alerte(String date_heure_alerte) {
		this.date_heure_alerte = date_heure_alerte;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
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

	public int getIdGroupeEnvoi() {
		return idGroupeEnvoi;
	}

	public void setIdGroupeEnvoi(int idGroupeEnvoi) {
		this.idGroupeEnvoi = idGroupeEnvoi;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
