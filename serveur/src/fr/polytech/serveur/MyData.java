package fr.polytech.serveur;

import java.io.Serializable;
import java.sql.Timestamp;

public class MyData implements Serializable {
	private String nomfruit;//Nom de Fruit
	private int id;//Id
	private Timestamp reception;
	private Timestamp fournisseur;
	private Float prix;
	private String corrID;
	public String getNomfruit() {
		return nomfruit;
	}
	public void setNomfruit(String nomfruit) {
		this.nomfruit = nomfruit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getReception() {
		return reception;
	}
	public void setReception(Timestamp reception) {
		this.reception = reception;
	}
	public Timestamp getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(Timestamp fournisseur) {
		this.fournisseur = fournisseur;
	}
	public Float getPrix() {
		return prix;
	}
	public void setPrix(Float prix) {
		this.prix = prix;
	}
	public String getCorrID() {
		return corrID;
	}
	public void setCorrID(String corrID) {
		this.corrID = corrID;
	}
	
	
	
}
