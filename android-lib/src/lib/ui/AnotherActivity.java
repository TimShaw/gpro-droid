package lib.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.lib.R;
import android.os.Bundle;

/**
 * @ClassName: AnotherAcitivty.java
 * @Description:
 * @Author JinChao
 * @Date 2013-2-26 下午2:15:32
 * @Copyright: 版权由 HundSun 拥有
 */
public class AnotherActivity extends Activity
{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.showNotification();

    }

    protected void showNotification()
    {
        CharSequence from = "IM";
        CharSequence message = "IM start up";

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("lib.ui",
                                                        "lib.ui.AnotherAcitivty");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // construct the Notification object.
        Notification notif = new Notification(R.drawable.ic_launcher, "IMM Still run background!", System.currentTimeMillis());
        notif.setLatestEventInfo(this, from, message, contentIntent);
        //notif.flags|=Notification.FLAG_AUTO_CANCEL;
        
        notif.flags|= Notification.FLAG_ONGOING_EVENT;
        // look up the notification manager service
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notif);
    }
}
