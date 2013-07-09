package lib.ui;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import lib.Tool;
import lib.func.ScreenShot;
import lib.func.contact.ContactActivity;
import lib.func.floatondesktop.FloatOnDescktopActivity;
import lib.func.handwrite.HandwritingActivity;
import lib.func.loadlocale.LoadLocaleActivity;
import lib.func.wps.WifiPosition;
import lib.ui.dialog.Tip;
import lib.ui.layout.TableLayout;
import lib.ui.list.list1.SelfListViewActivity;
import lib.ui.location.CellActivity;
import lib.ui.location.LocationActivity;
import lib.ui.menu.MenuActivity;
import lib.ui.select.SelectActivity;
import lib.ui.share.ShareSdkActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.wheel.WheelActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UILibActivity extends Activity {
    /** Called when the activity is first created. */
    private EditText startDateTime;
    private EditText endDateTime;
    private UILibActivity me = this;
    private String initStartDateTime = "2012年9月3日 14:44";
    private String TAG = UILibActivity.class.getSimpleName();
    
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        Button wifiPosition = (Button)findViewById(R.id.wifiPosition);
        wifiPosition.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,WifiPosition.class);
                startActivity(intent);
            }
        });
        
        
        Button copyAsset = (Button)findViewById(R.id.copyAsset);
        copyAsset.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ///Tool.copyAssets(me);
                List<String> fileLists = Tool.listFile(me, "www");
                
                String dataPath = Tool.getDataDir(me);
                for(String filepath:fileLists){
                    String filename = filepath.substring(filepath.lastIndexOf("/")+1);
                    Log.i(TAG, "filename:"+filename);
                    
                    File file = new File(dataPath);
                    try
                    {
                        InputStream is = me.getResources().getAssets().open(filepath);
                        byte[] b = new byte[256];
                        int count=0;
                        while((count=is.read(b))!=-1){
                           
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                
            }
        });
        
        Button pullListViewSelf = (Button)findViewById(R.id.pullListViewSelf);
        pullListViewSelf.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,SelfListViewActivity.class);
                startActivity(intent);
            }
        });
        
        
        Button loadLocale = (Button)findViewById(R.id.loadLocale);
        loadLocale.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,LoadLocaleActivity.class);
                startActivity(intent);
            }
        });
        
        
        Button floatondesktop = (Button)findViewById(R.id.floatondesktop);
        floatondesktop.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,FloatOnDescktopActivity.class);
                startActivity(intent);
            }
        });
        
        Button selectPopwindow = (Button)findViewById(R.id.selectPopwindow);
        selectPopwindow.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,SelectActivity.class);
                startActivity(intent);
            }
        });
        
        Button contact = (Button)findViewById(R.id.contact);
        contact.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,ContactActivity.class);
                startActivity(intent);
            }
        });
        
        
        Button imageListViewBtn = (Button)findViewById(R.id.imageList);
        imageListViewBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,ImageListViewActivity.class);
                startActivity(intent);
            }
        });
        
        Button handwriting = (Button)findViewById(R.id.handwriting);
        handwriting.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,HandwritingActivity.class);
                startActivity(intent);
            }
        });
        
        Button wheel = (Button)findViewById(R.id.wheel);
        wheel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,WheelActivity.class);
                startActivity(intent);
            }
        });
        
        Button image_grid = (Button)findViewById(R.id.image_grid);
        image_grid.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,ImageGridActivity.class);
                startActivity(intent);
            }
        });
        Button image_gallery = (Button)findViewById(R.id.image_gallery);
        image_gallery.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,ImageGalleryActivity.class);
                startActivity(intent);
            }
        });
        
        
        
        Button screenshot = (Button)findViewById(R.id.screenshot);
        screenshot.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ScreenShot.shoot(me);
            }
        });
        
        Button celllocation = (Button)findViewById(R.id.celllocation);
        celllocation.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,CellActivity.class);
                startActivity(intent);
            }
        });
        
        Button location = (Button)findViewById(R.id.location);
        location.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,LocationActivity.class);
                startActivity(intent);
            }
        });
        
        Button shareSdk = (Button)findViewById(R.id.shareSdk);
        shareSdk.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,ShareSdkActivity.class);
                startActivity(intent);
            }
        });
        
        
        Button googlemap = (Button)findViewById(R.id.googlemap);
        googlemap.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,GoogleMapActivity.class);
                startActivity(intent);
            }
        });
        
        Button shareQQ = (Button)findViewById(R.id.shareQQ);
        shareQQ.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,QQShareActivity.class);
                startActivity(intent);
            }
        });
        
        
       /* 
        WebView wv = (WebView)findViewById(R.id.webView);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //wv.setWebChromeClient(new WebChromeClient());
        
        
        wv.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                me.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            // The undocumented magic method override
            // Eclipse will swear at you if you try to put @Override here
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                me.startActivityForResult(Intent.createChooser(i, "File Chooser"),FILECHOOSER_RESULTCODE);
            }
        });

        
        
        //String url = "http://192.168.84.101:8686/maw-ht/viewer/viewer3.html";
        String url ="http://192.168.92.61:8080/test/upload.html";
        wv.loadUrl(url);*/
        
        
        
        
        Button selfListView = (Button)findViewById(R.id.selfListView);
        selfListView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,SelfListViewActivity.class);
                startActivity(intent);
            }
        });
        
        Button dialogShow = (Button) findViewById(R.id.dialogShow);  
        dialogShow.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Tip(me).show();
            }
        });
        
        Button sendNotiBt = (Button) findViewById(R.id.sendNotiBt);  
        sendNotiBt.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                //showDefaultNotification();  
                showCustomizeNotification();
            }  
        });          
        
        startDateTime = (EditText) findViewById(R.id.inputDate);
        startDateTime.setText(initStartDateTime);

        startDateTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        UILibActivity.this, initStartDateTime);
                dateTimePicKDialog.dateTimePicKDialog(startDateTime);

            }
        });
        
        
        Button notibar_icon = (Button)findViewById(R.id.notibar_icon);
        notibar_icon.setOnClickListener(new OnClickListener()  
        {  
            public void onClick(View v)  
            {  
//              TestActivity.this.openOptionsMenu();  
                /*String packName = "lib.ui";  
                String className = packName + ".AnotherAcitivty";  
                  
                Intent intent = new Intent();  
                ComponentName componentName = new ComponentName(packName, className);  
                intent.setComponent(componentName);  */
                
                Intent intent = new Intent(); 
                intent.setAction("lib.ui.AnotherActivity");
                /*  
                intent.addCategory("android.intent.category.DEFAULT");  
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  */
                  
                UILibActivity.this.startActivity(intent);  
            }  
        });  
        
        
        
        Button tableLayout = (Button)findViewById(R.id.tableLayout);
         
        
        tableLayout.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UILibActivity.this,TableLayout.class);
                startActivity(intent);
                
            }
        });
        
        Button MenuActivity = (Button)findViewById(R.id.MenuActivity);
         
        
        MenuActivity.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UILibActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });
        
        
        SlideButton myBtn = (SlideButton)findViewById(R.id.slipBtn);  
        myBtn.SetOnChangedListener(new SlideButton.OnChangedListener()
        {
            
            @Override
            public void OnChanged(boolean checkState)
            {
                if (checkState){  
                    
                }else{
                    
                }
                Toast.makeText(UILibActivity.this, " slidebutton "+checkState, Toast.LENGTH_SHORT).show();  
            }
        });  
       
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
    
    
    
  //自定义显示的通知 ，创建RemoteView对象  
    private void showCustomizeNotification() {  
  
        CharSequence title = "i am new";  
        int icon = R.drawable.ic_launcher;  
        long when = System.currentTimeMillis();  
        Notification noti = new Notification(icon, title, when + 10000);  
        noti.flags = Notification.FLAG_INSISTENT;  
          
        // 1、创建一个自定义的消息布局 view.xml  
        // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段  
        RemoteViews remoteView = new RemoteViews(this.getPackageName(),R.layout.notification_notice);  
        remoteView.setImageViewResource(R.id.noticeImage, R.drawable.ic_launcher);  
        remoteView.setTextViewText(R.id.notification_text , "通知类型为：自定义View");  
        noti.contentView = remoteView;  
        // 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)  
         
        //这儿点击后简单启动Settings模块  
        PendingIntent contentIntent = PendingIntent.getActivity  
                         (UILibActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);  
        noti.contentIntent = contentIntent;  
      
        NotificationManager mnotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
        mnotiManager.notify(0, noti);  
  
    }  
  
    // 默认显示的的Notification  
    private void showDefaultNotification() {  
        // 定义Notication的各种属性  
         CharSequence title = "i am new";  
        int icon = R.drawable.ic_launcher;  
        long when = System.currentTimeMillis();  
        Notification noti = new Notification(icon, title, when + 10000);  
        noti.flags = Notification.FLAG_INSISTENT;  
  
        // 创建一个通知  
        Notification mNotification = new Notification();  
  
        // 设置属性值  
        mNotification.icon = R.drawable.ic_launcher;  
        mNotification.tickerText = "NotificationTest";  
        mNotification.when = System.currentTimeMillis(); // 立即发生此通知  
  
        // 带参数的构造函数,属性值如上  
        // Notification mNotification = = new Notification(R.drawable.icon,"NotificationTest", System.currentTimeMillis()));  
  
        // 添加声音效果  
        mNotification.defaults |= Notification.DEFAULT_SOUND;  
  
        // 添加震动,后来得知需要添加震动权限 : Virbate Permission  
        //mNotification.defaults |= Notification.DEFAULT_VIBRATE ;   
  
        //添加状态标志   
  
        //FLAG_AUTO_CANCEL          该通知能被状态栏的清除按钮给清除掉  
        //FLAG_NO_CLEAR                 该通知能被状态栏的清除按钮给清除掉  
        //FLAG_ONGOING_EVENT      通知放置在正在运行  
        //FLAG_INSISTENT                通知的音乐效果一直播放  
        mNotification.flags = Notification.FLAG_INSISTENT ;  
  
        //将该通知显示为默认View  
        PendingIntent contentIntent = PendingIntent.getActivity  
                           (UILibActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);  
        mNotification.setLatestEventInfo(UILibActivity.this, "通知类型：默认View", "一般般哟。。。。",contentIntent);  
          
        // 设置setLatestEventInfo方法,如果不设置会App报错异常  
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
          
        //注册此通知   
        // 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等  
        mNotificationManager.notify(2, mNotification);  
  
    }  
      
    private void removeNotification()  
    {  
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
        // 取消的只是当前Context的Notification  
        mNotificationManager.cancel(2);  
    }
    
}