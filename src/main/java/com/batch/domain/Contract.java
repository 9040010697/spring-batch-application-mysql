package com.batch.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="CONTRACT")
public class Contract {
	
	@Id
	private String contractNo;
	
	private String holderName;
	
	private int duration;
	
	private double amount;
	
	private Date creationDate;

}
