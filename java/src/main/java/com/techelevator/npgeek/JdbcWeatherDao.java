package com.techelevator.npgeek;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcWeatherDao implements WeatherDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcWeatherDao(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	// Method to get all stored weather information for a park specified by parkcode
	@Override
	public List<Weather> getWeatherByParkCode(String parkCode) {
		List <Weather> weatherFiveDay = new ArrayList<>();
		
		String sql = "SELECT * FROM weather WHERE parkcode = ? ORDER BY fivedayforecastvalue ASC";
		SqlRowSet results= jdbcTemplate.queryForRowSet(sql, parkCode.toUpperCase());
		
		while(results.next()) {
			Weather weather = new Weather();
			weather.setParkCode(results.getString("parkcode"));
			weather.setFiveDayForecastValue(results.getInt("fivedayforecastvalue"));
			weather.setLow(results.getInt("low"));
			weather.setHigh(results.getInt("high"));
			weather.setForecast(results.getString("forecast"));
			
			weatherFiveDay.add(weather);
				
		}
		return weatherFiveDay;
	}
	
	// Method to generate a weather advisory string based on a weather object's attributes
	@Override
	public String weatherAdvisory(Weather weather) {
		String forecast = weather.getForecast().toLowerCase();
		String output = "";
		
		// Add forecast advisory strings based on predicted weather
		if (forecast.equals("snow")) {
			output += "Due to expected snowfall, make sure to bring snowshoes. ";
		} else if (forecast.equals("rain")) {
			output += "Due to expected rainfall, pack rain gear and wear waterproof shoes. ";
		} else if (forecast.equals("thunderstorms")) {
			output += "Due to expected thunderstorms, seek shelter and avoid hiking on exposed ridges. ";
		} else if (forecast.equals("sunny")) {
			output += "Lots of sun expected; make sure to bring sunblock. ";
		}
		
		// Add temperature warnings based on high/low temps
		if (weather.getHigh() > 75) {
			output += "It's gonna be hot: bring an extra gallon of water! ";
		}
		if (weather.getLow() < 20) {
			output += "Prepare for low temperatures today!  Avoid staying outside for too long and cover up! ";
		}
		int tempDif = weather.getHigh() - weather.getLow();
		if (tempDif > 20) {
			output += "Make sure to wear breathable layers for today's variable temperatures. ";
		}
		
		// If no extreme attributes are present, display a default message
		if (output.equals("")) {
			output += "It's a beautiful day: take only photos, leave only footprints.";
		}
		
		return output;
	}

}
