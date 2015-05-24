package worker;

import model.TweetCollection;
import twitter4j.*;

import java.util.ArrayList;

public class StreamStatusListener implements StatusListener {

    private ArrayList<String> hashtags = null;

    public void setHashtag(String [] hashtags) {
        this.hashtags = new ArrayList<String>();
        for(int i = 0 ; i < hashtags.length ; i++)
            this.hashtags.add(hashtags[i]);
    }

    public void onDeletionNotice(StatusDeletionNotice arg0) {


    }

    public void onScrubGeo(long arg0, long arg1) {


    }

    public void onStallWarning(StallWarning arg0) {


    }

    public void onStatus(Status status) {
        try {
            TweetCollection collection = TweetCollection.getInstance();
            for (HashtagEntity hashtag : status.getHashtagEntities()) {
                String h = "#" + hashtag.getText().toLowerCase();
                if (this.hashtags.contains(h)) {
                    System.out.println("+" + status.getUser().getName() + " : " + status.getText().replaceAll("\n", " "));
                    collection.addTweet(hashtag.getText().toLowerCase(), status);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void onException(Exception e) {
//		e.printStackTrace();
    }

    public void onTrackLimitationNotice(int arg0) {

    }
}
