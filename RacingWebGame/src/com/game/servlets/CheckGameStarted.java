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
 * Servlet implementation class CheckGameStarted
 * This class is responsible for first client request.
 * It validates if the game already started or a new player can join waiting list.
 * If the game started, client will be able to watch the game during his wait for new round.
 */
@WebServlet("/CheckGameStarted")
public class CheckGameStarted extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckGameStarted() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");  
	    response.setCharacterEncoding("UTF-8"); 
	    GameInterface game = new GameController();
		response.getWriter().write(game.isGameStarted());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
