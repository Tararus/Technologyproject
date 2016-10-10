package com.example.administrator.mytechnologyproject.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.Text;
import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.activity.HomeActivity;
import com.example.administrator.mytechnologyproject.activity.UserActivity;
import com.example.administrator.mytechnologyproject.gloable.API;
import com.example.administrator.mytechnologyproject.gloable.Contacts;
import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.Register;
import com.example.administrator.mytechnologyproject.model.parser.ParserUser;
import com.example.administrator.mytechnologyproject.util.CommonUtil;
import com.example.administrator.mytechnologyproject.util.SharedUtil;

import org.json.JSONObject;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private Button btn_register, btn_login;
    private EditText et_loginName, et_loginPassword;
    private RequestQueue requestQueue;
    private Dialog noticeDialog;
    private TextView tv_password,tv_fuwu;
    private View view;

    private PopupWindow popupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        et_loginName = (EditText) view.findViewById(R.id.et_loginName);
        et_loginPassword = (EditText) view.findViewById(R.id.et_loginPassword);
        tv_password = (TextView) view.findViewById(R.id.tv_password);
        tv_fuwu = (TextView) view.findViewById(R.id.tv_fuwu);



        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_password.setOnClickListener(this);
        tv_fuwu.setOnClickListener(this);

        showPopupWindow();

        requestQueue = Volley.newRequestQueue(getActivity());

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                ((HomeActivity) getActivity()).showRegisterFragment();
                break;
            case R.id.btn_login:
                String name = et_loginName.getText().toString();
                String pwd = et_loginPassword.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.verifyPassword(pwd)) {
                    Toast.makeText(getActivity(), "请输入6-16位字母与数字格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = API.USER_LONGIN + "ver=" + Contacts.VER + "&uid=" + name + "&pwd=" +
                        pwd + "&device=" + "0";
                requestLogin(url);
                break;
            case R.id.tv_password:
                showNoticeDialog();
                break;
            case R.id.tv_fuwu:
                if(popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }else if (popupWindow!=null) {
                    popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                }
                break;
        }
    }

    private void requestLogin(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        BaseEntity<Register> baseRegister = ParserUser.getRegisterInfo(jsonObject.toString());
                        int result = baseRegister.getStatus();
                        if (result == 0) {
                            SharedUtil.saveRegisterInfo(baseRegister,getContext());
                            startActivity(new Intent(getActivity(), UserActivity.class));
                        }
                        if (result == -1) {
                            Toast.makeText(getActivity(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
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

    private void showNoticeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("无法登陆");
        builder.setPositiveButton("找回密码", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ((HomeActivity) getActivity()).showPasswordFragment();
            }
        });
        builder.setNegativeButton("不管了，放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showPopupWindow() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_password_item,null);
        TextView tv = (TextView) view.findViewById(R.id.tv_favoritee);
        TextView tv_shard= (TextView) view.findViewById(R.id.tv_shardd);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setOnClickListener(this);
        tv_shard.setOnClickListener(this);
    }
}
