package com.p2pdinner.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class DinnerDelivery implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String name;
	
	private Collection<MenuItem> dinnerListings = new ArrayList<MenuItem>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<MenuItem> getDinnerListings() {
		return dinnerListings;
	}
	
	public void setDinnerListings(Collection<MenuItem> dinnerListings) {
		this.dinnerListings = dinnerListings;
	}

}
