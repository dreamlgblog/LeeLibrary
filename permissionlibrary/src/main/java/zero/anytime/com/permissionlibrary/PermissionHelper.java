package zero.anytime.com.permissionlibrary;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.List;

import static zero.anytime.com.permissionlibrary.PermissionUtils.getActivity;

public class PermissionHelper {
    //传的参数
    private Object mObject;
    //传的请求码
    private int mRequestCode;
    //请求的权限
    private String[] mRequestPermission;

    private PermissionHelper(Object object){
        this.mObject = object;
    }

    //以什么方式传参数
    //直接传参
    public static void requestPermisssion(Activity activity,int requestCode,String[] permission){
        PermissionHelper.with(activity).requestCode(requestCode).requestPermission(permission).request();
    }


    //链式调用
    public static PermissionHelper with(Activity activity){
        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment){
        return new PermissionHelper(fragment);
    }

    //添加一个请求码
    public PermissionHelper requestCode(int requestCode){
        this.mRequestCode = requestCode;
        return this;
    }

    //请求的权限
    public PermissionHelper requestPermission(String... permission){
        this.mRequestPermission = permission;
        return this;
    }
    //真正的实现
    public void request(){
    //首先需要判断当前版本是不是6.0以及以上
        if(!PermissionUtils.isOverMarshallow()) {
            //如果不是6.0以上 ，直接执行方法， 反射获取方法
            //执行什么方法不确定，那么我们就需要采用注解的方式给方法打一个标记，
            //通过注解的方式， 来获取参数
            PermissionUtils.executeSuccessMethod(mObject,mRequestCode);
            return;
        }
        //如果是6.0  那么首先判断是否授予权限
        List<String> deniePermissions = PermissionUtils.getDeniedPermissions(mObject,mRequestPermission);
        if(deniePermissions.size() == 0){
            //全部都是授予过的
            PermissionUtils.executeSuccessMethod(mObject,mRequestCode);
        }else {
            //如果没有，申请授予
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject)
                    ,deniePermissions.toArray(new String[deniePermissions.size()]),mRequestCode);
        }
    }


    public static void requestPermissionsResult(Object object,int requestCode, String[] permissions) {
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object, permissions);
        if(deniedPermissions.size() == 0){
            //
            PermissionUtils.executeSuccessMethod(object,requestCode);
        }else{
            //申请的权限中，有用户不同意的
            Toast.makeText(PermissionUtils.getActivity(object), "没有授权", Toast.LENGTH_SHORT).show();
            PermissionUtils.executeFailMethod(object,requestCode);
        }
    }
}
