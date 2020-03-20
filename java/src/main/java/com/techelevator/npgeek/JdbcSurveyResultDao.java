package com.techelevator.npgeek;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


@Component
public class JdbcSurveyResultDao implements SurveyResultsDAO {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcSurveyResultDao(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// Method that saves a survey to the survey_result table in npgeek database
	@Override
	public void createSurvey(SurveyResult result) {
		String sql= "INSERT INTO survey_result(surveyid, parkcode, emailaddress, state, activitylevel) "
				+ "VALUES (?,?,?,?,?)";
		jdbcTemplate.update(sql, result.getSurveyId(), result.getParkCode(), 
				result.getEmailAddress(), result.getState(), result.getActivityLevel());
	}

	// Method that gets the number of surveys submitted for a park based on parkcode
	@Override
	public int getSurveyCountByPark(String parkCode) {
		String surveyCount = "SELECT COUNT(*) FROM survey_result WHERE parkcode = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(surveyCount, parkCode);
		
		int count = 0;
		while (results.next()) {
			count = results.getInt("count");
		}
		return count;
	}
	
	// Method that gets the next available id based on the highest surveyid stored in npgeek database
	@Override
	public int getNextId() {
		String surveyCount = "SELECT COUNT(*) FROM survey_result";
		SqlRowSet results = jdbcTemplate.queryForRowSet(surveyCount);
		String maxId = "SELECT surveyid FROM survey_result ORDER BY surveyid DESC LIMIT 1";
		SqlRowSet recentId = jdbcTemplate.queryForRowSet(maxId);
		
		int id=0;
		
		// If no surveys are present in the database, assign surveyid = 1
		while (results.next()) {
			if (results.getInt("count") == 0) {
				return 1;
			}
		}
		
		// Else, assign the surveyid = max surveyid + 1
		while (recentId.next()) {
			id = recentId.getInt("surveyid") +1;
		}
		return id;
	}
	
}
