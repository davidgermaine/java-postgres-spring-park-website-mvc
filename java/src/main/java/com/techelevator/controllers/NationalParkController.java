package com.techelevator.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techelevator.npgeek.Park;
import com.techelevator.npgeek.ParkDAO;
import com.techelevator.npgeek.SurveyResult;
import com.techelevator.npgeek.SurveyResultsDAO;
import com.techelevator.npgeek.Weather;
import com.techelevator.npgeek.WeatherDAO;

@Controller
public class NationalParkController {
	
	@Autowired
	private ParkDAO parkDao;
	@Autowired
	private WeatherDAO weatherDao;
	@Autowired
	private SurveyResultsDAO surveyResultDao;
	
	// Home page mapping
	@RequestMapping("/")
	public String displayHome(ModelMap map) {
		List<Park> parkList = parkDao.getAllParks();
		map.put("parks", parkList);
		
		return "homePage";
	}
	
	// GET method displays survey form page
	@RequestMapping(path="/surveyInput", method=RequestMethod.GET)
	public String displaySurveyForm(Model modelHolder, ModelMap map) {
		
		// Store all available parks to be used in a drop-down selection list
		List<Park> parkList = parkDao.getAllParks();
		map.put("parks", parkList);
		
		// Check if a new survey form needs to be generated
		if (!modelHolder.containsAttribute("surveyInput")) {
			modelHolder.addAttribute("surveyInput", new SurveyResult());
		}
		return "surveyInput";
	}
	
	// POST redirect method to submit survey form
	@RequestMapping(path = "/surveyInput", method = RequestMethod.POST)
	public String submitSurveyForm(@Valid @ModelAttribute("surveyInput") SurveyResult surveyFormValues,
			BindingResult result, RedirectAttributes flash) {
		
		// If form validation fails, return to form page and display validation errors
		if (result.hasErrors()) {
			flash.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "surveyInput", result);
			flash.addFlashAttribute("surveyInput", surveyFormValues);
			return "redirect:/surveyInput";
		}
		
		// Otherwise, assign the submitted form a surveyId and save it as a survey_result in npgeek database
		long id = surveyResultDao.getNextId();
		surveyFormValues.setSurveyId(id);
		surveyResultDao.createSurvey(surveyFormValues);

		// After submission, go directly to favorites page
		return "redirect:/favorites";
	}
	
	// GET method to show the current top-ranking parks ordered on the number of surveys submitted
	@RequestMapping(path="/favorites", method=RequestMethod.GET)
	public String displayFavoritesPage(ModelMap map) {
		
		// Get all parks and order them into a list based on number of votes
		List<Park> parkList = parkDao.orderedParksByVote();
		// Get number of votes for each park in the same order as the parks
		List<Integer> parkCounts = new ArrayList<>();
		for (Park park : parkList) {
			Integer number = surveyResultDao.getSurveyCountByPark(park.getParkCode());
			parkCounts.add(number);
		}
		
		// Create a map with keys of parks and values of # of votes
		// LinkedHashMap used in order to preserve ordering of parks in ranking order
		Map<Park, Integer> parkMap = new LinkedHashMap<Park, Integer>();
		for (int i = 0; i < parkList.size(); i++) {
			parkMap.put(parkList.get(i), parkCounts.get(i));
		}
		
		// Store map to be used in JSP
		map.put("map", parkMap);
		return "favorites";
	}
	
	// GET method to display detail page
	@RequestMapping(path="/detail", method=RequestMethod.GET)
	public String displayDetailPage(
			@RequestParam String parkCode,
			HttpSession session,
			ModelMap map) {
		
		// Create empty objects to be filled later
		Park park = parkDao.getParkByCode(parkCode);
		List<Weather> weather = weatherDao.getWeatherByParkCode(parkCode);
		List<String> advisories = new ArrayList<>();
		
		/* 
		 * Determine if there is an existing session. 
		 * If there is one, set the temperature scale based on the session. 
		 * If not, default to Farenheit.
		 */
		String temperature = "";
		if (session.getAttribute("temperature") == null) {
			temperature = "F";
		} else {
			temperature = (String) session.getAttribute("temperature");
		}
		
		// For each weather object in the list:
		for (Weather day : weather) {
			
			// Calculate the day's temperatures based on the set scale
			if (temperature.equals("C")) {
				int high = day.getHigh();
				high = (int)Math.round((day.getHigh() - 32) / 1.8);
				int low = day.getLow();
				low = (int)Math.round((day.getLow() - 32) / 1.8);
				
				day.setHigh(high);
				day.setLow(low);
				
			}
			
			// Print out a weather advisory based on the weather qualities
			String advisory = weatherDao.weatherAdvisory(day);
			advisories.add(advisory);
		}
		
		// Store values to be used in the JSP
		map.put("forecast", weather);
		map.put("temperature", temperature);
		map.put("advisory", advisories);
		map.put("park", park);
		
		return "detailPage";
	}
	
	// POST method to update user session to include a change in temperature scale
	@RequestMapping(path="/detail", method=RequestMethod.POST)
	public String refreshTemperatureDetails(
			@RequestParam String parkCode,
			@RequestParam String temperature,
			HttpSession session,
			ModelMap map
			) {
		
		// Create empty objects to be filled later
		Park park = parkDao.getParkByCode(parkCode);
		List<Weather> weather = weatherDao.getWeatherByParkCode(parkCode);
		List<String> advisories = new ArrayList<>();
		
		// Set a session attribute to store the user's desired scaling
		session.setAttribute("temperature", temperature);
		
		// For each day in the list:
		for (Weather day : weather) {
			
			// Calculate temperatures based on user's desired scaling
			if (temperature.equals("C")) {
				int high = day.getHigh();
				high = (int)Math.round((day.getHigh() - 32) / 1.8);
				int low = day.getLow();
				low = (int)Math.round((day.getLow() - 32) / 1.8);
				
				day.setHigh(high);
				day.setLow(low);
				
			}
			
			// Print out an advisory based on the weather's attributes
			String advisory = weatherDao.weatherAdvisory(day);
			advisories.add(advisory);
		}
		
		// Store data to be used in JSP
		map.put("forecast", weather);
		map.put("temperature", temperature);
		map.put("advisory", advisories);
		map.put("park", park);
		
		return "detailPage";
	}
	
	// Visit page mapping
	@RequestMapping("/visit")
	public String displayVisitPage() {	
		return "visit";
	}
	

}
