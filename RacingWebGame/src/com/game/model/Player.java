/**
 * 
 */
package com.game.model;

import com.game.constants.JsonParseConsts;
import com.game.constants.LogicalConsts;
import com.game.constants.MessagesConsts;

/**
 * @author Yulia
 *
 */
public class Player {
	
	private String name;
	private String playerId;
	private int orderNum;
	private long finishTime;
	private int xCoordinate;
	private int yCoordinate;
	private int rotation;
	private LogicalConsts.Speed speed;
	private LogicalConsts.State state;
	private int numOfPassedLaps;
	private LogicalConsts.Quarter quarter;
	private LogicalConsts.Quarter prevQuarter;
	private boolean isUpdated;
	private int place;
	
	public Player(String name, String playerId, int orderNum) {
		super();
		this.name = name;
		this.playerId = playerId;
		this.orderNum = orderNum;
		this.xCoordinate = LogicalConsts.START_X;
		this.yCoordinate = LogicalConsts.START_Y;
		this.rotation = LogicalConsts.START_ROTATION;
		this.speed = LogicalConsts.Speed.STOP;
		this.state = LogicalConsts.State.WAIT_TO_START;
		this.numOfPassedLaps = 0;
		this.quarter = LogicalConsts.Quarter.getQuater(xCoordinate, yCoordinate);
		this.prevQuarter = this.quarter.getPrevQuater();
		this.isUpdated = true;
	}
	
	public String confirmStep(String commandCode, String groundColor) {
		if ( groundColor.equals("" + Configurations.WallRedCode) ) {
			this.state = LogicalConsts.State.CRASHED;
			this.isUpdated = false;
			return MessagesConsts.CRASH_MSG;
		}
		if ( groundColor.equals("" + Configurations.GrassRedCode) && this.speed.equals(LogicalConsts.Speed.FAST)) {
			this.speed = LogicalConsts.Speed.SLOW;
		}
		LogicalConsts.Command cmd = LogicalConsts.Command.getCommandBYCode(commandCode);
		if ( commandCode.length() > 0 && cmd == null ) return MessagesConsts.WRONG_COMMAND_MSG;
		else if ( cmd != null ) {
			switch (cmd) {
				case LEFT:
					this.rotation = ( 360 + this.rotation - Configurations.DegreeStep) % 360;
					break;
				case ACCELERATE:
					 this.speed = this.speed.accelerate(groundColor, "" + Configurations.GrassRedCode);
					break;
				case RIGHT:
					this.rotation = ( this.rotation + Configurations.DegreeStep) % 360;
					break;
				case DECELERATE:
					this.speed = this.speed.decelerate();
					break;
				default:
					break;
			}
		}
		return countNewCoordinates();
	}
	
	private String countNewCoordinates() {
		String msg = "";
		int step = Configurations.PixelsStep * this.speed.getFactor();
		this.xCoordinate += Math.cos( this.rotation * Math.PI/180) * step;
		this.yCoordinate += Math.sin( this.rotation * Math.PI/180) * step;
		LogicalConsts.Quarter newQuarter = LogicalConsts.Quarter.getQuater(xCoordinate, yCoordinate);
		if ( this.quarter.equals(LogicalConsts.Quarter.FOUR) && 
			 newQuarter.equals(LogicalConsts.Quarter.ONE) &&
			 this.prevQuarter.equals(LogicalConsts.Quarter.THREE)) {
			numOfPassedLaps++;
			if ( numOfPassedLaps == Configurations.NumberOfLaps ) {
				this.finishTime = System.currentTimeMillis();
				this.isUpdated = false;
				this.place = TheGame.getInstance().getNumOfFinished() + 1;
				msg = MessagesConsts.FINISH_MSG.replaceAll("#", "" + this.place);
				msg = msg.replaceAll("$", "" +  TheGame.getInstance().getNumberOfPlayers());
				this.state = LogicalConsts.State.FINISHED;
			}
		} 
		if ( ! newQuarter.equals(this.quarter) ) {
			if ( ! newQuarter.isRightOrder(this.quarter) ) {
				msg = MessagesConsts.WRONG_DIRECTION_MSG;
			}
			this.prevQuarter = this.quarter;
			this.quarter = newQuarter;
			
		}
		return msg;
	}
	
	public String toJson() {
		String reply = new String();
		reply += "{\"" + JsonParseConsts.ORDER_NUM + "\"" + ":" + "\"" + this.getOrderNum() + "\"" + "," 
				+ "\"" + JsonParseConsts.PLAYER_NAME + "\"" + ":" + "\"" + this.getName() + "\"" + ","
				+ "\"" + JsonParseConsts.STATE + "\"" + ":" + "\"" + this.getState() + "\"" + ","
				+ "\"" + JsonParseConsts.ROTATION + "\"" + ":" + "\"" + this.getRotation() + "\"" + ","
				+ "\"" + JsonParseConsts.X_COORDINATE + "\"" + ":" + "\"" + this.getxCoordinate() + "\"" + ","
				+ "\"" + JsonParseConsts.Y_COORDINATE + "\"" + ":" + "\"" + this.getyCoordinate() + "\"},";
		return reply;
	}
	
	public String resToJson() {
		String reply = new String();
		reply += "{\"" + JsonParseConsts.PLAYER_NAME + "\"" + ":" + "\"" + this.getName() + "\"" + ","
				+ "\"" + JsonParseConsts.STATE + "\"" + ":" + "\"" + this.getState() + "\"" + ","
				+ "\"" + JsonParseConsts.PLACE + "\"" + ":" + "\"" + this.getPlace() + "\"" + ",";
		long runningTime = 0;
		if ( this.getFinishTime() > 0 ) {
			runningTime = (this.getFinishTime() - TheGame.getInstance().getGameStartTime()) / 1000;
		}
		reply += "\"" + JsonParseConsts.FINISH_TIME + "\"" + ":" + "\"" + runningTime + "\"},";
		return reply;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime() {
		this.finishTime = System.currentTimeMillis();;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public LogicalConsts.State getState() {
		return state;
	}

	public void setState(LogicalConsts.State state) {
		this.state = state;
	}

	public boolean isUpdated() {
		return isUpdated;
	}
	
	public void setUpdated() {
		this.isUpdated = true;
	}

	public int getPlace() {
		return place;
	}
	

}
