package lib.ui.share;

import lib.ui.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.sharesdk.framework.AbstractWeibo;

/**
 * @ClassName: AnotherAcitivty.java
 * @Description:
 * @Author JinChao
 * @Date 2013-2-26 下午2:15:32
 * @Copyright: 版权由 HundSun 拥有
 */
public class ShareSdkActivity extends Activity
{
    private ShareSdkActivity me = this;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharesdk_page);
        AbstractWeibo.initSDK(this); 
        
        findViewById(R.id.btnShareAllGui).setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                //String imageHttpUrl = "http://t.inq.com.cn/static/team/2013/0514/13684941617695.jpg";
                
                Intent i = new Intent(me, ShareAllGird.class);
                // 分享时Notification的图标
                i.putExtra("notif_icon", R.drawable.ic_launcher);
                // 分享时Notification的标题
                i.putExtra("notif_title", me.getString(R.string.app_name));
                
                // address是接收人地址，仅在信息和邮件使用，否则可以不提供
                i.putExtra("address", "12345678901");
                // title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供
                i.putExtra("title", me.getString(R.string.share));
                // titleUrl是标题的网络链接，仅在QQ空间使用，否则可以不提供
                //i.putExtra("titleUrl", R.string.website);
                // text是分享文本，所有平台都需要这个字段
                i.putExtra("text", me.getString(R.string.share_content));
                // imagePath是本地的图片路径，所有平台都支持这个字段，不提供，则表示不分享图片
                //i.putExtra("imagePath", MainActivity.TEST_IMAGE);
                
                //i.putExtra("imagePath", "http://t.inq.com.cn/static/team/2013/0514/13684941617695.jpg");
                
                // url仅在人人网和微信（包括好友和朋友圈）中使用，否则可以不提供
                i.putExtra("url", R.string.website);
                // thumbPath是缩略图的本地路径，仅在微信（包括好友和朋友圈）中使用，否则可以不提供
               // i.putExtra("thumbPath", MainActivity.TEST_IMAGE);
                // appPath是待分享应用程序的本地路劲，仅在微信（包括好友和朋友圈）中使用，否则可以不提供
                //i.putExtra("appPath", MainActivity.TEST_IMAGE);
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
                i.putExtra("comment", me.getString(R.string.share));
                // site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供
                //i.putExtra("site", me.getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供
                //i.putExtra("siteUrl", R.string.website);
                
                // 是否直接分享
                i.putExtra("silent", false);
                
                me.startActivity(i);
            }
        });
        
        
    }

}
