package dreamlee.com.leelibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.butterknife.Butterknife;
import com.butterknife.Unbinder;
import com.butterknife.annotations.BindView;
import com.butterknife.annotations.OnClick;

import zero.anytime.com.baselibrary.mvc.BaseActivity;
import zero.anytime.com.permissionlibrary.PermissionFail;
import zero.anytime.com.permissionlibrary.PermissionHelper;
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
        PermissionHelper.with(this).requestPermission(Manifest.permission.CAMERA)
                .requestCode(CALL_PHONE_CODE)
                .request();
    }


    @PermissionSuccess(requestCode = CALL_PHONE_CODE)
    public void CallPhone() {
        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18298194316"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用摄像头action
        startActivity(intent);
    }

    @PermissionFail(requestCode = CALL_PHONE_CODE)
    public void CallFail(){
        Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
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
