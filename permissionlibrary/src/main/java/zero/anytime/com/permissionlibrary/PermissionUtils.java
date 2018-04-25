package zero.anytime.com.permissionlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    private PermissionUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是不是6.0以上的版本
     * @return
     */
    public static boolean isOverMarshallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public static void executeSuccessMethod(Object object,int requestCode) {

        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method:
             methods) {
            PermissionSuccess annotation = method.getAnnotation(PermissionSuccess.class);
            if (annotation != null) {
                int code = annotation.requestCode();
                if(code == requestCode){
                   executeMethod(object,method);
                }
            }
        }
    }

    private static void executeMethod(Object object,Method method) {
        //反射执行方法有两个参数，第一个是该方法是属于那个类，第二参数是传参数。如果是method（String  ）
        try {
            method.setAccessible(true);
            method.invoke(object,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果拒绝的话
     * @param object
     * @param requestCode
     */
    public static void executeFailMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        if (methods != null){
            for (Method method:
                    methods) {
                PermissionFail permissionFail = method.getAnnotation(PermissionFail.class);
                if(permissionFail != null){
                    int code = permissionFail.requestCode();
                    if(requestCode == code){
                        executeMethod(object,method);
                    }
                }
            }
        }
    }

    /**
     * 获取没有授予的权限
     * @param mObject
     * @param mRequestPermission
     * @return 没有授予过的权限。
     */
    public static List<String> getDeniedPermissions(Object mObject, String[] mRequestPermission) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission:
             mRequestPermission) {
            //把没有授予的权限加入集合

            if(ContextCompat.checkSelfPermission(getActivity(mObject),requestPermission)
                    == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions;
    }

    public  static Activity getActivity(Object mObject) {
        if(mObject instanceof Activity){
            return (Activity) mObject;
        }

        if(mObject instanceof Fragment){
            return ((Fragment)mObject).getActivity();
        }
        return null;
    }
}
