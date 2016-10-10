package com.example.administrator.mytechnologyproject.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.adapter.CommentsAdapter;
import com.example.administrator.mytechnologyproject.base.MyBaseActivity;
import com.example.administrator.mytechnologyproject.gloable.API;
import com.example.administrator.mytechnologyproject.gloable.Contacts;
import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.Commenter;
import com.example.administrator.mytechnologyproject.model.News;
import com.example.administrator.mytechnologyproject.model.Register;
import com.example.administrator.mytechnologyproject.model.parser.ParserComment;
import com.example.administrator.mytechnologyproject.model.parser.ParserUser;
import com.example.administrator.mytechnologyproject.util.CommonUtil;
import com.example.administrator.mytechnologyproject.util.SharedUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class CommentsActivity extends MyBaseActivity implements View.OnClickListener{
    private static final String TAG = "CommentsActivity";
    private EditText ed_commenter;
    private Button btn_go;
    private ListView lv_content;
    private RequestQueue requestQueue;
    private News news;
    private List<Commenter> comments;
    private CommentsAdapter commentsAdapter;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getNews();
        Initiv();

        commentsAdapter = new CommentsAdapter(this);
        lv_content.setAdapter(commentsAdapter);

        iv_back.setOnClickListener(this);
        btn_go.setOnClickListener(this);

        send_comment();
    }



    private void Initiv(){
        ed_commenter = (EditText) findViewById(R.id.ed_commenter);
        btn_go = (Button) findViewById(R.id.btn_go);
        lv_content = (ListView) findViewById(R.id.lv_content);
        iv_back = (ImageView) findViewById(R.id.iv_back_comment);


    }
    private void getNews(){
        Bundle bundle = getIntent().getExtras();
        news = (News) bundle.getSerializable("news");
    }

    /**
     * 获取评论
     */
    private void send_comment() {
        requestQueue = Volley.newRequestQueue(this);
        String url = API.CMT_LIST + "ver=" + Contacts.VER + "&nid=" + news.getNid()
                + "&type=" + 1 + "&stamp=" + 20161009 + "&cid=" + 20 + "&dir=" + 1 + "&cnt=" + 20;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                comments = ParserComment.getCommentList(jsonObject.toString());
                commentsAdapter.appendDataed(comments,true);
                commentsAdapter.updateAdapter();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                Intent intent = new Intent(CommentsActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_go:
                String content = ed_commenter.getText().toString();
                String token = SharedUtil.getTokey(this,"token");
                if (content==null){
                    showToast("评论内容不能为空");
                }else if(token == null){
                showToast("请登录后重试");
                }else{
                    try {
                        String ss = URLEncoder.encode(content, "utf-8");
                        toComment(ss,token);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    /**
     * 发表评论
     * @param content
     * @param token
     */
    private void toComment(String content,String token) {
        requestQueue = Volley.newRequestQueue(this);
        String url = API.CMT_COMMIT + "ver=" + Contacts.VER +"&nid="+news.getNid()+ "&token=" + token
                + "&imei=" + CommonUtil.getIMEI(this)+"&ctx="+content;
        Log.i(TAG, "toComment: 网址===================================================="+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                       String res = ParserComment.toCommentList(jsonObject.toString());
                        if(res == null){
                            showToast("评论失败");
                        }else {
                            showToast(res);
                            ed_commenter.setText(null);
                            send_comment();
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
