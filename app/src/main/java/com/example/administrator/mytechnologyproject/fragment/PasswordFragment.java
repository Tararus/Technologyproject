package com.example.administrator.mytechnologyproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.activity.HomeActivity;
import com.example.administrator.mytechnologyproject.gloable.API;
import com.example.administrator.mytechnologyproject.gloable.Contacts;
import com.example.administrator.mytechnologyproject.model.Password;
import com.example.administrator.mytechnologyproject.model.parser.ParserPassword;

import org.json.JSONObject;


public class PasswordFragment extends Fragment implements View.OnClickListener{
    private EditText et_loginName;
    private Button btn_login;
    private RequestQueue requestQueue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_password, container, false);
        et_loginName= (EditText) view.findViewById(R.id.et_loginName);
        btn_login = (Button) view.findViewById(R.id.btn_login);

        requestQueue = Volley.newRequestQueue(getActivity());

        btn_login.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String loginName = et_loginName.getText().toString();
                if(loginName.length()<=0||loginName == null){
                    ((HomeActivity)getActivity()).showToast("换个姿势再来一次");
                }else {
                    Password(loginName);
                }
        }
    }

    private void Password(String loginName) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.USER_FORGETPASS + "&ver=" + Contacts.VER + "&email=" + loginName, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Password password = ParserPassword.setPassword(jsonObject.toString());
                        if (password==null){
                            ((HomeActivity)getActivity()).showToast("发送失败");
                        }else if (password.getResult() != 0){
                            ((HomeActivity)getActivity()).showToast(password.getExplain());
                        }else {
                            ((HomeActivity)getActivity()).showToast(password.getExplain());
                            ((HomeActivity)getActivity()).showHomeFragment();
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
}
