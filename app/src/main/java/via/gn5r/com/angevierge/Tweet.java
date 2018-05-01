package via.gn5r.com.angevierge;

import android.graphics.Bitmap;

import java.io.Serializable;

import twitter4j.Status;

/**
 * Created by gn5r on 17/11/03.
 */

public class Tweet implements Serializable{
    private String userName, screenName, tweet, viaName, nowDate, mediaURL;
    private Bitmap bitmap, mediaBitmap;
    private long statusID;
    private Status status;
    private int rtweetNum, favNum;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getViaName() {
        return viaName;
    }

    public void setViaName(String viaName) {
        this.viaName = viaName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getNowDate() {
        return nowDate;
    }

    public int getRtweetNum() {
        return rtweetNum;
    }

    public void setRtweetNum(int rtweetNum) {
        this.rtweetNum = rtweetNum;
    }

    public int getFavNum() {
        return favNum;
    }

    public void setFavNum(int favNum) {
        this.favNum = favNum;
    }

    public long getStatusID() {
        return statusID;
    }

    public void setStatusID(long statusID) {
        this.statusID = statusID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Bitmap getMediaBitmap() {
        return mediaBitmap;
    }

    public void setMediaBitmap(Bitmap mediaBitmap) {
        this.mediaBitmap = mediaBitmap;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }
}
