/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.web;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.kondratiuk.form.model.IUser;
import info.kondratiuk.form.model.User;
import info.kondratiuk.form.validator.UserFormValidator;

/**
 * Controller for the User management service.
 * 
 * @author o.k.
 */
@Controller
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserFormValidator userFormValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userFormValidator);
	}

	@Autowired
	private IUser userService;

	// list page
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String showAllUsers(Model model) {

		logger.debug("showAllUsers()");
		model.addAttribute("users", userService.findAllUsers());
		return "users/list";
	}

	// save or update user
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String saveOrUpdateUser(@ModelAttribute("userForm") @Validated User user, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {

		logger.debug("saveOrUpdateUser() : {}", user);

		if (result.hasErrors()) {
			populateDefaultModel(model);
			return "users/userform";
		} else {
			redirectAttributes.addFlashAttribute("css", "success");
			if (user.isNew()) {
				redirectAttributes.addFlashAttribute("msg", "User added successfully!");
			} else {
				redirectAttributes.addFlashAttribute("msg", "User updated successfully!");
			}

			
			
			if (userService.findUserById(user.getId()) == null) {
				userService.saveUser(user);
			} else {
				userService.updateUser(user);
			}
			// POST/REDIRECT/GET
			return "redirect:/users/" + user.getId();
		}
	}

	// show add user form
	@RequestMapping(value = "/users/add", method = RequestMethod.GET)
	public String showAddUserForm(Model model) {
		logger.debug("showAddUserForm()");
		User user = new User();

		// set default value in table
		// user.setName("alex");
		// user.setEmail("alex@gmail.com");
		// user.setStreet("address");
		// user.setPassword("123");
		// user.setConfirmPassword("123");
		// user.setCountry("Germany");

		model.addAttribute("userForm", user);

		populateDefaultModel(model);
		return "users/userform";
	}

	// show update form
	@RequestMapping(value = "/users/{id}/update", method = RequestMethod.GET)
	public String showUpdateUserForm(@PathVariable("id") int id, Model model) {
		logger.debug("showUpdateUserForm() : {}", id);

		User user = userService.findUserById(id);
		model.addAttribute("userForm", user);

		populateDefaultModel(model);
		return "users/userform";
	}

	// delete user
	@RequestMapping(value = "/users/{id}/delete", method = RequestMethod.GET)
	public String deleteUser(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
		logger.debug("deleteUser() : {}", id);
		userService.deleteUser(id);

		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "User is deleted!");

		return "redirect:/users";
	}

	// show user
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String showUser(@PathVariable("id") int id, Model model) {
		logger.debug("showUser() id: {}", id);

		User user = userService.findUserById(id);
		if (user == null) {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "User not found");
		}
		model.addAttribute("user", user);
		return "users/show";
	}

	private void populateDefaultModel(Model model) {
		Map<String, String> month = new LinkedHashMap<String, String>();
		month.put("1", "January");
		month.put("2", "February");
		month.put("3", "March");
		month.put("4", "April");
		month.put("5", "May");
		month.put("6", "June");
		month.put("7", "July");
		month.put("8", "August");
		month.put("9", "September");
		month.put("10", "October");
		month.put("11", "November");
		month.put("12", "December");
		model.addAttribute("monthList", month);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ModelAndView handleEmptyData(HttpServletRequest req, Exception ex) {
		logger.debug("handleEmptyData()");
		logger.error("Request: {}, error ", req.getRequestURL(), ex);

		ModelAndView model = new ModelAndView();
		model.setViewName("user/show");
		model.addObject("msg", "user not found");

		return model;
	}

}