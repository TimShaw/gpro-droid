package lib.ui.dialog;

import android.lib.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Tip {

    private ImageView image;
    private Dialog mDialog;

    public Tip(Context context) {
        mDialog = new Dialog(context, R.style.dialog);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = -30;
        wl.y = 20;
        window.setAttributes(wl);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(R.layout.tip);
        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        image = (ImageView) mDialog.findViewById(R.id.image);
        image.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
            }
        });
    }

    public void show() {
        mDialog.show();
    }

}