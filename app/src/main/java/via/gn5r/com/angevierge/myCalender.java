package via.gn5r.com.angevierge;

import java.util.Calendar;

/**
 * Created by shangyuan.tuolang on 2017/11/13.
 */

public class myCalender {
    private String nowDate;

    public myCalender() {
        Calendar calendar = Calendar.getInstance();
        nowDate = String.valueOf(calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DATE) + "日"
                + calendar.get(Calendar.HOUR_OF_DAY) + "時"
                + calendar.get(Calendar.MINUTE) + "分"
                + calendar.get(Calendar.SECOND) + "秒";
    }

    public String getNowDate() {
        return nowDate;
    }
}
