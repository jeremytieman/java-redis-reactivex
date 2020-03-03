package com.codexmachina.javaredisreactivex;

public class RedisPubSubMessage {

	private final boolean isPong;
	private final String channel;
    private final String message;
    
	public RedisPubSubMessage(String channel, String message) {
		this.channel = channel;
		this.message = message;
		isPong = false;
	}
    
    public RedisPubSubMessage(boolean pong) {
		this.isPong = pong;
		channel = null;
		message = null;
	}
	
	public boolean isPong() {
		return isPong;
    }
    
	public String getChannel() {
		return channel;
    }
    
	public String getMessage() {
		return message;
    }
    
	@Override
	public String toString() {
		return "RedisPubSubMessage [channel=" + channel + ", message=" + message + "]";
	}	
}