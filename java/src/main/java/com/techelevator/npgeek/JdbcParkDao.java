package com.techelevator.npgeek;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcParkDao implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcParkDao(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// Method to get all parks stored in database
	@Override
	public List<Park> getAllParks() {
		List <Park> parkList = new ArrayList<>();
		String sql= "SELECT * FROM park";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql);
		
		while (results.next()) {
			Park park = mapRowToPark(results);
			parkList.add(park);
		}
		return parkList;
	}

	// Method to create a park object based off of a parkcode
	@Override
	public Park getParkByCode(String parkCode) {
		// TODO Auto-generated method stub
		Park park = null;
		String sql = "SELECT * FROM park WHERE parkcode = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkCode.toUpperCase());
		
		while(results.next()) {
			park = mapRowToPark(results);
		}
		return park;
	}
	
	// Create a list of parks ordered by the number of surveys submitted for them
	// This method only retrieves parks that have AT LEAST 1 vote for them
	@Override
	public List<Park> orderedParksByVote() {
		List <Park> parkList = new ArrayList<>();
		String sql= "SELECT parkcode FROM survey_result" + 
				" GROUP BY parkcode" + 
				" ORDER BY COUNT(*) DESC, parkcode";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql);
		
		while (results.next()) {
			Park park = getParkByCode(results.getString("parkcode"));
			parkList.add(park);
		}
		return parkList;
	}
	
	// Helper method to generate a park object based on a PostgreSQL query
	private Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		park.setParkCode(results.getString("parkcode").toLowerCase());
		park.setParkName(results.getString("parkname"));
		park.setState(results.getString("state"));
		park.setAcreage(results.getLong("acreage"));
		park.setElevation(results.getInt("elevationinfeet"));
		park.setMilesOfTrail(results.getDouble("milesoftrail"));
		park.setNumberOfCampsites(results.getInt("numberofcampsites"));
		park.setClimate(results.getString("climate"));
		park.setYearFounded(results.getInt("yearfounded"));
		park.setAnnualVisitorCount(results.getLong("annualvisitorcount"));
		park.setQuote(results.getString("inspirationalquote"));
		park.setQuoteSource(results.getString("inspirationalquotesource"));
		park.setDescription(results.getString("parkdescription"));
		park.setEntryFee(results.getInt("entryfee"));
		park.setNumberOfSpecies(results.getInt("numberofanimalspecies"));
		
		return park;
	}

}
