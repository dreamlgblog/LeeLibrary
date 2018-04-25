package zero.anytime.com.ioclibrary;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BindViewUtils {

    public static void inject(Activity activity){
        inject(new FinderView(activity),activity);
    }

    public static void inject(View view){
        inject(new FinderView(view),view);
    }

    /**
     * 可以绑定Fragment
     * @param view  rootView
     * @param object
     */
    public static void inject(View view,Object object){
        inject(new FinderView(view),object);
    }

    private static void inject(FinderView finderView, Object object){
        injectFiled(finderView,object);
        injectEvent(finderView,object);
    }

    /**
     *
     * @param finderView
     * @param object 是View 或者是Activity
     */
    private static void injectEvent(FinderView finderView, final Object object) {
        Class<?> aClass = object.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (final Method method:
             methods) {
            OnClick click = method.getAnnotation(OnClick.class);
            if(click != null){
                int[] viewIds = click.value();
                if(viewIds.length > 0){
                    for (int viewid:
                         viewIds) {
                        View view = finderView.findViewById(viewid);
                        if(view != null){
                            /*view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    method.setAccessible(true);
                                    try {
                                        method.invoke(object,view);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        try {
                                            method.invoke(object,null);
                                        } catch (IllegalAccessException e1) {
                                            e1.printStackTrace();
                                        } catch (InvocationTargetException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });*/
                            view.setOnClickListener(new DeclaredOnClickListener(method,object));
                        }
                    }
                }
            }
        }
    }

    private static void injectFiled(FinderView finderView, Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:
             fields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null){
                int viewID = bindView.value();
                View view = finderView.findViewById(viewID);
                if(view != null){
                    field.setAccessible(true);
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener{
        private Method mMethod;
        private Object mObject;

        public DeclaredOnClickListener(Method mMethod, Object mObject) {
            this.mMethod = mMethod;
            this.mObject = mObject;
        }

        @Override
        public void onClick(View view) {
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject,view);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
