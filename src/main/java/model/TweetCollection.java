package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.tweet.MyTweet;
import twitter4j.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TweetCollection {

    public static final String PATH_TWEETS = "data/tweets/";
    public static final String PATH_HASHTAGS = "data/hashtags.json";
    public static final long SAVE_COUNT = 10;
    private static TweetCollection INSTANCE = null;
    private long count = 0;

    private HashMap<String, HashMap<Long, MyTweet>> myTweets = null;
    private HashMap<String, List<Long>> myHashtags = null;

    private HashMap<String, Long> tweetCounter = new HashMap<>();

    private TweetCollection() {
        new File("data/tweets/").mkdirs();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        tweetCounter = new HashMap<String, Long>();
        BufferedReader br = null;

        try {
            System.out.println("Read " + new File(PATH_HASHTAGS).getAbsolutePath());
            br = new BufferedReader(new FileReader(PATH_HASHTAGS));
            myHashtags = gson.fromJson(br, new TypeToken<HashMap<String, List<Long>>>() {
            }.getType());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            myHashtags = new HashMap<String, List<Long>>();
        }

        myTweets = new HashMap<String, HashMap<Long, MyTweet>>();
        for (String s : myHashtags.keySet()) {
            try {
                String file = PATH_TWEETS + "/" + s + ".json";
                System.out.println("Read " + new File(file).getAbsolutePath());
                br = new BufferedReader(new FileReader(file));
                HashMap<Long, MyTweet> tweetsCollection = gson.fromJson(br, new TypeToken<HashMap<Long, MyTweet>>() {
                }.getType());
                br.close();
                myTweets.put(s, tweetsCollection);
            } catch (IOException e) {
                e.printStackTrace();
                myTweets.put(s, new HashMap<Long, MyTweet>());
                tweetCounter.put(s, (long) 0);
            }
        }
    }

    public static TweetCollection getInstance() throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new TweetCollection();
        }
        return INSTANCE;
    }


    public void addTweet(String hashtag, Status tweet) throws FileNotFoundException {
        long tweetId = tweet.getId();
        if (!myTweets.containsKey(hashtag)) {
            myTweets.put(hashtag, new HashMap<Long, MyTweet>());
        }

        MyTweet myTweet = MyTweet.convert(tweet);
        if (myTweet == null)
            return;
        myTweets.get(hashtag).put(tweetId, myTweet);

        if (!tweetCounter.containsKey(hashtag)) {
            tweetCounter.put(hashtag, (long) 0);
        } else {
            tweetCounter.put(hashtag, tweetCounter.get(hashtag) + 1);
        }

        if (!myHashtags.containsKey(hashtag))
            myHashtags.put(hashtag, new ArrayList<Long>());
        myHashtags.get(hashtag).add(tweetId);

        if (tweetCounter.get(hashtag) == SAVE_COUNT) {
            tweetCounter.put(hashtag, (long) 0);

            synchronized (this) {
                String file = PATH_TWEETS + "/" + hashtag + ".json";
                System.out.println("Save " + new File(file).getAbsolutePath());

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String content = gson.toJson(myTweets.get(hashtag));

                PrintWriter pw = new PrintWriter(file);
                pw.write(content);
                pw.close();

                System.out.println("Save " + new File(PATH_HASHTAGS).getAbsolutePath());
                String content2 = gson.toJson(myHashtags);
                PrintWriter pw2 = new PrintWriter(PATH_HASHTAGS);
                pw2.write(content2);
                pw2.close();
            }

        }


    }


    public void save() throws FileNotFoundException {
        synchronized (this) {
            for (String hashtag : myTweets.keySet()) {
                String file = PATH_TWEETS + "/" + hashtag + ".json";
                System.out.println("Save " + new File(file).getAbsolutePath());

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String content = gson.toJson(myTweets.get(hashtag));

                PrintWriter pw = new PrintWriter(file);
                pw.write(content);
                pw.close();

                System.out.println("Save " + new File(PATH_HASHTAGS).getAbsolutePath());
                String content2 = gson.toJson(myTweets.get(hashtag));
                PrintWriter pw2 = new PrintWriter(PATH_HASHTAGS);
                pw2.write(content2);
                pw2.close();
            }
        }
    }
}
