package com.codexmachina.javaredisreactivex;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPubSub;

public class SendMessageServlet extends HttpServlet {

	private static final long serialVersionUID = -4751096228274971485L;
	private static final String jsp = "/WEB-INF/jsp/sendMessage.jsp";
	private Jedis jedis;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(jsp);
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String channel = request.getParameter("channel");
		String message = request.getParameter("message");
		jedis.publish(channel, message);
		request.setAttribute("jedis", "The message has successfully been published.");
		RequestDispatcher rd = request.getRequestDispatcher(jsp);
		rd.forward(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		jedis = new Jedis("localhost");
		System.out.println("Servlet " + this.getServletName() + " has started");
		System.out.println("Connected to Redis: " + jedis.ping());
	}
	
	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
}