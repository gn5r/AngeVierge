package via.gn5r.com.angevierge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by via.gn5r on 2017/11/27.
 */

public class GetMediaTask extends AsyncTask<URL, Void, Bitmap> {
    private Tweet tweet;
    private MainActivity mainActivity;

    public GetMediaTask(Tweet tweet, MainActivity mainActivity) {
        this.tweet = tweet;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        final URL url = urls[0];
        HttpURLConnection connection = null;
        Bitmap bitmap = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        tweet.setMediaBitmap(bitmap);
        mainActivity.viewTweet(tweet);
    }
}
