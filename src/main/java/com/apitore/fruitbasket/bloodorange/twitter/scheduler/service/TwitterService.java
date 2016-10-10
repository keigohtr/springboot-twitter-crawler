package com.apitore.fruitbasket.bloodorange.twitter.scheduler.service;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


@Service
public class TwitterService {

  @Bean(name="twitterInit")
  public Twitter twitterInit(
      @Value("${consumer.key}")
      String CONSUMERKEY,
      @Value("${consumer.secret}")
      String CONSUMERSECRET,
      @Value("${access.token}")
      String ACCESSTOKEN,
      @Value("${access.token.secret}")
      String ACCESSSECRET
      ) {
    ConfigurationBuilder confbuilder = new ConfigurationBuilder();
    confbuilder.setOAuthConsumerKey(CONSUMERKEY);
    confbuilder.setOAuthConsumerSecret(CONSUMERSECRET);
    confbuilder.setOAuthAccessToken(ACCESSTOKEN);
    confbuilder.setOAuthAccessTokenSecret(ACCESSSECRET);

    TwitterFactory twitterfactory = new TwitterFactory(confbuilder.build());
    Twitter twitter = twitterfactory.getInstance();
    return twitter;
  }

  @Resource(name="twitterInit")
  Twitter twitter;

  public Twitter getTwitter() {
    return this.twitter;
  }

}