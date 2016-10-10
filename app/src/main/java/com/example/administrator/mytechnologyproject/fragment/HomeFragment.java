package com.example.administrator.mytechnologyproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.activity.HomeActivity;
import com.example.administrator.mytechnologyproject.activity.NewsShowActivity;
import com.example.administrator.mytechnologyproject.adapter.NewTypeAdapter;
import com.example.administrator.mytechnologyproject.adapter.NewsListAdapter;
import com.example.administrator.mytechnologyproject.model.News;
import com.example.administrator.mytechnologyproject.model.SubType;
import com.example.administrator.mytechnologyproject.model.NewsType;
import com.example.administrator.mytechnologyproject.gloable.API;
import com.example.administrator.mytechnologyproject.gloable.Contacts;
import com.example.administrator.mytechnologyproject.model.parser.ParserNews;
import com.example.administrator.mytechnologyproject.model.parser.ParserNewsType;
import com.example.administrator.mytechnologyproject.util.CommonUtil;
import com.example.administrator.mytechnologyproject.util.DBManager.DBTools;
import com.example.administrator.mytechnologyproject.view.HorizontalListView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HorizontalListView horizontalListView;
    private ListView news_list_view;
    private RequestQueue requestQueue;
    private List<SubType> datalist;
    private List<News> newsList;
    private NewTypeAdapter adapter;
    private NewsListAdapter newsListAdapter;
    private DBTools dbTools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbTools = new DBTools(getContext());

        horizontalListView = (HorizontalListView) view.findViewById(R.id.horizontalView);

        adapter = new NewTypeAdapter(getContext());
        horizontalListView.setAdapter(adapter);

        news_list_view = (ListView) view.findViewById(R.id.news_list_view);

        newsListAdapter = new NewsListAdapter(getContext());
        news_list_view.setAdapter(newsListAdapter);



        requestQueue = Volley.newRequestQueue(getContext());//实例化一个RequestQueue对象


        loadTileTypeData();
        loadHomeList();
        news_list_view.setOnItemClickListener(onItemClickListener);

        return view;
    }

    /***
     * 加在标题栏
     *
     */
    private void  loadTileTypeData(){
        if(CommonUtil.isNetworkAvailable(getActivity())){
            sendRequestData_newsSort();
        }else {
            List<SubType> list = dbTools.getLocalType();
            if(list != null && list.size()>0){
                adapter.appendDataed(datalist,true);
                adapter.updateAdapter();
            }else {
                ((HomeActivity)getActivity()).showToast("请检查当前网络");
            }
        }
    }


    /**
     * 加载首页数据
     */
    private  void  loadHomeList(){
        if (CommonUtil.isNetworkAvailable(getActivity())){
            sendRequestData_newsList();
        }else {
            newsList = dbTools.getLocalNews();
            if (newsList!=null && newsList.size()>0){
                newsListAdapter.appendDataed(newsList,true);
                newsListAdapter.updateAdapter();
            }else {
                ((HomeActivity)getActivity()).showToast("请检查当前网络");
            }
        }
    }


    /**
     * 请求新闻类型网络数据
     */
    private void sendRequestData_newsSort() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.NEWS_SORT
                + "ver=" + Contacts.VER + "&imei=" + CommonUtil.getIMEI(
                getContext()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        List<NewsType> list = ParserNewsType.getNewsTypeList(jsonObject.toString());
                        datalist = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            datalist.addAll(list.get(i).getSubgrp());
                        }
                        for (int i = 0;i < list.size();i++){
                            dbTools.saveLocalType(datalist.get(i));
                        }
                        adapter.appendDataed(datalist,true);
                        adapter.updateAdapter();
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

    private void sendRequestData_newsList() {
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, API.NEWS_LIST +
                "ver=" + Contacts.VER + "dsf" + "&subid=" + 1
                + "&dir=" + 1 + "&nid=" + 0 + "&stamp=" + 20160828 + "&cnt=" + 20,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        newsList = ParserNews.getNewsList(jsonObject.toString());
                        for (int i = 0;i<newsList.size();i++){
                            dbTools.saveLocalNews(newsList.get(i));
                        }
                        newsListAdapter.appendDataed(newsList,true);
                        newsListAdapter.updateAdapter();
                        ((HomeActivity)getActivity()).cancleDialong();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest2);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
            Bundle bundle = new Bundle();
            News news = newsListAdapter.getItem(i);
            bundle.putSerializable("news",news);
            ((HomeActivity)getActivity()).openActivity(NewsShowActivity.class,bundle);
        }
    };
}
