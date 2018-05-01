package via.gn5r.com.angevierge;

import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserStreamAdapter;

/**
 * Created by via.gn5r on 2017/11/26.
 */

class TwitterStreamAdapter extends UserStreamAdapter {
    private MainActivity mainActivity;
    private Twitter twitter;

    public TwitterStreamAdapter(MainActivity mainActivity, Twitter twitter) {
        this.mainActivity = mainActivity;
        this.twitter = twitter;
    }

    @Override
    public void onStatus(Status status) {
        super.onStatus(status);
        final Tweet tweet = new Tweet();

        if (status.getText().startsWith("RT @")) {

        } else {
            tweet.setUserName(status.getUser().getName());
            tweet.setScreenName(status.getUser().getScreenName());
            tweet.setTweet(status.getText());
            tweet.setViaName(status.getSource().replaceAll("<.+?>", ""));
            myCalender calender = new myCalender();
            tweet.setNowDate(calender.getNowDate());
            tweet.setFavNum(status.getFavoriteCount());
            tweet.setRtweetNum(status.getRetweetCount());
            tweet.setStatusID(status.getId());
            tweet.setStatus(status);

            try {
                new GetUserIconTask(tweet, mainActivity)
                        .execute(new URL(status.getUser().getBiggerProfileImageURL()));
                MediaEntity[] entities = status.getExtendedMediaEntities();
                if (entities.length > 0) {
                    tweet.setMediaURL(entities[0].getMediaURL());
                    new GetMediaTask(tweet, mainActivity).execute(new URL(entities[0].getMediaURL()));
                    tweet.setTweet(status.getText().replaceAll("https://t.co/\\w{10}", ""));
                }
                mainActivity.streamViewTweet(tweet);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        try {
            if (target.getScreenName().matches(".*" + twitter.getScreenName() + ".*")) {
                mainActivity.showToast("@" + source.getScreenName() + " にふぁぼられた");
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        try {
            if (target.getScreenName().matches(".*" + twitter.getScreenName() + ".*")) {
                mainActivity.showToast("@" + source.getScreenName() + " にふぁぼが解除されました");
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFollow(User source, User followedUser) {
        mainActivity.showToast("@" + source.getScreenName() + " にフォローされました");
    }
}