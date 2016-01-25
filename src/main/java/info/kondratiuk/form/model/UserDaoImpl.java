/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Dao implementation for the embedded database (HSQL).
 * 
 * @author o.k.
 */
@Repository
public class UserDaoImpl implements IUser {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static List<User> userAccess;
	
	private NamedParameterJdbcTemplate namedParamJdbcTmpl;

	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParamJdbcTmpl = namedParameterJdbcTemplate;
	}

	public static List<User> getAllUsers() {
		return userAccess;
	}
	
	
	@Override
	public User findUserById(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		String sql = "SELECT * FROM users WHERE id=:id";

		User result = null;
		try {
			result = namedParamJdbcTmpl.queryForObject(sql, params, new UserMapper());
			logger.info("User is found by Id: " + id);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("User is not found by Id: " + id);
		}
		return result;
	}

	@Override
	public List<User> findAllUsers() {
		String sql = "SELECT * FROM users";
		List<User> result = namedParamJdbcTmpl.query(sql, new UserMapper());
		userAccess = result;
		logger.info("All User Number: " + result.size());
		return result;
	}

	@Override
	public void saveUser(User user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		String sql = "INSERT INTO USERS(NAME, EMAIL, PASSWORD, COUNTRY, ZIPCODE, CITY, STREET, DAY, MONTH, YEAR, APPID) "
				+ "VALUES ( :name, :email, :password, :country, :zipcode, :city, :street, :day, :month, :year, :appid)";

		namedParamJdbcTmpl.update(sql, getSqlParameterByModel(user), keyHolder);
		user.setId(keyHolder.getKey().intValue());	
		logger.info("User is saved in Database: " + user);
	}

	@Override
	public void updateUser(User user) {
		String sql = "UPDATE USERS SET NAME=:name, EMAIL=:email, PASSWORD=:password, COUNTRY=:country, "
				+ "ZIPCODE=:zipcode, CITY=:city, STREET=:street, DAY=:day, MONTH=:month, YEAR=:year, APPID=:appid";

		namedParamJdbcTmpl.update(sql, getSqlParameterByModel(user));
		logger.info("User is updated in Database: " + user);
	}

	@Override
	public void deleteUser(Integer id) {
		String sql = "DELETE FROM USERS WHERE id= :id";
		namedParamJdbcTmpl.update(sql, new MapSqlParameterSource("id", id));
		logger.info("User is deleted from Database: " + id);
	}

	private SqlParameterSource getSqlParameterByModel(User user) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("id", user.getId());
		paramSource.addValue("name", user.getName());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("password", user.getPassword());		
		paramSource.addValue("country", user.getCountry());		
		paramSource.addValue("zipcode", user.getZipcode());
		paramSource.addValue("city", user.getCity());
		paramSource.addValue("street", user.getStreet());
		paramSource.addValue("day", user.getDay());
		paramSource.addValue("month", user.getMonth());
		paramSource.addValue("year", user.getYear());
		paramSource.addValue("appid", user.getAppid());

		return paramSource;
	}

	private static final class UserMapper implements RowMapper<User> {
		
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setCountry(rs.getString("country"));			
			user.setZipcode(rs.getString("zipcode"));
			user.setCity(rs.getString("city"));
			user.setStreet(rs.getString("street"));
			user.setDay(rs.getString("day"));
			user.setMonth(rs.getString("month"));
			user.setYear(rs.getString("year"));
			user.setAppid(rs.getString("appid"));

			return user;
		}
	}

}