package com.jws.example.service;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

import com.jws.example.model.Prediction;

public class Predictions {

	private ConcurrentMap<Integer, Prediction> predictions;
	private ServletContext sctx;
	private AtomicInteger mapKey;

	public Predictions() {
		this.predictions = new ConcurrentHashMap<Integer, Prediction>();
		this.mapKey = new AtomicInteger();
	}

	public ConcurrentMap<Integer, Prediction> getPredictions() {
		if (sctx == null) {
			return null;
		}
		if (predictions.isEmpty()) {
			populate();
		}
		return predictions;
	}

	public int addPrediction(Prediction prediction) {
		prediction.setId(mapKey.get());
		this.predictions.put(mapKey.get(), prediction);
		return mapKey.getAndIncrement();
	}

	private void populate() {
		addPrediction(new Prediction("The sky is very black. It is going to snow"));
		addPrediction(new Prediction("It's 8.30! You're going to miss the train!"));
		addPrediction(new Prediction("I crashed the company car. My boss isn't going to be very happy!"));
		addPrediction(new Prediction("It'll almost certainly rain. "));
		addPrediction(new Prediction("I don't think he'll come tonight."));
		addPrediction(new Prediction("I predict that Congress will pass an anti-piracy law soon."));
		addPrediction(new Prediction("If you ask him, he'll probably give you a lift."));
	}

	public String toXML(Object obj) {
		String xml = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(out);
			encoder.writeObject(obj);
			encoder.close();
			xml = encoder.toString();
		} catch (Exception e) {
		}
		return xml;
	}

	public void setPredictions(ConcurrentMap<Integer, Prediction> predictions) {
		this.predictions = predictions;
	}

	public ServletContext getSctx() {
		return sctx;
	}

	public void setSctx(ServletContext sctx) {
		this.sctx = sctx;
	}

	public AtomicInteger getMapKey() {
		return mapKey;
	}

	public void setMapKey(AtomicInteger mapKey) {
		this.mapKey = mapKey;
	}

}
