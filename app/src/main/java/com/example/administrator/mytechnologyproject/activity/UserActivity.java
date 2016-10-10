package com.example.administrator.mytechnologyproject.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.adapter.LoginAdapter;
import com.example.administrator.mytechnologyproject.base.MyBaseActivity;
import com.example.administrator.mytechnologyproject.bean.MyImageLoader;
import com.example.administrator.mytechnologyproject.gloable.API;
import com.example.administrator.mytechnologyproject.gloable.Contacts;
import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.User;
import com.example.administrator.mytechnologyproject.model.parser.ParserUser;
import com.example.administrator.mytechnologyproject.util.CommonUtil;
import com.example.administrator.mytechnologyproject.util.SharedUtil;

import org.json.JSONObject;

public class UserActivity extends MyBaseActivity implements View.OnClickListener{
    private static final String TAG = "UserActivity";
    private RequestQueue requestQueue;
    private TextView tv_user_name,tv_user_integral,comment_count;
    private LoginAdapter adapter;
    private ListView lv_user_login_list;
    private ImageView iv_user_back,iv_user_icon;
    private Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        requestQueue = Volley.newRequestQueue(this);

        initview();
        initData();
    }

    private void  initData(){
        adapter = new LoginAdapter(this);
        lv_user_login_list.setAdapter(adapter);

        boolean isLogined = SharedUtil.getIsLogined(this);
        boolean isThirdpartyogin = SharedUtil.getIsThirdPartyLogin(this);
        if (isLogined){
            if (isThirdpartyogin){
                new MyImageLoader(getBaseContext()).display(SharedUtil.getUserPortrait(getBaseContext()),iv_user_icon);
                tv_user_name.setText(SharedUtil.getUserUid(getBaseContext()));
            }else {

                String token = SharedUtil.getTokey(this, "token");
                String url = API.USER_CENTER_DATA + "ver=" + Contacts.VER + "&imei=" +
                        CommonUtil.getIMEI(this) + "&token=" + token;
                requestUserData(url,null);
            }
        }
    }

    private  void initview(){

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_integral = (TextView) findViewById(R.id.tv_user_integral);
        comment_count = (TextView) findViewById(R.id.comment_count);
        lv_user_login_list = (ListView) findViewById(R.id.lv_user_login_list);
        iv_user_back = (ImageView) findViewById(R.id.iv_user_back);
        iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
        btn_back = (Button) findViewById(R.id.btn_back);

        iv_user_back.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void requestUserData(String url, final User user) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        BaseEntity<User> userBaseEntity = ParserUser.getLoginSuccInfo
                                (jsonObject.toString());
                        int status = userBaseEntity.getStatus();
                        if (status != 0) {
                            showToast("用户数据请求错误！");

                        } else {
                            User user = userBaseEntity.getData();
                            SharedUtil.saveUserInfo(getBaseContext(),user);
                            new MyImageLoader(getBaseContext()).display(user.getPortrait(),iv_user_icon);
                            tv_user_name.setText(user.getUid());
                            tv_user_integral.setText("用户积分："+user.getIntegration());
                            comment_count.setText(user.getComnum() + "");
                            adapter.appendDataed(user.getLoginlog(),true);
                            adapter.updateAdapter();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void dialog() {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        SharedPreferences preferences = getSharedPreferences("user",
                                MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        SharedUtil.clearAllInfos(UserActivity.this);
                        Intent intent = new Intent(UserActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(UserActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_user_back:
                Intent intent = new Intent(UserActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_back:
                dialog();
                break;
        }
    }


}
