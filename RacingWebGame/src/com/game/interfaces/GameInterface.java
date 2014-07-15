/**
 * 
 */
package com.game.interfaces;

/**
 * @author Yulia
 * 
 * GameInterface - contains all commands that needed to manage the game
 *
 */
public interface GameInterface {
	
	/* Returns Json string that contains information about game state 
	 * If game started it contains information needed to display current situation on game field
	 * otherwise it just contains indicator that game is still not started*/
	public String isGameStarted();

	/* Adds new player to game waiting list
	 * Returns Json string that contains information about game state and new player details
	 * If game started it contains information needed to display current situation on game field
	 * otherwise it just contains indicator that game is still not started*/
	public String loginToGame(String playerName);
	
	/* Counts next game screen
	 * Returns Json string that contains information about game state and all players (that still not finished the game) locations
	 * If game started it contains information needed to display current situation on game field
	 * if game finished it contains the results
	 * otherwise it just contains indicator that game is still not started*/
	public String getGameNextStep(String playerId, String command, String groundColor);

}
