/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.model;

import java.util.List;

/**
 * Interface for user
 * 
 * @author o.k.
 */
public interface IUser {
	User findUserById(Integer id);
	List<User> findAllUsers();
	void deleteUser(Integer id);
	void saveUser(User user);
	void updateUser(User user);
}
