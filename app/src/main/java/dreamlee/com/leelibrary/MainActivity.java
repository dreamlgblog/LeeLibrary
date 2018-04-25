package dreamlee.com.leelibrary;

import android.widget.TextView;

import com.butterknife.annotations.BindView;

import zero.anytime.com.baselibrary.mvc.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.text_view)
    private TextView textView;

    @BindView(R.id.text_view2)
    private TextView textView2;
    @BindView(R.id.text_view3)
    private TextView textView3;

    @Override
    protected void initData() {
        textView.setText("mainActivity");
    }

    @Override
    protected void initView() {
        //Butterknife.bind(this);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }
}
