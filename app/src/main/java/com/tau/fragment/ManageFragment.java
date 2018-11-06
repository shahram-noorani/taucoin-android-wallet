package com.mofei.tau.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mofei.tau.R;
import com.mofei.tau.adapter.HistoryEventRecycleAdapter;
import com.mofei.tau.view.CustomToolBar;
import com.mofei.tau.view.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {


    private CustomToolBar mMainCustomToolBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    // private RecyclerView historyRecyclerView;
    private SwipeRecyclerView swipeRecyclerView;
    private HistoryEventRecycleAdapter historyEventRecycleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initEvent(view);

    }

    private void initEvent(View view) {

        swipeRecyclerView=view.findViewById(R.id.history_recycleView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        //Set the background color of the drop-down progress bar, default white.
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //Set the color theme of the drop-down progress bar, the parameter is a variable parameter, and is the resource ID. Set up to four different colors, and each turn displays a color.
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED);
        //To set up listeners, you need to override the onRefresh () method, which is called when the top drop-down occurs, which implements the logic of requesting data, sets the drop-down progress bar to disappear, and so on.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);

            }
        });

        List<String> list=new ArrayList<>();
        for (int i=0;i<50;i++){
            list.add("data" +i);
        }

        historyEventRecycleAdapter=new HistoryEventRecycleAdapter(getActivity(),list);
        /**
         * 设置布局方向
         * Setting layout direction
         */
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        swipeRecyclerView.setLayoutManager(layoutManager);
        /**
         * 设置间隔线
         * Setting intervals
         */
        swipeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        //
        /**
         * 设置删除动画
         * Set delete animation
         */
        swipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * 设置适配器
         * Setup adapter
         */
        swipeRecyclerView.setAdapter(historyEventRecycleAdapter);
        /**
         * 设置右删除监听
         * Set right delete listen.
         */
        swipeRecyclerView.setRightClickListener(new SwipeRecyclerView.OnRightClickListener() {
            @Override
            public void onRightClick(int position, String id) {

               // showToast("删除数据"+position);

            }
        });

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
