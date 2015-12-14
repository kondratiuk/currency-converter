/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Validator for a city (should not be any numbers in the name).
 * 
 * @author o.k.
 */
@Component("cityValidator")
public class CityValidator {
	private Pattern pattern;
	private Matcher matcher;

	private static final String CITY_PATTERN = ".*\\d+.*";

	public CityValidator() {
		pattern = Pattern.compile(CITY_PATTERN);
	}

	public boolean notValid(final String city) {
		matcher = pattern.matcher(city);
		return matcher.matches();
	}
}
