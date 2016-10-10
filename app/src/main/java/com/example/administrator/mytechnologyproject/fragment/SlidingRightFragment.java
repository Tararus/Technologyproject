package com.example.administrator.mytechnologyproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.activity.HomeActivity;
import com.example.administrator.mytechnologyproject.activity.UserActivity;
import com.example.administrator.mytechnologyproject.bean.MyImageLoader;
import com.example.administrator.mytechnologyproject.gloable.Contacts;
import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.Update;
import com.example.administrator.mytechnologyproject.model.parser.ParserUpdate;
import com.example.administrator.mytechnologyproject.util.SharedUtil;
import com.example.administrator.mytechnologyproject.util.UpdateManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;


public class SlidingRightFragment extends Fragment implements View.OnClickListener{
    private ImageView iv_loginImageView,iv_loginedImageView,fun_qq;
    private TextView tv_loginTextView,tv_loginedName,tv_update;
    private RelativeLayout layout_logined,layout_unlogin;
    private boolean isLogin;
    private UpdateManager mUpdateManager;
    private Platform qq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_right, container, false);
        iv_loginImageView = (ImageView) view.findViewById(R.id.iv_loginImageView);
        tv_loginTextView = (TextView) view.findViewById(R.id.tv_loginTextView);
        layout_logined = (RelativeLayout) view.findViewById(R.id.layout_logined);
        layout_unlogin = (RelativeLayout) view.findViewById(R.id.layout_unlogin);
        tv_loginedName = (TextView) view.findViewById(R.id.tv_loginedName);
        iv_loginedImageView = (ImageView) view.findViewById(R.id.iv_loginedImageView);
        tv_update = (TextView) view.findViewById(R.id.tv_update);
        fun_qq= (ImageView) view.findViewById(R.id.fun_qq);


        iv_loginImageView.setOnClickListener(this);
        tv_loginTextView.setOnClickListener(this);
        tv_loginedName.setOnClickListener(this);
        iv_loginedImageView.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        fun_qq.setOnClickListener(this);

        isLogin = SharedUtil.getIsLogined(getActivity());
        changeView();
        return view;
    }

    public void changeView() {
        isLogin = SharedUtil.getIsLogined(getActivity());
        if (isLogin) {
            layout_logined.setVisibility(View.VISIBLE);
            String userUid = SharedUtil.getUserUid(getContext());
            String userPortrait = SharedUtil.getUserPortrait(getContext());
            new MyImageLoader(getContext()).display(userPortrait,iv_loginedImageView);
            tv_loginedName.setText(userUid);
            layout_unlogin.setVisibility(View.GONE);
        }else {
            layout_unlogin.setVisibility(View.VISIBLE);
            layout_logined.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_loginImageView:
            case R.id.tv_loginTextView:
                ((HomeActivity)getActivity()).showLoginFragment();
                break;
            case R.id.tv_loginedName:
            case R.id.iv_loginedImageView:
                ((HomeActivity)getActivity()).openActivity(UserActivity.class);
                break;
            case R.id.tv_update:
                sendQuesone_updateVerify();
                break;
            case R.id.fun_qq:
                setPlatformActionListener();
                break;
        }
    }


    /**
     *授权
     */
    private void setPlatformActionListener(){
            if (qq == null){
                qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(paListener);
                qq.SSOSetting(true);//设置是否客户端授权
                qq.showUser(null);//授权并获取用户信息
            }
    }

    /**
     * 授权成功的回调
     */
    private PlatformActionListener paListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            String userName = qq.getDb().getUserName();
            String userIcon = qq.getDb().getUserIcon();

            SharedUtil.saveUserName(getContext(),userName);
            SharedUtil.saveUserIcon(getContext(),userIcon);
            SharedUtil.setIsLogined(getContext(),true);
            SharedUtil.saveIsThirdPartyLogin(getContext(),true);
            ((HomeActivity)getActivity()).openActivity(UserActivity.class);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };

    /**
     * 验证软件版本
     */
    /**
     * 验证是否需要更新
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                BaseEntity<Update> baseEntity = ParserUpdate.
                        getUpdte(msg.obj.toString());
                if (baseEntity.getStatus() == 0) {
                    Update update = baseEntity.getData();
                    if (Contacts.VER == update.getVersion()) {
                        ((HomeActivity)getActivity()).showToast("版本已经是最新的了！");
                    } else {
                        String url = update.getLink();
                        mUpdateManager = new UpdateManager(getContext(),url);
                        mUpdateManager.chenkUpdateInfo();
                        ((HomeActivity)getActivity()).showToast("版本更新成功了！");
                    }
                } else {
                    ((HomeActivity)getActivity()).showToast("获取更新信息失败，请检查网络连接");
                }
            }
        }
    };



    private void sendQuesone_updateVerify() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.3.80:8080/test/update.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = stringBuffer.toString();
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
