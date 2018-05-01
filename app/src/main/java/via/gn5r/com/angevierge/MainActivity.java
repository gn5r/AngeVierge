package via.gn5r.com.angevierge;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;

public class MainActivity extends AppCompatActivity {

    private Twitter twitter;
    private Handler viewTimeLineHandler, userStreamHandler, toastHandler, notifyHandler;
    private ArrayList<Tweet> tweetList;
    private ListView listView;
    private File path;
    private ImageUpload imageUpload;
    private SwipeRefreshLayout refreshLayout;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_main);

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            twitter = TwitterUtils.getTwitterInstance(this);
            viewTimeLineHandler = new Handler();
            userStreamHandler = new Handler();
            toastHandler = new Handler();
            notifyHandler = new Handler();
            tweetList = new ArrayList<>();
            listView = (ListView) findViewById(R.id.mainView);
            adapter = new CustomAdapter(MainActivity.this);
            setUserStreaming();
            setListView();
            setImageButton();

            getTimeLine();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            String[] columns = {MediaStore.Images.Media.DATA};
            Cursor c = cr.query(uri, columns, null, null, null);

            c.moveToFirst();
            path = new File(c.getString(0));
            if (!path.exists())
                return;

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
                builder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));
                builder.setOAuthAccessToken(twitter.getOAuthAccessToken().getToken());
                builder.setOAuthAccessTokenSecret(twitter.getOAuthAccessToken().getTokenSecret());
                builder.setMediaProvider("TWITTER");

                Configuration conf = builder.build();

                imageUpload = new ImageUploadFactory(conf)
                        .getInstance();

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    public void showToast(final String toast) {
        toastHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> timelineTask = new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    Paging paging = new Paging(1, 50);
                    return twitter.getHomeTimeline(paging);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    showToast("API制限です、終了します");
                    finish();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> statuses) {
                if (statuses != null) {
                    for (twitter4j.Status status : statuses) {
                        Tweet tweet = new Tweet();

                        if (status.getText().startsWith("RT @")) {

                        } else {
                            tweet.setUserName(status.getUser().getName());
                            tweet.setScreenName(status.getUser().getScreenName());
                            tweet.setTweet(status.getText());
                            tweet.setViaName(status.getSource().replaceAll("<.+?>", ""));
                            tweet.setFavNum(status.getFavoriteCount());
                            tweet.setRtweetNum(status.getRetweetCount());
                            tweet.setStatusID(status.getId());
                            tweet.setStatus(status);

                            try {
                                new GetUserIconTask(tweet, MainActivity.this)
                                        .execute(new URL(status.getUser().getBiggerProfileImageURL()));

                                MediaEntity[] entities = status.getExtendedMediaEntities();
                                if (entities.length > 0) {
                                    tweet.setMediaURL(entities[0].getMediaURL());
                                    new GetMediaTask(tweet, MainActivity.this).execute(new URL(entities[0].getMediaURL()));
                                    tweet.setTweet(status.getText().replaceAll("https://t.co/\\w{10}", ""));
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
        timelineTask.execute();
    }

    public void viewTweet(final Tweet tweet) {
        viewTimeLineHandler.post(new Runnable() {
            @Override
            public void run() {
                tweetList.add(tweet);
                adapter.setTweetList(tweetList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
    }

    public void setUserStreaming() {
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
            builder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));
            builder.setOAuthAccessToken(twitter.getOAuthAccessToken().getToken());
            builder.setOAuthAccessTokenSecret(twitter.getOAuthAccessToken().getTokenSecret());

            Configuration conf = builder.build();
            TwitterStream stream = new TwitterStreamFactory(conf).getInstance();

            stream.addListener(new TwitterStreamAdapter(MainActivity.this, twitter));
            stream.user();

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public void streamViewTweet(final Tweet tweet) {
        userStreamHandler.post(new Runnable() {
            @Override
            public void run() {
                int index = tweetList.size();
                tweetList.remove(index - 1);
                tweetList.add(0, tweet);
                adapter.setTweetList(tweetList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
    }

    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.editTweet);
        String tweetMessage = editText.getText().toString();
        new TweetTask(twitter, MainActivity.this, path, imageUpload).execute(tweetMessage);
        editText.setText("");
    }

    private void setListView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showToast("更新しました");
                tweetList.clear();
                getTimeLine();
                refreshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (!TextUtils.isEmpty(tweetList.get(i).getMediaURL())) {
                    showToast("URL:" + tweetList.get(i).getMediaURL());
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new FavoriteTask(twitter, tweetList.get(position), MainActivity.this).execute();
                return false;
            }
        });
    }

    private void setImageButton() {
        ImageButton imageButton = (ImageButton) findViewById(R.id.postTweet);
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

}
