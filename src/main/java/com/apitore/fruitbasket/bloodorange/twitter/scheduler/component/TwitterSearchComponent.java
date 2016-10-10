package com.apitore.fruitbasket.bloodorange.twitter.scheduler.component;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.apitore.fruitbasket.bloodorange.twitter.scheduler.entity.TwitterCrawlerEntity;
import com.apitore.fruitbasket.bloodorange.twitter.scheduler.service.TwitterService;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


/**
 * @author Keigo Hattori
 */
@Component
public class TwitterSearchComponent {
  private final Logger LOG = Logger.getLogger(TwitterSearchComponent.class);

  @Autowired
  private TwitterService twitterService;

  @Bean(name="initialize")
  public TwitterCrawlerEntity init() throws IOException {
    TwitterCrawlerEntity obj = new TwitterCrawlerEntity();
    BufferedReader br = new BufferedReader(new FileReader(new File("./searchlist.txt")));
    String str;
    while((str = br.readLine()) != null) {
      if (str.startsWith("\\/\\/"))
        continue;
      obj.getQueries().add(str);
      obj.getMaxIds().put(str, -1L);
    }
    br.close();
    return obj;
  }

  @Resource(name="initialize")
  TwitterCrawlerEntity obj;

  private final Integer COUNT = 100;
  private final String LANG = "ja";
  private final String LOCALE = "ja";

  private int idx=-1;
  private final int MAX_REQUEST = 100;


  @Scheduled(initialDelay = 5000, fixedDelay = 900000)
  public void update() throws Exception {
    System.out.println("Start.");
    LOG.info("Start.");
    Twitter twitter = twitterService.getTwitter();
    if (twitter == null) {
      System.err.println("Twitter.com is busy or Password is incorrect.");
      throw new Exception();
    }

    if (this.obj.getMaxIds().isEmpty())
      System.exit(0);

    String filebase = String.valueOf(System.currentTimeMillis());
    List<String> tweets = new ArrayList<String>();
    int num = this.obj.getQueries().size();
    Query query = new Query();
    query.setLang(this.LANG);
    query.setLocale(this.LOCALE);
    query.setCount(this.COUNT);

    for (int i=0; i<MAX_REQUEST; i+=1) {
      if (this.obj.getMaxIds().isEmpty())
        break;
      this.idx = (this.idx+1)%num;
      String str = this.obj.getQueries().get(idx);

      query.setQuery(str);
      if (this.obj.getMaxIds().containsKey(str)) {
        Long maxId = this.obj.getMaxIds().get(str);
        if (maxId != -1)
          query.setMaxId(maxId);
        getSearch(tweets, twitter, query, str);
      } else {
        i--;
      }
    }

    if (!tweets.isEmpty()) {
      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
          new File(String.format("./data/%s.txt", filebase)))));
      for (String str: tweets)
        pw.println(str);
      pw.flush();
      pw.close();
    }
    System.out.println("End.");
    LOG.info("End.");
  }

  private void getSearch(List<String> tweetlist, Twitter twitter, Query query, String str) {
    try {
      System.out.println(str);
      QueryResult statuses = twitter.search(query);
      if(statuses != null){
        List<Status> tweets = statuses.getTweets();
        if (!tweets.isEmpty()) {
          Long value = tweets.get(tweets.size()-1).getId()-1;
          this.obj.getMaxIds().put(str, value);
        } else {
          System.out.println("...removed.");
          this.obj.getMaxIds().remove(str);
        }
        for (Status status: tweets) {
          String tweet = status.getText();
          tweet = tweet.replaceAll("[\r\n\t\\s]+", " ");
          String id    = String.valueOf(status.getId());
          if(tweet.length() < 20)
            continue;
          if(status.isRetweeted() || tweet.startsWith("RT "))
            continue;
          //if(status.getInReplyToStatusId() != -1 || status.getText().startsWith("@"))
          //  continue;
          if(tweet.contains("http"))
            continue;
          String ltwt = tweet.toLowerCase();
          String chk = str.toLowerCase().replace("\"", "");
          if(!ltwt.matches(".*"+chk+".*"))
            continue;
          if(ltwt.matches(".*@[\\w\\d_]*"+chk+"[\\w\\d_]*.*"))
            continue;
          tweetlist.add(String.format("%s\t%s\t%s", id,str,tweet));
        }
      }
      Thread.sleep(200);
    } catch (TwitterException e) {
      System.err.println(e.toString());
      LOG.error("TwitterException", e);
      try {
        Thread.sleep(300000);
      } catch (InterruptedException e1) {
        System.err.println(e1.toString());
        LOG.error("InterruptedException", e1);
      }
    } catch (InterruptedException e) {
      System.err.println(e.toString());
      LOG.error("InterruptedException", e);
    }
  }

}
