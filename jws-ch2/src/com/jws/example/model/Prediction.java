package com.jws.example.model;

import java.io.Serializable;
import java.util.Random;

public class Prediction implements Serializable, Comparable<Prediction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String who;
	private String what;
	private int id;

	public Prediction() {
		super();
	}

	public Prediction(String what) {
		super();
		this.setWho(getRandomName(10));
		this.setWhat(what);
	}

	private String getRandomName(int maxLength) {
		String result = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while (i < maxLength) {
			char c = (char) r.nextInt(255);
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z')) {
				result += c;
				i++;
			}
		}
		return result;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(Prediction other) {
		return this.id - other.id;
	}

}
