/**
 * 
 */
package com.game.model;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import com.game.constants.JsonParseConsts;
import com.game.constants.LogicalConsts;
import com.game.constants.MessagesConsts;
import com.game.controller.GameLogger;
import com.game.interfaces.LoggingInterface;

/**
 * @author Yulia
 *
 */
public class TheGame {
	
	private ConcurrentHashMap<String, Player> playersMap;	
	private long lastPersonJoinedTime = 0;
	private long gameStartTime = 0;
	private long winnerTime = 0;
	private long gameFinishTime = 0;
	private int numOfPlaying = 0;
	private int numOfFinished = 0;
	private boolean isStarted = false;
	private File logFile = null;
	private String result = "";
	
	private static TheGame instance = new TheGame();
	
	public static synchronized TheGame getInstance()
	{
		return instance;
	}
	
	private TheGame() {
		super();
		playersMap = new ConcurrentHashMap<String, Player>();
	}
	
	public String startGame(String playerId) {
		String result = new String();
		long crntTime = System.currentTimeMillis();
		if ( !isStarted && gameStartTime == 0 &&
			 playersMap.size() >= Configurations.MinNumberOfPlayers && 
			 crntTime - lastPersonJoinedTime > Configurations.MiliSecStartDelay ) {
			isStarted = true;
			gameStartTime = crntTime;
			playersMap.get(playerId).setState(LogicalConsts.State.PLAY);
			numOfPlaying ++;
			result = theGameStartToJson();
			
		} else {
			result = "{\"" + JsonParseConsts.IS_STARTED + "\" : \"" + JsonParseConsts.NO + "\"}";
		}
		return result;
	}
	
	public void stopGame() {
		isStarted = false;
		gameFinishTime = System.currentTimeMillis();
	}

	public boolean isStarted() {
		return isStarted;
	}
	
	public boolean isFinished() {
		return (gameFinishTime > 0);
	}

	public int getNumberOfPlayers() {
		return playersMap.size();
	}
	
	public void addNewPlayer(Player newPlayer) {
		playersMap.put(newPlayer.getPlayerId(), newPlayer);
		lastPersonJoinedTime = System.currentTimeMillis();
	}
	
	public String theGameToJson(String playerId, String res) {
		String reply = new String();
		reply += "{" + "\"" + JsonParseConsts.IS_STARTED  + "\"" + ":" + "\"" + JsonParseConsts.YES + "\"" + ",";
		if ( playerId.equals(JsonParseConsts.EMPTY_PLAYER) ) {
			reply += emptyPlayerJson();
		} else {
			Player player = playersMap.get(playerId);
			if ( player.getState().equals(LogicalConsts.State.WAIT_TO_START) ) {
				player.setState(LogicalConsts.State.PLAY);
				numOfPlaying ++;
				return theGameStartToJson();
			} 
			reply +=  "\"" + JsonParseConsts.PLAYER_ID + "\"" + ":" + "\"" + playerId + "\"" + "," 
						 + "\"" + JsonParseConsts.COMMENT + "\"" + ":" + "\"" + res + "\"" + ","
						 + "\"" + JsonParseConsts.STATE + "\"" + ":" + "\"" + player.getState().name() + "\"" + ","
						 + "\"" + JsonParseConsts.X_COORDINATE + "\"" + ":" + "\"" + player.getxCoordinate() + "\"" + ","
						 + "\"" + JsonParseConsts.Y_COORDINATE + "\"" + ":" + "\"" + player.getyCoordinate() + "\"" + ","
						 + "\"" + JsonParseConsts.ROTATION + "\"" + ":" + "\"" + player.getRotation() + "\"";
		}
		if ( numOfPlaying > 0 ) {
			reply += "," + "\"" + JsonParseConsts.PLAYERS + "\"" + ":" + playersMapToJson(); 
		}
		reply += "}";
		
		return reply;
	}
	
	private String emptyPlayerJson() {
		String reply = new String();
		reply +=  "\"" + JsonParseConsts.PLAYER_ID + "\"" + ":" + "\"" + JsonParseConsts.EMPTY_PLAYER + "\"" + "," 
				 + "\"" + JsonParseConsts.COMMENT + "\"" + ":" + "\"" + "" + "\"" + ","
				 + "\"" + JsonParseConsts.STATE + "\"" + ":" + "\"" + LogicalConsts.State.WATCHER + "\"" + ","
				 + "\"" + JsonParseConsts.X_COORDINATE + "\"" + ":" + "\"" + JsonParseConsts.EMPTY_PLAYER + "\"" + ","
				 + "\"" + JsonParseConsts.Y_COORDINATE + "\"" + ":" + "\"" + JsonParseConsts.EMPTY_PLAYER + "\"" + ","
				 + "\"" + JsonParseConsts.ROTATION + "\"" + ":" + "\"" + JsonParseConsts.EMPTY_PLAYER + "\"";
		return reply;
	}

	public String isStartedJson() {
		String jsonReply = new String();
		jsonReply += "{" + "\"" + JsonParseConsts.ANNIMATION_TIMEOUT + "\"" + ":" + "\"" + Configurations.MiliSecUpdateTime + "\"" + ","
					+ "\"" + JsonParseConsts.MAX_NUM_PLAYERS + "\"" + ":" + "\"" + Configurations.MaxNumberOfPlayers + "\"" + ",";
		if ( isStarted ) {
			jsonReply += "\"" + JsonParseConsts.PLAYERS_NUMBER + "\"" + ":" + "\"" + playersMap.size() + "\"" + ","
						+ "\"" + JsonParseConsts.PLAYERS + "\"" + ":" + playersMapToJson() + "}";
		} else {
			jsonReply += "\"" + JsonParseConsts.PLAYERS_NUMBER + "\"" + ":" + "\"0\"" + "}";
		}
		return jsonReply;
	}
	
	public String loginJson(String playerId, int orderNum) {
		String jsonReply = new String();
		jsonReply += "{" + "\"" + JsonParseConsts.PLAYER_ID + "\"" + ":" + "\"" + playerId + "\"" + ","
			 	 	+ "\"" + JsonParseConsts.ORDER_NUM +"\"" +  ":" + "\"" +  orderNum + "\"";
		if ( ! playerId.equals(JsonParseConsts.EMPTY_PLAYER) ) {
			jsonReply += ",\"" + JsonParseConsts.PLAYER_NAME +"\"" +  ":" + "\"" +  playersMap.get(playerId).getName() + "\"";
		}
		jsonReply += "}";
		return jsonReply;
	}

	public String performCommand(String playerId, String command, String groundColor) {
		if ( !playerId.equals(JsonParseConsts.EMPTY_PLAYER) &&
			 command != null && groundColor != null &&
			 playersMap.get(playerId).getState().equals(LogicalConsts.State.PLAY) ) {
			return playersMap.get(playerId).confirmStep(command, groundColor);
		}
		return "";
	}
	
	public void decreaseNumOfPlaying() {
		numOfPlaying--;
	}
	
	public void incrementNumOfFinished(long finishTime) {
		if ( numOfFinished == 0 ) {
			winnerTime = finishTime;
		}
		numOfPlaying--;
		numOfFinished++;
	}

	private String theGameStartToJson() {
		String jsonReply = new String();
		jsonReply += "{" + "\"" + JsonParseConsts.IS_STARTED  + "\"" + ":" + "\"" + JsonParseConsts.YES + "\"" + ","
					+ "\"" + JsonParseConsts.MESSAGE  + "\"" + ":" + "\"" + MessagesConsts.START_MSG + "\"" + "}";
		return jsonReply;
	}
	
	private String playersMapToJson() {
		String reply = new String();
		reply += "[";
		for (String crntId: playersMap.keySet()) {
			Player crntPlayer = playersMap.get(crntId);
			if ( crntPlayer.getState().equals(LogicalConsts.State.PLAY) || !crntPlayer.isUpdated() ) {
				reply += crntPlayer.toJson();
				if ( crntPlayer.getState().equals(LogicalConsts.State.CRASHED) ) {
					decreaseNumOfPlaying();
				} 
				if ( crntPlayer.getState().equals(LogicalConsts.State.FINISHED) ) {
					incrementNumOfFinished(crntPlayer.getFinishTime());
				} 
				crntPlayer.setUpdated();
			}
		}
		reply = reply.substring(0, reply.length() - 1);
		reply += "]";	
		return reply;
	}

	public int getNumOfFinished() {
		return numOfFinished;
	}

	public boolean isOver() {
		if ( gameFinishTime > 0 ) return true;
		long crntTime = System.currentTimeMillis();
		if ( gameStartTime > 0 && ( numOfPlaying < 1 || 
			 winnerTime > 0 && crntTime > Configurations.MiliSecEndWinnerDelay + winnerTime || 
			 crntTime > Configurations.MiliSecEndDelay + gameStartTime ) ) {
			synchronized (this) {
				if ( isStarted ) {
					isStarted = false;
					gameFinishTime = crntTime;
				}
			}
			return true;
		}
		return false;
	}

	public String theGameResultsToJson() {
		if ( result.length() > 0 ) return result;
		String jsonReply = new String();
		jsonReply += "{" + "\"" + JsonParseConsts.IS_STARTED  + "\"" + ":" + "\"" + JsonParseConsts.FINISHED + "\"" + ","
					+ "\"" + JsonParseConsts.STARTED_TIME  + "\"" + ":" + "\"" + new Date(gameStartTime) + "\"" + ","
					+ "\"" + JsonParseConsts.RESULT_TIMEOUT  + "\"" + ":" + "\"" + Configurations.MiliSecResDisplay + "\"" + ","
					+ "\"" + JsonParseConsts.PLAYERS + "\"" + ":" + playersResToJson() + "}";
		result = jsonReply;
		return result;
	}

	private String playersResToJson() {
		String reply = new String();
		reply += "[";
		for (String crntId: playersMap.keySet()) {
				reply += playersMap.get(crntId).resToJson();
		}
		reply = reply.substring(0, reply.length() - 1);
		reply += "]";	
		return reply;
	}

	public void completeTheGame() {
		playersMap = new ConcurrentHashMap<String, Player>();
		lastPersonJoinedTime = 0;
		gameStartTime = 0;
		winnerTime = 0;
		gameFinishTime = 0;
		numOfPlaying = 0;
		numOfFinished = 0;
		isStarted = false;
		LoggingInterface log = new GameLogger();
		log.addNewGameResults(result, logFile);
		result = "";
	}

	public long getGameStartTime() {
		return gameStartTime;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}
	
	

}


