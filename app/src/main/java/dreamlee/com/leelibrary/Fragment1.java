package dreamlee.com.leelibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.butterknife.Butterknife;
import com.butterknife.annotations.BindView;
import com.butterknife.annotations.OnClick;


public class Fragment1 extends Fragment {
    private static String ARG_PARAM = "param_key";

    private String mParam;
    private Activity mActivity;
    @BindView(R.id.text) TextView view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mParam = getArguments().getString(ARG_PARAM);  //获取参数
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_1, container, false);
        Butterknife.bind(this,root);
        view.setText(mParam);
        return root;
    }
    @OnClick({R.id.text})
    public void TextClick() {
        Toast.makeText(getActivity(), "点击了TextView", Toast.LENGTH_SHORT).show();
    }


    public static Fragment1 newInstance(String str) {
        Fragment1 fragment = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM, str);
        fragment.setArguments(bundle);   //设置参数
        return fragment;
    }
}