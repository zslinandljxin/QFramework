package com.zsl.zhaoqing.hotspot.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.items.meta.ListItem;

/**
 * Created by zsl on 2017/11/14.
 */

public class AdvCardFour extends ListItem<Object>{

    private TextView mTitleTv;
    private ImageView mImgShow;
    private TextView mTypeTv;
    private ImageView mDelBtn;
    private TextView mOpenAPP;

    public AdvCardFour(View itemView) {
        super(itemView);
    }

    @Override
    public void updateItem(int position, Object o) {
        super.updateItem(position, o);
        mTitleTv = itemView.findViewById(R.id.title_tv);
        mImgShow = itemView.findViewById(R.id.img_show);
        mTypeTv = itemView.findViewById(R.id.type_tv);
        mOpenAPP = itemView.findViewById(R.id.open_app);
        mDelBtn = itemView.findViewById(R.id.del_btn);
        mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
