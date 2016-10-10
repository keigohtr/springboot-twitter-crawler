package com.apitore.fruitbasket.bloodorange.twitter.scheduler.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class TwitterCrawlerEntity {

  private List<String> queries;
  private Map<String,Long> maxIds;

  public TwitterCrawlerEntity() {
    this.queries = new ArrayList<String>();
    this.maxIds  = new HashMap<String,Long>();
  }

}