package zero.anytime.com.baselibrary.mvc;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 整个应用的BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局layout
        setContentView();
        // 初始化头部
        initTitle();
        // 初始化界面
        initView();
        // 初始化数据
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void setContentView();

    protected  <T extends View> T findView(int id) {
        return (T)findViewById(id);
    }
}
