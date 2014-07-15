/**
 * 
 */
package com.game.controller;

import java.io.File;
import java.io.FileReader;

import com.game.constants.JsonParseConsts;
import com.game.interfaces.ConfiguratorInterface;
import com.game.model.Configurations;

/**
 * @author Yulia
 *
 */
public class GameConfigurator implements ConfiguratorInterface {
	
	private String configurations;
	
	public GameConfigurator(String url) {
		File file = new File(url);
		configurations = fileToString(file);
	}
	
	public void loadConfigurations() {
		if ( configurations == null || configurations.length() < 1) return;
		Configurations.DegreeStep = getValueForParam(Configurations.DegreeStep, JsonParseConsts.DegreeStep);
		Configurations.GrassRedCode = getValueForParam(Configurations.GrassRedCode, JsonParseConsts.GrassRedCode);
		Configurations.MaxNumberOfPlayers = getValueForParam(Configurations.MaxNumberOfPlayers, JsonParseConsts.MaxNumberOfPlayers);
		Configurations.MiliSecEndDelay = getValueForParam(Configurations.MiliSecEndDelay, JsonParseConsts.MiliSecEndDelay);
		Configurations.MiliSecEndWinnerDelay = getValueForParam(Configurations.MiliSecEndWinnerDelay, JsonParseConsts.MiliSecEndWinnerDelay);
		Configurations.MiliSecResDisplay = getValueForParam(Configurations.MiliSecResDisplay, JsonParseConsts.MiliSecResDisplay);
		Configurations.MiliSecStartDelay = getValueForParam(Configurations.MiliSecStartDelay, JsonParseConsts.MiliSecStartDelay);
		Configurations.MiliSecUpdateTime = getValueForParam(Configurations.MiliSecUpdateTime, JsonParseConsts.MiliSecUpdateTime);
		Configurations.MinNumberOfPlayers = getValueForParam(Configurations.MinNumberOfPlayers, JsonParseConsts.MinNumberOfPlayers);
		Configurations.NumberOfLaps = getValueForParam(Configurations.NumberOfLaps, JsonParseConsts.NumberOfLaps);
		Configurations.PixelsStep = getValueForParam(Configurations.PixelsStep, JsonParseConsts.PixelsStep);
		Configurations.WallRedCode = getValueForParam(Configurations.WallRedCode, JsonParseConsts.WallRedCode);
	}
	
	 private int getValueForParam(int defaultValue, String paramName) {
		int indStr = configurations.indexOf(paramName);
		if ( indStr < 0 ) return defaultValue;
		indStr += paramName.length();
		if ( indStr >= configurations.length() ) return defaultValue;
		String valueStr = configurations.substring(indStr, configurations.indexOf(";", indStr)).trim();
		if ( valueStr.length() < 2 ) return defaultValue;
		valueStr = valueStr.substring(valueStr.indexOf("=") + 1).trim();
		try {
			int res = Integer.parseInt(valueStr);
			return res;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private String fileToString(File file){
		int len;
		char[] chr = new char[4096];
		final StringBuffer buffer = new StringBuffer();
		FileReader reader;
		try {
			reader = new FileReader(file);
			try {
				while ((len = reader.read(chr)) > 0) {
				buffer.append(chr, 0, len);
			}
			} finally {
				reader.close();
			}
		} catch (Exception e) {
			return "";
		}
		
		return buffer.toString();
	}

}
