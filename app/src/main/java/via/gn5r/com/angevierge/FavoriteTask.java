package via.gn5r.com.angevierge;

import android.os.AsyncTask;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by via.gn5r on 2017/11/26.
 */

public class FavoriteTask extends AsyncTask<Void, Void, Boolean> {
    private Tweet tweet;
    private Twitter twitter;
    private MainActivity mainActivity;

    public FavoriteTask(Twitter twitter, Tweet tweet, MainActivity mainActivity) {
        this.twitter = twitter;
        this.tweet = tweet;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            tweet.setStatus(twitter.createFavorite(tweet.getStatusID()));
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            mainActivity.showToast("ふぁぼりました");
        } else {
            mainActivity.showToast("なにかがおかしいよ");
        }
    }
}
