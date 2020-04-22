package uk.ac.man.cs.eventlite.dao;

import java.util.List;
import java.util.stream.Collectors;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Twitter;

public class TwitterService {

	private Twitter twitter;
	
	public TwitterService() {
		String consumerKey = "Y24OPYtAsp5yFL7z68lQZ1yXn";
		String consumerSecretKey = "mtKkHRWM3T0iZscSV7XDGuGrjm39DvecVIZGDwwmHhJPum2Srw";
		String accessToken = "1249734082323533826-Eon4lzUymi14xoqQQbRpefRVvT5YuD";
		String accessSecretToken = "9LkGpLbBjNgvYRlhjANecHAywDHoh873ouApND7nbGEkA";
		
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecretKey)
		  .setOAuthAccessToken(accessToken)
		  .setOAuthAccessTokenSecret(accessSecretToken);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		this.twitter = twitter;
		
	}
	
	public Twitter getTwitter() {
		return this.twitter;
	}
	
	public String createTweet(String tweet) throws TwitterException {

	    Status status = this.twitter.updateStatus(tweet);
	    return status.getText();
	}
	
	public List<String> getTimeLine() throws TwitterException {
	     
	    return this.twitter.getHomeTimeline().stream()
	      .map(item -> item.getText())
	      .collect(Collectors.toList());
	}
}
