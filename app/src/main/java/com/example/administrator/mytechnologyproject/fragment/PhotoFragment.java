package com.example.administrator.mytechnologyproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.activity.HomeActivity;
import com.example.administrator.mytechnologyproject.activity.NewsShowActivity;
import com.example.administrator.mytechnologyproject.adapter.PhotoAdapter;
import com.example.administrator.mytechnologyproject.model.News;
import com.example.administrator.mytechnologyproject.util.DBManager.DBTools;

import java.util.ArrayList;
import java.util.List;


public class PhotoFragment extends Fragment {
    private List<News> newsList;
    private List<String> imageList;
    private GridView gv_image;
    private PhotoAdapter photoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        gv_image = (GridView) view.findViewById(R.id.gv_image);
        photoAdapter = new PhotoAdapter(getContext());
        gv_image.setAdapter(photoAdapter);

        gv_image.setOnItemClickListener(onItemClickListener);

        loadDate();
        return view;

    }

    private void  loadDate(){
        DBTools dbTools = new DBTools(getContext());
        newsList = dbTools.getLocalNews();
        imageList = new ArrayList<>();
        for(int i=0;i<newsList.size();i++){
            imageList.add(newsList.get(i).getIcon());
        }
        if(imageList ==null||imageList.size()<=0){
            ((HomeActivity)getActivity()).showToast("木有新闻图骗");
        }else {
            photoAdapter.appendDataed(imageList,true);
            photoAdapter.updateAdapter();
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle bundle = new Bundle();
            News news = newsList.get(i);
            bundle.putSerializable("news",news);//传news
            ((HomeActivity)getActivity()).openActivity(NewsShowActivity.class,bundle);
        }
    };
}
