package com.codexmachina.javaredisreactivex;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;

public class ListMessagesServlet extends HttpServlet {

	private static final long serialVersionUID = -4751096228274971485L;

	private final Map<String, List<String>> messages = new HashMap<>();
	private final JedisPool jedisPool = new JedisPool();
	private Jedis jedis = null;
	private RedisPubSubListener listener = null;

	@Override
	protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("Hello World!");
	}
	
	@Override
	public void init() throws ServletException {
		jedis = jedisPool.getResource();
		listener = new RedisPubSubListener(messages, true);

		new Thread(new Runnable() {
		
			@Override
			public void run() {
				try {
					System.out.println("Subscribing to Redis...");
					jedis.psubscribe(listener, "fc:*", "172.20.56.106:*");
					jedis.close();
					System.out.println("Subscribing to Redis succeeded.");
				}
				catch(Exception e) {
					System.out.println("Subscribing to Redis failed");
				}
			}
		}).start();

		System.out.println("Servlet " + this.getServletName() + " has started");
	}
	
	@Override
	public void destroy() {
		listener.close();
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
}