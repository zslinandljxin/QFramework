package com.zsl.zhaoqing.hotspot.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.items.meta.ListItem;

/**
 * Created by zsl on 2017/11/14.
 */

public class NewsCardThree extends ListItem<Object>{

    private TextView mHotNotV;
    private TextView mTitleTv;
    private TextView mSetTopTv;
    private TextView mNewsSrcTv;
    private TextView mCommentTv;
    private TextView mPubTimeTv;
    private TextView mBeHotTv;

    public NewsCardThree(View itemView) {
        super(itemView);
        mHotNotV = itemView.findViewById(R.id.hot_no_tv);
        mTitleTv = itemView.findViewById(R.id.title_tv);
        mSetTopTv = itemView.findViewById(R.id.set_top_tv);
        mNewsSrcTv = itemView.findViewById(R.id.news_src_tv);
        mCommentTv = itemView.findViewById(R.id.comment_tv);
        mPubTimeTv = itemView.findViewById(R.id.pub_time_tv);
        mBeHotTv = itemView.findViewById(R.id.be_hot_tv);
    }

    @Override
    public void updateItem(int position, Object o) {
        super.updateItem(position, o);
    }
}
