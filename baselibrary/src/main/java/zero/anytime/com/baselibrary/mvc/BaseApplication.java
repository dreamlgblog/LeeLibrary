package zero.anytime.com.baselibrary.mvc;

import android.app.Application;

import zero.anytime.com.baselibrary.exceptioncrash.ExceptionCrashHandler;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //设置全局异常捕捉类
        ExceptionCrashHandler.getInstance().init(this);
    }
}
