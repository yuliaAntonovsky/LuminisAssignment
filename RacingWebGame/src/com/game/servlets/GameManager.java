package com.game.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.game.constants.JsonParseConsts;
import com.game.controller.GameController;
import com.game.interfaces.GameInterface;

/**
 * Servlet implementation class GameManager
 * 
 * This class returns current game status including all info to display the game progress.
 */
@WebServlet("/GameManager")
public class GameManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String playerId = request.getParameter(JsonParseConsts.PLAYER_ID);
		String command = request.getParameter(JsonParseConsts.COMMAND);
		String groundColor = request.getParameter(JsonParseConsts.GROUND_COLOR);
		response.setContentType("application/json");  
	    response.setCharacterEncoding("UTF-8"); 
	    GameInterface game = new GameController();
	    response.getWriter().write(game.getGameNextStep(playerId, command, groundColor)); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
