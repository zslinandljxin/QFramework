package com.zsl.zhaoqing.framework.share;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by zsl on 2017/10/19.
 */

public class WXShare implements BaseShare {
    private static final String KEY = "";
    private final int TIMELINE_SUPPORTED_VERSION = 0x21020001;// 微信4.2版本之后才支持朋友圈分享

    private Context mContext;
    private IWXAPI mIwxapi;
    private boolean isPYQ = true;

    public WXShare(Context context){
        this.mContext = context;
        try{
            mIwxapi = WXAPIFactory.createWXAPI(context,KEY);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void share(String type, String title, String des, String url, Bitmap thumb) {
        checkWXInit();
        WXMediaMessage msg = new WXMediaMessage();
        WXMediaMessage.IMediaObject mediaObject = null;
        mergeMessage(type, title, des, url, thumb, msg);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(type);
        req.message = msg;
        req.scene = this.isPYQ ?
                SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mIwxapi.sendReq(req);
    }

    public void setPYQ(boolean isPYQ){
        this.isPYQ = isPYQ;
    }

    private void mergeMessage(String type, String title, String des, String url, Bitmap thumb, WXMediaMessage msg) {
        if (type.equals("text")){
            shareText(msg, des);
        }else if (type.equals("img")){
            shareImage(msg, thumb);
        }else if (type.equals("music")){
            shareMusic(msg,title,des,url, thumb);
        }else if (type.equals("vedio")){
            shareVedio(msg, title, des, url, thumb);
        }else if (type.equals("webpage")){
            shareWebpage(msg, title, des, url, thumb);
        }
    }

    private void checkWXInit(){
        if (null == mIwxapi){
            Toast.makeText(mContext, "微信分享失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mIwxapi.isWXAppInstalled()) {
            Toast.makeText(mContext, "尚未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        int wxSdkVersion = mIwxapi.getWXAppSupportAPI();
        if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
            Toast.makeText(mContext, "微信客户端版本太低", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void shareText(WXMediaMessage msg, String text){
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        msg.mediaObject = textObject;
        msg.description = text;
    }

    private void shareImage(WXMediaMessage msg, Bitmap bmp){
        WXImageObject imageObject = new WXImageObject(bmp);
        msg.mediaObject = imageObject;
        //设置缩略图
//        msg.thumbData =
    }

    private void shareMusic(WXMediaMessage msg, String title, String des, String url, Bitmap bmp){
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = url;

        msg.mediaObject = musicObject;
        msg.title = title;
        msg.description = des;
        //设置缩略图
//        msg.thumbData =
    }

    private void shareVedio(WXMediaMessage msg, String title, String des, String url, Bitmap bmp){
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = url;

        msg.mediaObject = videoObject;
        msg.title = title;
        msg.description = des;
        //设置缩略图
//        msg.thumbData =
    }

    private void shareWebpage(WXMediaMessage msg, String title, String des, String url, Bitmap bmp){
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;

        msg.mediaObject = webpageObject;
        msg.title = title;
        msg.description = des;
        //设置缩略图
//        msg.thumbData =
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }
}
