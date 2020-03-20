package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.npgeek.JdbcParkDao;
import com.techelevator.npgeek.JdbcSurveyResultDao;
import com.techelevator.npgeek.JdbcWeatherDao;
import com.techelevator.npgeek.Park;
import com.techelevator.npgeek.SurveyResult;
import com.techelevator.npgeek.Weather;

public class DAOIntegrationTest {

	/*
	 * Using this particular implementation of DataSource so that every database
	 * interaction is part of the same database session and hence the same database
	 * transaction
	 */
	private static SingleConnectionDataSource dataSource;
	private JdbcParkDao parkDAO;
	private JdbcWeatherDao weatherDAO;
	private JdbcSurveyResultDao surveyResultsDAO;

	/*
	 * Before any tests are run, this method initializes the datasource for testing.
	 */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/npgeek");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/*
		 * The following line disables autocommit for connections returned by this
		 * DataSource. This allows us to rollback any changes after each test
		 */
		dataSource.setAutoCommit(false);
	}

	/*
	 * After all tests have finished running, this method will close the DataSource
	 */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parkDAO = new JdbcParkDao(dataSource);
		weatherDAO = new JdbcWeatherDao(dataSource);
		surveyResultsDAO = new JdbcSurveyResultDao(dataSource);

	}

	/*
	 * After each test, we rollback any changes that were made to the database so
	 * that everything is clean for the next test
	 */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	/*
	 * This method provides access to the DataSource for subclasses so that they can
	 * instantiate a DAO for testing
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	@Test
	public void test_that_get_all_parks_returns_list_of_parks() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Park> parkList = parkDAO.getAllParks();
		String sql = "SELECT COUNT(*) FROM park";
		SqlRowSet numberOfParks = jdbcTemplate.queryForRowSet(sql);
		int countParks = 0;
		while (numberOfParks.next()) {
			countParks = numberOfParks.getInt("count");

		}
		assertEquals(countParks, parkList.size());
	}

	@Test
	public void test_that_get_park_by_code_returns_park() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO park (parkcode, parkname, state, acreage, elevationinfeet, "
				+ "milesoftrail, numberofcampsites, climate, yearfounded, annualvisitorcount, "
				+ "inspirationalquote, inspirationalquotesource, parkdescription, entryfee, numberofanimalspecies) "
				+ "VALUES ('AAA', 'Cedar Point', 'OH', 1000, 0, 50, 200, 'Temperate', 1985, 100000000, "
				+ "'Life is a rollar coaster -- ride it', 'Andorra', "
				+ "'The only place in Ohio that Michiganders like', 50, 1)";
		jdbcTemplate.update(sql);
		
		Park park = parkDAO.getParkByCode("AAA");
		assertNotNull(park);
		assertTrue(park.getParkCode().toUpperCase().equals("AAA"));
		assertTrue(park.getParkName().equals("Cedar Point"));
		assertTrue(park.getState().equals("OH"));
		assertEquals(1000, park.getAcreage());
		assertEquals(0, park.getElevation());
		assertEquals(50, park.getMilesOfTrail(), 0);
		assertEquals(200, park.getNumberOfCampsites());
		assertTrue(park.getClimate().equals("Temperate"));
		assertEquals(1985, park.getYearFounded());
		assertEquals(100000000, park.getAnnualVisitorCount());
		assertTrue(park.getQuote().equals("Life is a rollar coaster -- ride it"));
		assertTrue(park.getQuoteSource().equals("Andorra"));
		assertTrue(park.getDescription().equals("The only place in Ohio that Michiganders like"));
		assertEquals(50, park.getEntryFee());
		assertEquals(1, park.getNumberOfSpecies());
	}

	@Test
	public void test_that_parks_order_by_number_of_votes() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO park (parkcode, parkname, state, acreage, elevationinfeet, "
				+ "milesoftrail, numberofcampsites, climate, yearfounded, annualvisitorcount, "
				+ "inspirationalquote, inspirationalquotesource, parkdescription, entryfee, numberofanimalspecies) "
				+ "VALUES ('AAA', 'Cedar Point', 'OH', 1000, 0, 50, 200, 'Temperate', 1985, 100000000, "
				+ "'Life is a rollar coaster -- ride it', 'Andorra', "
				+ "'The only place in Ohio that Michiganders like', 50, 1)";
		jdbcTemplate.update(sql);
		
		Park newPark = parkDAO.getParkByCode("AAA");
		
		for (int i = 0; i < 5; i++) {
			SurveyResult survey = new SurveyResult();
			survey.setSurveyId(surveyResultsDAO.getNextId());
			survey.setParkCode(newPark.getParkCode().toUpperCase());
			survey.setEmailAddress("notfromohio@gmail.com");
			survey.setState("MI");
			survey.setActivityLevel("Sendentary");
			surveyResultsDAO.createSurvey(survey);
		}
		
		List<Park> parkList = parkDAO.orderedParksByVote();
		List<Integer> counts = new ArrayList<>();

		for (Park park : parkList) {
			int x = surveyResultsDAO.getSurveyCountByPark(park.getParkCode().toUpperCase());
			counts.add(x);
		}
		assertTrue(parkList.size() == counts.size());

		for (int i = 0; i < counts.size() - 2; i++) {
			assertTrue(counts.get(i) >= counts.get(i + 1));
		}
	}
	 
	@Test
	public void test_that_weather_is_returned_by_parkcode() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO park (parkcode, parkname, state, acreage, elevationinfeet, "
				+ "milesoftrail, numberofcampsites, climate, yearfounded, annualvisitorcount, "
				+ "inspirationalquote, inspirationalquotesource, parkdescription, entryfee, numberofanimalspecies) "
				+ "VALUES ('AAA', 'Cedar Point', 'OH', 1000, 0, 50, 200, 'Temperate', 1985, 100000000, "
				+ "'Life is a rollar coaster -- ride it', 'Andorra', "
				+ "'The only place in Ohio that Michiganders like', 50, 1)";
		jdbcTemplate.update(sql);
		
		Park park = parkDAO.getParkByCode("AAA");
		park.setParkCode(park.getParkCode().toUpperCase());
		
		Weather weather = new Weather();
		weather.setParkCode(park.getParkCode());
		weather.setLow(16);
		weather.setForecast("sunny");
		weather.setHigh(88);
		for (int i = 1; i < 6; i++) {
			weather.setFiveDayForecastValue(i);
			String sql2 = "INSERT INTO weather (parkcode, fivedayforecastvalue, low, high, forecast) "
					+ "VALUES (?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql2, weather.getParkCode(), weather.getFiveDayForecastValue(), weather.getLow(),
					weather.getHigh(), weather.getForecast());
		}
		
		List <Weather> weatherList = weatherDAO.getWeatherByParkCode(park.getParkCode());
		assertEquals(5, weatherList.size());
		
	}
	
	@Test
	public void test_that_sunny_and_under_20_over_75_degrees_returns_sunblock_water_coldwarning_layers() {
		Weather weather = new Weather();
		weather.setLow(16);
		weather.setForecast("sunny");
		weather.setHigh(88);
		String output = weatherDAO.weatherAdvisory(weather);
		String expected = "Lots of sun expected; make sure to bring sunblock. It's gonna be hot: bring an extra gallon of water! "
				+ "Prepare for low temperatures today!  Avoid staying outside for too long and cover up! Make sure to wear "
				+ "breathable layers for today's variable temperatures. ";
		assertTrue(expected.equals(output));
	}
	
	@Test
	public void test_that_survey_posts_to_database() {
		int preId = surveyResultsDAO.getNextId();
		
		SurveyResult survey = new SurveyResult();
		survey.setSurveyId(preId);
		survey.setParkCode("AAA");
		survey.setEmailAddress("integrationtest@dao.com");
		survey.setState("MI");
		survey.setActivityLevel("Inactive");
		surveyResultsDAO.createSurvey(survey);
		int postId = surveyResultsDAO.getNextId();
		
		assertEquals(preId + 1, postId);
	}
	
	@Test
	public void test_that_survey_count_by_park_returns_park_count_of_surveys() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO park (parkcode, parkname, state, acreage, elevationinfeet, "
				+ "milesoftrail, numberofcampsites, climate, yearfounded, annualvisitorcount, "
				+ "inspirationalquote, inspirationalquotesource, parkdescription, entryfee, numberofanimalspecies) "
				+ "VALUES ('AAA', 'Cedar Point', 'OH', 1000, 0, 50, 200, 'Temperate', 1985, 100000000, "
				+ "'Life is a rollar coaster -- ride it', 'Andorra', "
				+ "'The only place in Ohio that Michiganders like', 50, 1)";
		jdbcTemplate.update(sql);
		
		for (int i = 0; i < 5; i++) {
			SurveyResult survey = new SurveyResult();
			survey.setSurveyId(surveyResultsDAO.getNextId());
			survey.setParkCode("AAA");
			survey.setEmailAddress("notfromohio@gmail.com");
			survey.setState("MI");
			survey.setActivityLevel("Sendentary");
			surveyResultsDAO.createSurvey(survey);
		}
		
		int actual = surveyResultsDAO.getSurveyCountByPark("AAA");
		assertEquals(5, actual);
	}
	

}
