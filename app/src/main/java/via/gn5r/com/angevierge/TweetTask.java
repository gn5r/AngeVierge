package via.gn5r.com.angevierge;

import android.os.AsyncTask;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.media.ImageUpload;

/**
 * Created by shangyuan.tuolang on 2017/11/13.
 */

public class TweetTask extends AsyncTask<String, Void, Boolean> {

    private Twitter twitter;
    private MainActivity mainActivity;
    private File imageFile;
    private ImageUpload imageUpload;

    public TweetTask(Twitter twitter, MainActivity mainActivity, File imageFile, ImageUpload imageUpload) {
        this.twitter = twitter;
        this.mainActivity = mainActivity;
        this.imageFile = imageFile;
        this.imageUpload = imageUpload;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            if (imageFile != null) {
                imageUpload.upload(imageFile, strings[0]);
            } else {
                twitter.updateStatus(strings[0]);
            }
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mainActivity.showToast("送信しました");
        } else {
            mainActivity.showToast("何かがおかしいよ");
        }
    }
}
