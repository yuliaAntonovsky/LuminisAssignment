/**
 * 
 */
package com.game.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.game.interfaces.LoggingInterface;
import com.game.model.TheGame;

/**
 * @author Yulia
 *
 */
public class GameLogger implements LoggingInterface {

	/* (non-Javadoc)
	 * @see com.game.interfaces.LoggingInterface#getAllGamesResults()
	 */
	@Override
	public String getAllGamesResults() {
		// TODO Auto-generated method stub
		return null;
	}

	public void configLogging(String fileSystemUrl) {
		String loggingfileName = "racingwebgame" + System.currentTimeMillis();
    	File dir = new File(fileSystemUrl);
    	if ( dir.exists() || dir.mkdirs() ) {
	    	try {
	    		File f = new File(fileSystemUrl + "/" + loggingfileName);
				f.createNewFile();
				TheGame.getInstance().setLogFile(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}

	@Override
	public void addNewGameResults(String result, File file) {
		if ( file == null || !file.exists() || result == null || result.length() < 1 ) return;
		FileWriter fw = null;
		try {
		    fw = new FileWriter(file.getPath(), true);
		    fw.write(result + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( fw != null)
				try {
					fw.close();
				} catch (IOException e) {
				}
		}
	}

}
