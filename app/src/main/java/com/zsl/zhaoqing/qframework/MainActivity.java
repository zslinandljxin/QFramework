package com.zsl.zhaoqing.qframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.TextView;

import com.zsl.zhaoqing.framework.customview.draggrid.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/26.
 */

public class MainActivity extends AppCompatActivity{

//    int[] resId = {R.layout.xiala, R.layout.sifang, R.layout.jiazai, R.layout.finish};
    private DragGridView mDragView;
    private List<String> mData = new ArrayList<>();
//    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rect_text);
        TextView lie = (TextView) findViewById(R.id.text);
        String str = "<font color='#f89422'>2</font> 微软Surface Book 2正式开卖：12388元起";
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new UnderLineG(), 2, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        lie.setText(ss);
//        lie.setText(Html.fromHtml(str));
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        mRecyclerView = (RecyclerView) findViewById(R.id.paper_container);
//        LinearLayoutManager lpm = new LinearLayoutManager(this);
//        SwipeLayout refresh = (SwipeLayout) findViewById(R.id.refresh);
//        refresh.setHeader(new HeaderView(this));
//        mDragView = (DragGridView) findViewById(R.id.drag_view);
//        for (int i=0; i < 20; i++){
//            mData.add(i+" ");
//        }
//        MyAdapter adapter = new MyAdapter(mData);
//        mRecyclerView.setLayoutManager(lpm);
//        mRecyclerView.setAdapter(adapter);
//        mDragView.setAdapter(new MyDragAdapter(this, mData));

    }
}
