package dreamlee.com.leelibrary;

import android.widget.TextView;

import com.butterknife.Butterknife;
import com.butterknife.Unbinder;
import com.butterknife.annotations.BindView;

import zero.anytime.com.baselibrary.mvc.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.text_view) TextView textView;

    @BindView(R.id.text_view2) TextView textView2;
    @BindView(R.id.text_view3) TextView textView3;
    private Unbinder mUnbinder;
    @Override
    protected void initData() {
        textView.setText("mainActivity");
    }

    @Override
    protected void initView() {
        mUnbinder = Butterknife.bind(this);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }
}
