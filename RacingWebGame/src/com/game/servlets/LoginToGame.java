package com.game.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.game.controller.GameController;
import com.game.interfaces.GameInterface;

/**
 * Servlet implementation class LoginToGame
 * 
 * This class is responsible for logging new player and adding it to wait list.
 * If the game already started, the client is able to watch it, until new round of registration starts.
 *  
 */
@WebServlet("/LoginToGame")
public class LoginToGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LoginToGame() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String playerName = request.getParameter("playerName");
		response.setContentType("application/json");  
	    response.setCharacterEncoding("UTF-8"); 
	    GameInterface game = new GameController();
	    response.getWriter().write(game.loginToGame(playerName));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
