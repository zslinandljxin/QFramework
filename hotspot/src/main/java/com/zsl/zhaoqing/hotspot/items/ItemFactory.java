package com.zsl.zhaoqing.hotspot.items;

import android.content.Context;

import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.items.meta.ListItem;


/**
 * Created by zsl on 2017/7/25.
 */

public class ItemFactory {
    public static ListItem create(Context context, int viewType){
        ListItem item = null;
        switch (viewType){
            case 0:
                item = new BroadcastCard(ListItem.getView(context, R.layout.card_broadcast));
                break;
            case 1:
                item = new HotTopicCard(ListItem.getView(context, R.layout.card_hot_topic));
                break;
            case 2:
                item = new NewsCardOne(ListItem.getView(context, R.layout.card_news_one));
                break;
            case 3:
                item = new NewsCardTwo(ListItem.getView(context, R.layout.card_news_two));
                break;
            case 4:
                item = new NewsCardThree(ListItem.getView(context, R.layout.card_news_three));
                break;
            case 5:
                item = new AdvCardOne(ListItem.getView(context, R.layout.card_adv_one));
                break;
            case 6:
                item = new AdvCardTwo(ListItem.getView(context, R.layout.card_adv_two));
                break;
            case 7:
                item = new AdvCardThree(ListItem.getView(context, R.layout.card_adv_three));
                break;
            case 8:
                item = new AdvCardFour(ListItem.getView(context, R.layout.card_adv_four));
                break;

            default:break;
        }
        return item;
    }
}
