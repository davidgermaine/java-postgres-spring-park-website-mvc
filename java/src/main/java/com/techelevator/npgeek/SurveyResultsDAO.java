package com.techelevator.npgeek;

public interface SurveyResultsDAO {
	
	public void createSurvey(SurveyResult result);
	
	public int getSurveyCountByPark(String parkCode);
	
	public int getNextId();
	
}
