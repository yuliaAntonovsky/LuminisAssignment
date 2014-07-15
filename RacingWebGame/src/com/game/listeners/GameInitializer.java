package com.game.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.game.constants.JsonParseConsts;
import com.game.controller.GameConfigurator;
import com.game.controller.GameLogger;
import com.game.interfaces.ConfiguratorInterface;
import com.game.interfaces.LoggingInterface;

/**
 * Application Lifecycle Listener implementation class GameInitializer
 *
 */
@WebListener
public class GameInitializer implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public GameInitializer() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
    	String configurationFileName = (String) sce.getServletContext().getInitParameter(JsonParseConsts.configurationFileName);
    	String fileSystemUrl = (String) sce.getServletContext().getInitParameter(JsonParseConsts.fileSystemUrl);
    	LoggingInterface log = new GameLogger();
    	log.configLogging(fileSystemUrl);
    	ConfiguratorInterface configurator = new GameConfigurator(fileSystemUrl + "/" + configurationFileName);
    	configurator.loadConfigurations();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
