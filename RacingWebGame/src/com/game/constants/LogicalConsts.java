/**
 * 
 */
package com.game.constants;

/**
 * @author Yulia
 *
 */
public class LogicalConsts {
	
	public static final int SIZE_X = 1000;
	public static final int SIZE_Y = 526;
	public static final int START_X = 140;
	public static final int START_Y = 261;
	public static final int START_ROTATION = 270;
	
	public static enum State {WAIT_TO_START, PLAY, FINISHED, CRASHED, WATCHER}
	
	public static enum Command {
		LEFT("37"), ACCELERATE("38"), RIGHT("39"), DECELERATE("40");
		
		private String code;
		
		Command (String code) {
			this.code = code;
		}
				
		public String getCode() {
			return code;
		}
		
		public static Command getCommandBYCode(String code) {
			for (Command c: Command.values()) {
				if ( c.getCode().equals(code) ) {
					return c;
				}
			}
			return null;
		}
	}
	
	public static enum Speed {
		STOP(0), SLOW(1), FAST(2);
		
		private int factor;
		
		Speed (int factor) {
			this.factor = factor;
		}

		public int getFactor() {
			return factor;
		}
		
		public Speed getSpeedByGroundType (String groundColor, String grassRedCode) {
			if ( this.equals(Speed.FAST) && groundColor.equals(grassRedCode) ) return Speed.SLOW;
			return this;
		}
		
		public Speed accelerate (String groundColor, String grassRedCode) {
			if ( this.equals(Speed.SLOW) && !groundColor.equals(grassRedCode)) return Speed.FAST;
			if ( this.equals(Speed.STOP) ) return Speed.SLOW;
			return this;
		}
		
		public Speed decelerate () {
			if ( this.equals(Speed.FAST) ) return Speed.SLOW;
			if ( this.equals(Speed.SLOW) ) return Speed.STOP;
			return this;
		}
		
	};
	
	public static enum Quarter {
		ONE(1), TWO(2), THREE(3), FOUR(4);
		
		int value;
		
		Quarter (int value) {
			this.value = value;
		}
		
		public boolean isRightOrder( Quarter prevQuater) {
			if ( prevQuater.equals(FOUR) && this.equals(ONE) ) return true;
			return (prevQuater.value < this.value);
		}
		
		public static Quarter getQuater(int x, int y) {
			if ( x <= SIZE_X/2 && y <= SIZE_Y/2) return ONE;
			if ( x >= SIZE_X/2 && y <= SIZE_Y/2) return TWO;
			if ( x >= SIZE_X/2 && y >= SIZE_Y/2) return THREE;
			if ( x <= SIZE_X/2 && y >= SIZE_Y/2) return FOUR;
			return null;
		}
		
		public Quarter getPrevQuater() {
			if ( this.equals(TWO) ) return ONE;
			if ( this.equals(THREE) ) return TWO;
			if ( this.equals(FOUR) ) return THREE;
			if ( this.equals(ONE) ) return FOUR;
			return null;
		}
	};
}
