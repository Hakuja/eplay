package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Bid extends Model {

	public Float amount;
	public Date date;
	
	public Bid(Float amount) {
		this.amount = amount;
		this.date = new Date();
	}
}
