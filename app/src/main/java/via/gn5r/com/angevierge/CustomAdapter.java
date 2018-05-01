package via.gn5r.com.angevierge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by gn5r on 17/11/03.
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Tweet> tweetList;
    private LinearLayout imageLayout;

    public CustomAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTweetList(ArrayList<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    @Override
    public int getCount() {
        return tweetList.size();
    }

    @Override
    public Object getItem(int i) {
        return tweetList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_list, viewGroup, false);

        if (tweetList.get(i).getScreenName().equals("Gin_Ray")) {
            view.setBackgroundResource(R.color.aquas);
        }
        if (tweetList.get(i).getTweet().matches(".*" + "@Gin_Ray" + ".*")) {
            view.setBackgroundResource(R.color.replay);
        }

        imageLayout = (LinearLayout) view.findViewById(R.id.viewImage);
        if (imageLayout.getChildCount() > 0) {
            imageLayout.removeAllViews();
        }

        ((TextView) view.findViewById(R.id.userName)).setText(tweetList.get(i).getUserName());
        ((TextView) view.findViewById(R.id.screenName)).setText(" ＠" + tweetList.get(i).getScreenName());
        ((TextView) view.findViewById(R.id.tweet)).setText(tweetList.get(i).getTweet());
        ((TextView) view.findViewById(R.id.viaName)).setText("via " + tweetList.get(i).getViaName());
        ((TextView) view.findViewById(R.id.nowDate)).setText(tweetList.get(i).getNowDate());
        ((ImageView) view.findViewById(R.id.userIcon)).setImageBitmap(tweetList.get(i).getBitmap());
        ((TextView) view.findViewById(R.id.rt_num)).setText(String.valueOf(tweetList.get(i).getRtweetNum()));
        ((TextView) view.findViewById(R.id.fav_num)).setText(String.valueOf(tweetList.get(i).getFavNum()));

        if (!TextUtils.isEmpty(tweetList.get(i).getMediaURL())) {
            ImageView mediaImage = new ImageView(context);
            mediaImage.setImageBitmap(tweetList.get(i).getMediaBitmap());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
            imageLayout.addView(mediaImage, params);
        }


        final ImageView imageView = (ImageView)view.findViewById(R.id.userIcon);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Toast.makeText(context,"おされた",Toast.LENGTH_SHORT).show();

                            break;

                        default:break;
                    }
                return true;
            }
        });
        return view;
    }
}
