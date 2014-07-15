package com.game.interfaces;

import java.io.File;


/**
 * @author Yulia
 * 
 * LoggingInterface - contains all commands that needed to manage the game results log
 *
 */
public interface LoggingInterface {
	
	/* Returns Json string that contains results of all games 
	 * If the log is empty returns empty string*/
	public String getAllGamesResults();
	
	/* Perform all manipulations that needs for logging games results*/
	public void configLogging(String url);
	
	/* Save game results to log file*/
	public void addNewGameResults(String result, File file);

}
