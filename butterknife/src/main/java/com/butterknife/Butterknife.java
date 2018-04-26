package com.butterknife;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Constructor;

public class Butterknife {
    public static Unbinder bind(Activity activity) {
        try {
            Class<? extends Unbinder> bindClassName = (Class<? extends Unbinder>) Class.forName(activity.getClass().getName() + "_ViewBinding");
            //获取xxxx_ViewBind 构造函数（activity，view）
            Constructor<? extends Unbinder> bindConstructor = bindClassName.getDeclaredConstructor(activity.getClass(),View.class);
            Unbinder unbinder = bindConstructor.newInstance(activity,activity.getWindow().getDecorView());

            return unbinder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Unbinder.EMPTY;
    }


    public static Unbinder bind(Fragment fragment1, View root) {
        try {
            Class<? extends Unbinder> forName =(Class<? extends Unbinder>) Class.forName(fragment1.getClass().getName() + "_ViewBinding");
            Constructor<? extends Unbinder> constructor = forName.getDeclaredConstructor(fragment1.getClass(),View.class);
            Unbinder binder = constructor.newInstance(fragment1,root);

            return binder;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return Unbinder.EMPTY;
    }
}
