package zero.anytime.com.baselibrary.exceptioncrash;

import android.content.Context;
import android.util.Log;

/**
 * 单利全局异常捕捉
 *
 *  把崩溃的信息保存到内存卡中，等上线之后将内存卡中的崩溃信息上传到服务器。
 *  腾讯的bugly 友盟
 *
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance(){
        if (mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if(mInstance == null)
                    mInstance = new ExceptionCrashHandler();
            }
        }
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context;
        //将此类设置为默认异常处理
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //全局异常
        Log.d("TAG","异常");
        //写入到本地文件，ex  当前版本， 手机信息，
        //拿到手机型号
        //拿到版本号
        //保存本地
        //在首页上传崩溃日志
        //让系统默认处理
        mDefaultExceptionHandler.uncaughtException(t,e);
    }
}
