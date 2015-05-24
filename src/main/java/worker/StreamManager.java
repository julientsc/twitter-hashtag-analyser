package worker;

import model.Config;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class StreamManager {

    private static StreamManager instance = null;
    private StreamStatusListener listener ;
    private TwitterStream twitterStream = null;
    private boolean isWorking = false;
    private StreamManager() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("fPZObUh20Etmgd3EOiFe8Cc31")
                .setOAuthConsumerSecret("KvtcoNWyLrJyUEwGKrni7zMXSAlnpso8I8FtFv1YwA5LJBxR2K")
                .setOAuthAccessToken("286388280-WIk9xAM4LnYW4KuVOGkv2Cr1uz1T0U1GSSzGDpdj")
                .setOAuthAccessTokenSecret("tWO3TTtFDCKXpdiAYV8kT1wjivcnQaH590UposkkxDq20");


        listener = new StreamStatusListener();

        this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        this.twitterStream.addListener(listener);

    }

    public static StreamManager getInstance() {
        if (instance == null) {
            instance = new StreamManager();
        }
        return instance;
    }

    private static String [] extractHashtagFromConfig(Config config) {
        ArrayList<String> hashtags = new ArrayList<String>();
        HashMap<String, ArrayList<String>> mainHashtags = config.getData();
        for (String mainHashtag : mainHashtags.keySet()) {
            if(!hashtags.contains(mainHashtag)) {
                hashtags.add(mainHashtag);
            }
            for (String subHashtag : mainHashtags.get(mainHashtag)) {
                if(!hashtags.contains(subHashtag))
                    hashtags.add(subHashtag);
            }
        }

        String []  r = new String[hashtags.size()];
        int count = 0;
        for (String hashtag : hashtags) {
            r[count++] = "#" + hashtag.toLowerCase();
        }
        return r;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public boolean startStream(Config config) {
        if(isWorking)
            return false;
        isWorking = true;

        System.out.println("Start Stream");
        String[] hashtags = extractHashtagFromConfig(config);
        this.listener.setHashtag(hashtags);

        for(int i = 0 ; i < hashtags.length ; i++) {
            System.out.println(" + " + hashtags[i]);
        }

        FilterQuery fq = new FilterQuery();
        fq.track(hashtags);

        twitterStream.filter(fq);

        System.out.println("=> Stream started !");
        return false;
    }

    public boolean stopStream() {
        if(!isWorking)
            return false;
        isWorking = false;

        System.out.println("Stop Stream");

        twitterStream.shutdown();

        System.out.println("=> Stream Stopped");
        return true;
    }

    public void changeConfig(Config config) {
        System.out.println("Change Recordable");
        stopStream();
        startStream(config);
    }
}
