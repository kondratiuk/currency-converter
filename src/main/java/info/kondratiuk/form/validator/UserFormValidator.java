/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.validator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import info.kondratiuk.form.model.User;
import info.kondratiuk.form.web.JsonReader;

/**
 * Validator for the User form.
 * 
 * @author o.k.
 */
@Component
public class UserFormValidator implements Validator {
	@Autowired
	@Qualifier("emailValidator")
	EmailValidator emailValidator;

	@Autowired
	@Qualifier("cityValidator")
	CityValidator cityValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.userForm.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.userForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.userForm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.userForm.confirmPassword");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "NotEmpty.userForm.country");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "zipcode", "NotEmpty.userForm.zipcode");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "NotEmpty.userForm.city");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "NotEmpty.userForm.street");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "appid", "NotEmpty.userForm.appid");

		// name
		if (user.getName().length() > 50) {
			errors.rejectValue("name", "Userform.name.length");
		}

		// email
		if (!emailValidator.valid(user.getEmail())) {
			errors.rejectValue("email", "Pattern.userForm.email");
		}
		if (user.getEmail().length() > 100) {
			errors.rejectValue("email", "Userform.email.length");
		}

		// password
		if (user.getPassword().length() < 3 || user.getPassword().length() > 20) {
			errors.rejectValue("password", "Userform.password.length");
		}

		// confirm password
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "Diff.userform.confirmPassword");
		}

		// country
		if (user.getCountry().equalsIgnoreCase("none")) {
			errors.rejectValue("country", "NotEmpty.userForm.country");
		}

		// zipcode
		try {
			Integer.parseInt(user.getZipcode());
		} catch (NumberFormatException nfe) {
			errors.rejectValue("zipcode", "Userform.zipcode.format");
		}
		if (user.getZipcode().length() != 5) {
			errors.rejectValue("zipcode", "Userform.zipcode.length");
		}

		// city
		if (cityValidator.notValid(user.getCity())) {
			errors.rejectValue("city", "Pattern.userForm.city");
		}
		if (user.getCity().length() < 3 || user.getCity().length() > 100) {
			errors.rejectValue("city", "Userform.city.length");
		}

		// street
		if (user.getStreet().length() < 3 || user.getStreet().length() > 200) {
			errors.rejectValue("street", "Userform.street.length");
		}

		// day
		if (!user.getDay().isEmpty()) {
			try {
				int day = Integer.parseInt(user.getDay());
				if (day < 1 || day > 31) {
					errors.rejectValue("day", "Userform.day.length");
				}
			} catch (NumberFormatException nfe) {
				errors.rejectValue("day", "Userform.day.format");
			}
		}
		if (!user.getMonth().isEmpty() || !user.getYear().isEmpty()) {
			if (user.getDay().isEmpty()) {
				errors.rejectValue("day", "Userform.day.empty");
			}
		}

		// month
		if (!user.getDay().isEmpty() || !user.getYear().isEmpty()) {
			if (user.getMonth().isEmpty()) {
				errors.rejectValue("month", "Userform.month.empty");
			}
		}

		// year
		if (!user.getYear().isEmpty()) {
			try {
				int year = Integer.parseInt(user.getYear());

				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String currentYear = df.format(date);

				if (year < 1900 || year > Integer.parseInt(currentYear)) {
					errors.rejectValue("year", "Userform.year.length");
				}
			} catch (NumberFormatException nfe) {
				errors.rejectValue("year", "Userform.year.format");
			}
		}
		if (!user.getDay().isEmpty() || !user.getMonth().isEmpty()) {
			if (user.getYear().isEmpty()) {
				errors.rejectValue("year", "Userform.year.empty");
			}
		}

		// appid
		try {
			JsonReader.readJsonFromUrl("https://openexchangerates.org/api/latest.json?app_id=" + user.getAppid());
		} catch (JSONException | IOException e) {
			errors.rejectValue("appid", "Userform.appid.invalid");
		}

	}

}