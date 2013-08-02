package lib.func.system_service;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.KeyguardManager.OnKeyguardExitResult;
import android.content.Context;
/**
 * 键盘锁管理
 * @author jinchao
 *
 */

public class ManageKeyguard {
    private static KeyguardManager myKM = null;
    private static KeyguardLock myKL = null;

    public static synchronized void initialize(Context context) {
        if (myKM == null) {
            myKM = (KeyguardManager) context
                    .getSystemService(Context.KEYGUARD_SERVICE);
        }
    }
    /**
     * 屏幕解锁
     * @param context
     */
    public static synchronized void disableKeyguard(Context context) {
        // myKM = (KeyguardManager)
        // context.getSystemService(Context.KEYGUARD_SERVICE);
        initialize(context);

        if (myKM.inKeyguardRestrictedInputMode()) {
            myKL = myKM.newKeyguardLock("ManageKeyGuard");
            myKL.disableKeyguard();
            
        } else {
            myKL = null;
        }
    }

    public static synchronized boolean inKeyguardRestrictedInputMode() {
        if (myKM != null) {
            return myKM.inKeyguardRestrictedInputMode();
        }
        return false;
    }
    /**
     * 反解锁屏
     */
    public static synchronized void reenableKeyguard() {
        if (myKM != null) {
            if (myKL != null) {
                myKL.reenableKeyguard();
                myKL = null;
            }
        }
    }

    public static synchronized void exitKeyguardSecurely(
            final LaunchOnKeyguardExit callback) {
        if (inKeyguardRestrictedInputMode()) {
            myKM.exitKeyguardSecurely(new OnKeyguardExitResult() {
                public void onKeyguardExitResult(boolean success) {
                    reenableKeyguard();
                    if (success) {
                        callback.LaunchOnKeyguardExitSuccess();
                    } else {

                    }
                }
            });
        } else {
            callback.LaunchOnKeyguardExitSuccess();
        }
    }

    public interface LaunchOnKeyguardExit {
        public void LaunchOnKeyguardExitSuccess();
    }
}