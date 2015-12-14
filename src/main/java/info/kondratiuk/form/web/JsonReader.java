/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Reader for Json data format
 * 
 * @author o.k.
 */
public class JsonReader {
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		String jsonText = getJson(url);
		JSONObject json = new JSONObject(jsonText);
		return json;
	}
	
	public static JSONArray readJsonArrFromUrl(String url) throws IOException, JSONException {
		String jsonText = getJson(url);		
		JSONArray json = new JSONArray("[" + jsonText + "]");
		return json;
	}

	private static String getJson(String url) throws IOException, MalformedURLException {
		InputStream is = new URL(url).openStream();
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return jsonText;
		} finally {
			is.close();
		}
	}

}
