package zero.anytime.com.ioclibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class FinderView {
    private Activity mActivity;
    private View mView;
    public FinderView(Activity activity){
        this.mActivity = activity;
    }
    public FinderView(View view){
        this.mView = view;
    }

    public View findViewById(int viewID){
        return mActivity != null ? mActivity.findViewById(viewID) : mView.findViewById(viewID);
    }
}
