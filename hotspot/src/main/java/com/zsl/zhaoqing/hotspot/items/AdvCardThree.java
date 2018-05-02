package com.zsl.zhaoqing.hotspot.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.items.meta.ListItem;

/**
 * Created by zsl on 2017/11/14.
 */

public class AdvCardThree extends ListItem<Object>{

    private TextView mTitleTv;
    private ImageView mImgOneShow;
    private ImageView mImgTwoShow;
    private ImageView mImgThreeShow;
    private TextView mTypeTv;
    private ImageView mDelBtn;

    public AdvCardThree(View itemView) {
        super(itemView);
        mTitleTv = itemView.findViewById(R.id.title_tv);
        mImgOneShow = itemView.findViewById(R.id.img1_show);
        mImgTwoShow = itemView.findViewById(R.id.img2_show);
        mImgThreeShow = itemView.findViewById(R.id.img3_show);
        mTypeTv = itemView.findViewById(R.id.type_tv);
        mDelBtn = itemView.findViewById(R.id.del_btn);
        mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void updateItem(int position, Object o) {
        super.updateItem(position, o);
    }
}
