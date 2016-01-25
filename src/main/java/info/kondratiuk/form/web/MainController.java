/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 * Currency Converter uses external services. 
 * All usage is subject to your acceptance of the Terms and Conditions of Service, available at: https://openexchangerates.org/terms/
 *
 */
package info.kondratiuk.form.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import info.kondratiuk.form.model.User;
import info.kondratiuk.form.model.UserDaoImpl;

/**
 * Controller for the application
 * 
 * @author o.k.
 */
@Controller
public class MainController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static String currentAppId = "";

	private Map<String, String> availableCurrencyTypes;
	private StringBuilder disclaimer;
	private StringBuilder errors;
	private StringBuilder historyLine;
	private StringBuilder historyResult;
	private int requestNumber = 1;
	private int historyLimit = 10;
	private Queue<String> historyQueue = new LinkedList<String>();

	{
		errors = new StringBuilder();
		historyLine = new StringBuilder();
		historyResult = new StringBuilder();
		try {
			availableCurrencyTypes = new TreeMap<String, String>();
			JSONArray jsonArray = JsonReader.readJsonArrFromUrl("https://openexchangerates.org/api/currencies.json");
			JSONObject jsonObj = jsonArray.getJSONObject(0);
			JSONArray names = jsonObj.names();

			for (Object name : names) {
				String key = (String) name;
				String val = (String) jsonObj.get(key);
				availableCurrencyTypes.put(key, val + " (" + key + ")");
				logger.info("Available Currency Types are loaded");
			}
		} catch (JSONException | IOException e) {
			errors.append("Error: currency types are not available now! Try later, please.");
			e.printStackTrace();
		}
	}

	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("hello");
		return model;
	}

	@RequestMapping(value = "/main**", method = RequestMethod.GET)
	public ModelAndView mainPage(@ModelAttribute("actualCurrency") CurrencyEntry currForm) {
		detectCurrentAppId();
		if (disclaimer == null) {
			disclaimer = getDisclaimer();
		}
		
		String inputForHandling = currForm.getCurrency();
		String result = "";
		if (inputForHandling != null) {
			String[] currArr = inputForHandling.split(",");
			String rate = calculateCurrentRate(currArr);
			result = errors.length() > 0 ? errors.toString() : rate;
			if (historyQueue.size() >= historyLimit) {
				historyQueue.remove();
			}
			historyLine.append("(").append(requestNumber++).append(") ").append(result).append("\n");
			historyQueue.add(historyLine.toString());
			historyLine.setLength(0);
			historyResult.setLength(0);
			for (String sb : historyQueue) {
				historyResult.append(sb);
			}
		}		
		
		ModelAndView model = new ModelAndView();
		model.addObject("disclaimer", disclaimer.toString());
		model.addObject("actualCurrency", new CurrencyEntry());
		model.addObject("allCurrency", availableCurrencyTypes);
		model.addObject("result", result);
		model.addObject("history", historyResult.toString());
		model.setViewName("main");

		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and/or password!");
		}

		if (logout != null) {
			clearSources();
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");		

		return model;
	}

	private void detectCurrentAppId() {
		String currentUserEmail = UserAccessController.currentUser;
		List<User> users = UserDaoImpl.getAllUsers();
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(currentUserEmail)) {
				currentAppId = user.getAppid();
				continue;
			}
		}
	}

	private StringBuilder getDisclaimer() {
		disclaimer = new StringBuilder();
		try {
			JSONObject jsonObject = JsonReader
					.readJsonFromUrl("https://openexchangerates.org/api/latest.json?app_id=" + currentAppId);
			String discl = jsonObject.getString("disclaimer");
			disclaimer.append(discl);
			logger.info("Disclaimer is loaded");
		} catch (JSONException | IOException e) {
			logger.warn("Disclaimer is not available!");
			disclaimer.append("Disclaimer is not available!");
		}
		return disclaimer;
	}

	private String calculateCurrentRate(String[] currArr) {
		Map<String, Double> rates = new HashMap<String, Double>(availableCurrencyTypes.size());
		JSONObject jsonObject = null;
		String result = "";

		try {
			jsonObject = JsonReader
					.readJsonFromUrl("https://openexchangerates.org/api/latest.json?app_id=" + currentAppId);
		} catch (JSONException | IOException e) {
			errors.append("Error: currency rates are not available now! Try later, please.");
			e.printStackTrace();
		}
		String baseCurrency = jsonObject.getString("base");
		if (!baseCurrency.equalsIgnoreCase("usd")) {
			errors.append("Error: base currency is not available now! Try later, please.");
			return "";
		}

		long timestampUnix = jsonObject.getLong("timestamp");
		int ms = 1000;
		Date timestamp = new Date((long) timestampUnix * ms);

		JSONObject ratesObject = jsonObject.getJSONObject("rates");
		JSONArray names = ratesObject.names();
		for (Object name : names) {
			String key = (String) name;
			double val = ratesObject.getDouble(key);
			rates.put(key, val);
		}

		// Currency must be converted here via String (not double) to avoid
		// precision loss
		String fromCurr = rates.get(currArr[2]) + "";
		String toCurr = rates.get(currArr[3]) + "";
		int scale = Integer.parseInt(currArr[0]);
		if (scale < 1) {
			return "Error: Incorrect precision value: " + scale + " Valid numbers are between 1 and 99.";
		}
		int roundingMode = BigDecimal.ROUND_HALF_UP;

		try {
			BigDecimal fromCurrency = new BigDecimal(fromCurr).setScale(scale, roundingMode);
			BigDecimal toCurrency = new BigDecimal(toCurr).setScale(scale, roundingMode);

			BigDecimal amountCurrency = new BigDecimal(currArr[1]);
			double amountVal = amountCurrency.doubleValue();
			if (amountVal <= 0) {
				return "Error: Incorrect amount value: " + amountVal + " Valid input number must be bigger than 0.";
			}

			BigDecimal res = toCurrency.divide(fromCurrency, scale, roundingMode);
			res = res.multiply(amountCurrency);
			result = amountCurrency + " " + currArr[2] + " = " + res + " " + currArr[3] + " >>> Rate valid time: "
					+ timestamp + " | Request time: " + new Date();
		} catch (NumberFormatException nfe) {
			result = "Error: Incorrect input data format or currency is not selected.";
		}

		return result;
	}

	private void clearSources() {
		errors.setLength(0);
		historyLine.setLength(0);
		historyResult.setLength(0);

		requestNumber = 1;
		historyQueue.clear();
		logger.debug("Sources were cleared");
	}
}