package dreamlee.com.leelibrary;

import android.annotation.SuppressLint;
import android.widget.TextView;
import android.widget.Toast;

import com.butterknife.Butterknife;
import com.butterknife.Unbinder;
import com.butterknife.annotations.BindView;
import com.butterknife.annotations.OnClick;

import zero.anytime.com.baselibrary.mvc.BaseActivity;
import zero.anytime.com.permissionlibrary.PermissionFail;
import zero.anytime.com.permissionlibrary.PermissionSuccess;

public class MainActivity extends BaseActivity {

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.text_view2)
    TextView textView2;
    @BindView(R.id.text_view3)
    TextView textView3;
    private Unbinder mUnbinder;
    private static final int CALL_PHONE_CODE = 1000;

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

    @OnClick(R.id.btn_phone)
    protected void CellPhone() {

    }


    @SuppressLint("MissingPermission")
    @PermissionSuccess(requestCode = CALL_PHONE_CODE)
    public void CallPhone(){

    }

    @PermissionFail(requestCode = CALL_PHONE_CODE)
    public void CallFail(){
        Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.text_view,R.id.text_view3})
    public void TextClick(){
        Toast.makeText(this, "点击了TextView", Toast.LENGTH_SHORT).show();
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
