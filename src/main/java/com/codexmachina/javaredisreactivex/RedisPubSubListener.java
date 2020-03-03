package com.codexmachina.javaredisreactivex;

import io.reactivex.FlowableEmitter;

import java.util.concurrent.atomic.AtomicLong;

import redis.clients.jedis.JedisPubSub;

public class RedisPubSubListener extends JedisPubSub {

    private final FlowableEmitter<RedisPubSubMessage> emitter;
    private final boolean pattern;
    private final AtomicLong lastUpdated;
    
    public RedisPubSubListener(FlowableEmitter<RedisPubSubMessage> emitter, boolean pattern, AtomicLong lastUpdated) {
        this.emitter = emitter;
        this.pattern = pattern;
        this.lastUpdated = lastUpdated;
    }
    
    @Override
    public void onPong(String pattern) {
        this.lastUpdated.set(System.currentTimeMillis());
        emitter.onNext(new RedisPubSubMessage(true));
    }
    
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        super.onPSubscribe(pattern, subscribedChannels);
    }
    
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onMessage(String channel, String message) {
        this.lastUpdated.set(System.currentTimeMillis());

        try {
            emitter.onNext(new RedisPubSubMessage(channel, message));
        } catch (Throwable t) {
            System.out.println( "Failed to call onNext for redis pubsub message: " + channel + ":" + message);
        }
        
        System.out.println( "Received redis pubsub message for channel: {" + channel + "}, msg: {" + message + "}" );
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        this.lastUpdated.set(System.currentTimeMillis());

        try{
            emitter.onNext(new RedisPubSubMessage(channel, message));
        } catch (Throwable t){
            System.out.println( "Failed to call onNext for redis pubsub message: " + channel + ":" + message);
        }
        
        System.out.println( "Received redis pubsub message for Pattern: {" + pattern + "}, Channel: {" + channel + "}, Msg: {" + message + "}" );
    }

    public void close() {
        if(isSubscribed()) {
            //Attempt to unsubscribe all patterns
            try {
                if(pattern) {
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