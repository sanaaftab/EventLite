package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import uk.ac.man.cs.eventlite.EventLite;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventLite.class)
@DirtiesContext
@ActiveProfiles("test")

public class TwitterServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private Twitter twitter;
	
	private TwitterService twitterService;
	
	@Test
	public void testCreateTweet() throws Exception{
		
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
		twitter = tf.getInstance();
		twitterService = mock(TwitterService.class);
		
		given(twitterService.getTwitter()).willReturn(twitter);
		
		Twitter expected = twitterService.getTwitter();
		
		assertThat(expected, equalTo(twitter));
	}
}
