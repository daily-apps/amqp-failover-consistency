package com.guideapps.notifications;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class NotificationsApplication 
{
    public static void main( String[] args )
    {
    	System.out.println( "Hello notifications-app ;)");
    	System.out.println( "Consuming some messages:");
    	Arrays.asList("Message 1", "Message 2", "Message 3").stream()
    	.filter(m -> {
    		return "Message 1".contains("Message");
    	}).forEach(message -> {
    		System.out.println(String.format("Message %s consumed...", message));
    	});
        
    }
}
