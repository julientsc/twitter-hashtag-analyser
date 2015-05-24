package model;

import model.tweet.MyTweet;
import twitter4j.Status;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TweetCollection {

    public static final String PATH_TWEETS = "tweets.json";
    public static final String PATH_HASHTAGS = "hashtags.json";
    public static final long SAVE_COUNT = 500;
    private static TweetCollection INSTANCE = null;
    private long count = 0;

    private Recordable<HashMap<Long, MyTweet>> myTweets = null;
    private Recordable<HashMap<String, List<Long>>> myHashtags = null;

    private TweetCollection() throws Exception {
        this.myTweets = new Recordable<HashMap<Long, MyTweet>>(PATH_TWEETS, new HashMap<Long, MyTweet>());
        this.myHashtags = new Recordable<HashMap<String, List<Long>>>(PATH_HASHTAGS, new HashMap<String, List<Long>>());
    }

    public static TweetCollection getInstance() throws Exception {
        if(INSTANCE == null) {
            INSTANCE = new TweetCollection();
        }
        return INSTANCE;
    }

    public void addTweet(String hashtag, Status tweet) {
        long tweetId = tweet.getId();
        if (!myTweets.getData().containsKey(tweetId)) {
            MyTweet myTweet = MyTweet.convert(tweet);
            if (myTweet == null)
                return;

            myTweets.getData().put(tweetId, myTweet);

            if(!myHashtags.getData().containsKey(hashtag)){
                myHashtags.getData().put(hashtag, new ArrayList<Long>());
            }
            if(!myHashtags.getData().get(hashtag).contains(tweetId)) {
                myHashtags.getData().get(hashtag).add(tweetId);
            }

            count++;
            System.out.println(count + "\t" + tweet.getId());

            save();
        }
    }

    public void save() {
        if(count == SAVE_COUNT) {
            System.out.println("Save Data");
            try {
                myTweets.save();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
            try {
                myHashtags.save();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
            count = 0;
        }
    }

}
