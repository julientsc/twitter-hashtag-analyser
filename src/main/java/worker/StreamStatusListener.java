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

    @Override
    public void onDeletionNotice(StatusDeletionNotice arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScrubGeo(long arg0, long arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStallWarning(StallWarning arg0) {
        // TODO Auto-generated method stub

    }

    @Override
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

    @Override
    public void onException(Exception e) {
//		e.printStackTrace();
    }

    @Override
    public void onTrackLimitationNotice(int arg0) {

    }
}
