package com.codexmachina.javaredisreactivex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPubSub;

public class RedisPubSubListener extends JedisPubSub {

    private boolean isPattern;
    private Map<String, List<String>> messages;
    
    public RedisPubSubListener(Map<String, List<String>> messages, boolean isPattern) {
        this.messages = messages;
        this.isPattern = isPattern;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!messages.containsKey(channel)) {
            messages.put(channel, new ArrayList<String>());
        }

        List<String> list = messages.get(channel);
        list.add(message);
        System.out.println( "Received redis pubsub message for channel: {" + channel + "}, msg: {" + message + "}" );
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        if (!messages.containsKey(channel)) {
            messages.put(channel, new ArrayList<String>());
        }

        List<String> l = messages.get(channel);
        l.add(message);
        System.out.println( "Received redis pubsub message for channel: {" + channel + "}, msg: {" + message + "}" );
        System.out.println( "Received redis pubsub message for Pattern: {" + pattern + "}, Channel: {" + channel + "}, Msg: {" + message + "}" );
    }

    public void close() {
        if(isSubscribed()) {
            try {
                if(isPattern) {
                    punsubscribe();
                } else {
                    unsubscribe();
                }
            } catch (Exception e) {
                System.out.println("Failed to Pattern Unsubscribe from all Patterns");
            }
        }
    }
}