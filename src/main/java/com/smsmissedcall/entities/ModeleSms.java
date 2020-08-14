package com.smsmissedcall.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "modelesms")
public class ModeleSms {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String sms_appel_entrant;
	private String sms_appel_sortant_ld;
	private String sms_appel_sortant_sld;	

	private String initial;

	// constructeur sans params
	public ModeleSms() {
		super();
		// TODO Auto-generated constructor stub
	}

	// constructeur avec params
	public ModeleSms(String sms_appel_entrant, String sms_appel_sortant_ld, String delai, String initial,
			String sms_appel_sortant_sld) {
		super();
		this.sms_appel_entrant = sms_appel_entrant;
		this.sms_appel_sortant_ld = sms_appel_sortant_ld;
		this.sms_appel_sortant_sld = sms_appel_sortant_sld;
		this.initial = initial;
	}

	// getters && setters
	public String getSms_appel_entrant() {
		return sms_appel_entrant;
	}

	public void setSms_appel_entrant(String sms_appel_entrant) {
		this.sms_appel_entrant = sms_appel_entrant;
	}

	public String getSms_appel_sortant_ld() {
		return sms_appel_sortant_ld;
	}

	public void setSms_appel_sortant_ld(String sms_appel_sortant_ld) {
		this.sms_appel_sortant_ld = sms_appel_sortant_ld;
	}

	public String getSms_appel_sortant_sld() {
		return sms_appel_sortant_sld;
	}

	public void setSms_appel_sortant_sld(String sms_appel_sortant_sld) {
		this.sms_appel_sortant_sld = sms_appel_sortant_sld;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

}
