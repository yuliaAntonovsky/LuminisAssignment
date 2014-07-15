/**
 * 
 */
package com.game.controller;

import java.util.Random;

import com.game.constants.JsonParseConsts;
import com.game.interfaces.GameInterface;
import com.game.model.Configurations;
import com.game.model.Player;
import com.game.model.TheGame;

/**
 * @author Yulia
 * 
 * GameController - this class implements GameInterface and used by servlets to manage the game.
 * It rely on to model class Singelton TheGame, which contains all information about the game.
 *
 */
public class GameController implements GameInterface {

	public String isGameStarted() {
		synchronized (TheGame.class) {
			if ( TheGame.getInstance().isFinished() ) {
				TheGame.getInstance().completeTheGame();
			}
		}
		String result = new String();
		try {
			result = TheGame.getInstance().isStartedJson();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonParseConsts.ERROR;
		}
		return result;
	}

	public String loginToGame(String playerName) {
		String result = new String();
		try {
			if ( playerName != null && !TheGame.getInstance().isStarted() ) {
				synchronized (TheGame.class) {
					int numOfPlayers = TheGame.getInstance().getNumberOfPlayers();
					if ( numOfPlayers < Configurations.MaxNumberOfPlayers ) {
						Random rand = new Random(1000);
					    int randomNum = rand.nextInt();
						String playerId = playerName + numOfPlayers + randomNum;
						Player newPlayer = new Player(playerName, playerId, numOfPlayers);
						TheGame.getInstance().addNewPlayer(newPlayer);
						result = TheGame.getInstance().loginJson(playerId, numOfPlayers);
					}
				}
			} else if ( TheGame.getInstance().isStarted() ) {
				result = TheGame.getInstance().loginJson(JsonParseConsts.EMPTY_PLAYER, JsonParseConsts.EMPTY_PLAYER_ORDER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonParseConsts.ERROR;
		}
		return result;
	}

	public String getGameNextStep(String playerId, String command, String groundColor) {
		String result = new String();
		try {
			if ( TheGame.getInstance().isOver() ) {
				result = TheGame.getInstance().theGameResultsToJson();
				return result;
			}
			synchronized (TheGame.class) {
				if (! TheGame.getInstance().isStarted() ) {
						result = TheGame.getInstance().startGame(playerId);
						return result;
				}
			}
			if ( playerId == null || playerId.length() < 1 ) {
				playerId = JsonParseConsts.EMPTY_PLAYER;
			}
			String res = TheGame.getInstance().performCommand(playerId, command, groundColor);
			result = TheGame.getInstance().theGameToJson(playerId, res); 
		} catch (Exception e) {
			e.printStackTrace();
			return JsonParseConsts.ERROR;
		}
		return result;
	}
	
	

}
