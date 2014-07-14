package com.jws.example.servlet;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.json.JSONObject;
import org.json.XML;

import com.jws.example.model.Prediction;
import com.jws.example.service.Predictions;

public class PredictionsServlet extends HttpServlet {

	private Predictions predictions;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		this.predictions = new Predictions();
		this.predictions.setSctx(getServletContext());
	}

	// GET predictions2
	// GET predictions2?id=1
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("id");
		Integer key = param == null ? null : Integer.valueOf(param);
		boolean json = false;
		String accept = request.getHeader("accept");
		if (accept != null && accept.contains("json")) {
			json = true;
		}

		if (key == null) {
			ConcurrentMap<Integer, Prediction> map = predictions.getPredictions();
			Object[] list = map.values().toArray();
			Arrays.sort(list);
			String xml = predictions.toXML(list);
			sendResponse(response, xml, json);
		} else {
			Prediction pred = predictions.getPredictions().get(key);
			if (pred == null) {
				sendResponse(response, predictions.toXML(key + " does not map to a prediction.\n"), false);
			} else {
				sendResponse(response, predictions.toXML(pred), json);
			}

		}
	}

	// POST predictions2
	// HTTP body should contain two keys, one for the predictor "who" and
	// another for the prediction "what"
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String who = request.getParameter("who");
		String what = request.getParameter("what");
		if (who == null || what == null) {
			throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
		}
		// Creating a new prediction
		Prediction pred = new Prediction();
		pred.setWhat(what);
		pred.setWho(who);
		int id = predictions.addPrediction(pred);
		String message = "Prediction " + id + " was created";
		sendResponse(response, predictions.toXML(message), false);
	}

	// PUT predictions2
	// HTTP body should contain at least two keys, the id and either who or what
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (id == null) {
			throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
		}
		String who = request.getParameter("who");
		String what = request.getParameter("what");
		if (who == null && what == null) {
			throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
		}
		// Updating prediction
		Integer key = Integer.valueOf(id);
		Prediction pred = predictions.getPredictions().get(key);
		if (pred == null) {
			String message = "Key " + id + " does not map to a Prediction\n";
			sendResponse(response, predictions.toXML(message), false);
		}
		if (what != null) {
			pred.setWhat(what);
		}
		if (who != null) {
			pred.setWho(who);
		}

		String message = "Prediction " + id + " has been updated";
		sendResponse(response, predictions.toXML(message), false);
	}

	// PUT predictions2
	// HTTP id must be populated
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (id == null) {
			throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
		}
		predictions.getPredictions().remove(id);
		String message = "Prediction " + id + " has been deleted";
		sendResponse(response, predictions.toXML(message), false);

	}

	private void sendResponse(HttpServletResponse response, String payload, boolean json) {
		try {
			if (json) {
				JSONObject jobt = XML.toJSONObject(payload);
				payload = jobt.toString();
			}
			OutputStream out = response.getOutputStream();
			out.write(payload.getBytes());
			out.flush();
		} catch (Exception e) {
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		}
	}
}
