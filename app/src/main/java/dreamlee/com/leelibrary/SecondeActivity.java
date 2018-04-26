package dreamlee.com.leelibrary;

import android.widget.TextView;
import android.widget.Toast;

import com.butterknife.Butterknife;
import com.butterknife.Unbinder;
import com.butterknife.annotations.BindView;
import com.butterknife.annotations.OnClick;

import zero.anytime.com.baselibrary.mvc.BaseActivity;

public class SecondeActivity extends BaseActivity {
    Unbinder mUnbinder;
    @Override
    protected void initData() {
        mUnbinder = Butterknife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, Fragment1.newInstance("hello world"), "f1")
                //.addToBackStack("fname")
                .commit();
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_second);
    }
}
