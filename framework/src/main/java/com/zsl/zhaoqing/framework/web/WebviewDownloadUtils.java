package com.zsl.zhaoqing.framework.web;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * 下载工具类
 * 
 */
public class WebviewDownloadUtils {

	// 存储下载的文件夹名字，默认为在SD卡根目录下
	public static final String DOWNLOAD_FOLDER_NAME = "GbankDownload";
	private WebviewDownloadUtils(){
		
	}
	
	/**
	 * 下载文件
	 *
	 * @param url
	 *            文件地址
	 * @return int -1:下载失败；0:表示正在下载；1:表示开始下载。
	 */
	@SuppressLint("NewApi")
	public static int downloadFile(Context context, String url, String suffix) {
		// 获取本地存储
		try {			
			if (null == url || null == context || TextUtils.isEmpty(suffix)){
				return -1;
			}
			// 获取下载器实例
			DownloadManager downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			String ext_folder = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ (File.separator)
					+ (WebviewDownloadUtils.DOWNLOAD_FOLDER_NAME);
			File folder = new File(ext_folder);
			// 如果文件夹不存在则创建
			if (!folder.exists() || !folder.isDirectory()) {
				folder.mkdirs();
			}
			// 设置下载文件夹及文件名
			String file_name = url
					.substring(url.lastIndexOf(File.separator) + 1);
			// 每一个下载ID对应一个待下载文件的url
			SharedPreferences sp_downloading = context.getSharedPreferences("downloading_files",
			                     					Context.MODE_PRIVATE);
			if (sp_downloading.contains(file_name)){
				//该文件正在下载列表中，不再新起下载任务
				return 0;
			}
			// 创建下载请求实例
			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse(url));

			request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME,
					file_name);
			// 设置下载标题，
			request.setTitle(file_name);
			// 设置下载描述
			request.setDescription("下载中......");
			if (Build.VERSION.SDK_INT >= 11) {
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			}
			request.setVisibleInDownloadsUi(true);
			// 设置下载文件的类型为APK类型
			String mime_type = getMimeType(suffix);
			request.setMimeType(mime_type);
			// 启动下载，并保存下载ID
			long downloadId = downloadManager.enqueue(request);
			// 每一个下载ID对应一个文件名
			SharedPreferences sp = context.getSharedPreferences("wv_download",
			                     					Context.MODE_PRIVATE);
			sp.edit().putString("" + downloadId, file_name).commit();
			
			//记录正在下载的url
			sp_downloading.edit().putString(file_name,""+downloadId).commit();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		//开始下载
		return 1;
	}
	
	private static String getMimeType(String suffix) {
		// TODO Auto-generated method stub
		if (null == suffix){
			return null;
		}
		String type = null;
		if (suffix.equals(".apk")){
			type = ("application/vnd.android.package-archive");
		}else if (suffix.equals(".bmp")){
			type = "image/bmp";
		}else if (suffix.equals(".gif")){
			type = "image/gif";
		}else if (suffix.equals(".jpg")){
			type = "image/jpeg";
		}else if (suffix.equals(".png")){
			type = "image/png";
		}else if (suffix.equals(".mp3")){
			type = "audio/x-mpeg";
		}else if (suffix.equals(".wav")){
			type = "audio/x-wav";
		}else{
			type = "*/*";
		}
		
		return type;
	}

	/**
	 * 下载APK文件
	 *
	 * @param url
	 *            APK文件地址
	 * @return int -1:下载失败；0:表示正在下载；1:表示开始下载。
	 */
	@SuppressLint("NewApi")
	public static int downloadApk(Context context, String url) {
		// 获取本地存储
		try {			
			if (null == url){
				return -1;
			}
			// 获取下载器实例
			DownloadManager downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			String ext_folder = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ (File.separator)
					+ (WebviewDownloadUtils.DOWNLOAD_FOLDER_NAME);
			File folder = new File(ext_folder);
			// 如果文件夹不存在则创建
			if (!folder.exists() || !folder.isDirectory()) {
				folder.mkdirs();
			}
			// 设置下载文件夹及文件名
			String file_name = url
					.substring(url.lastIndexOf(File.separator) + 1);
			// 每一个下载ID对应一个待下载文件的url
			SharedPreferences sp_downloading = context.getSharedPreferences("downloading_files",
			                     					Context.MODE_PRIVATE);
			if (sp_downloading.contains(file_name)){
				//该文件正在下载列表中，不再新起下载任务
				return 0;
			}
			// 创建下载请求实例
			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse(url));

			request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME,
					file_name);
			// 设置下载标题，
			request.setTitle(file_name);
			// 设置下载描述
			request.setDescription("下载中......");
			if (Build.VERSION.SDK_INT >= 11) {
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			}
			request.setVisibleInDownloadsUi(true);
			// 设置下载文件的类型为APK类型
			request.setMimeType("application/vnd.android.package-archive");
			// 启动下载，并保存下载ID
			long downloadId = downloadManager.enqueue(request);
			// 每一个下载ID对应一个文件名
			SharedPreferences sp = context.getSharedPreferences("wv_download",
			                     					Context.MODE_PRIVATE);
			sp.edit().putString("" + downloadId, file_name).commit();
			
			//记录正在下载的url
			sp_downloading.edit().putString(file_name,""+downloadId).commit();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		//开始下载
		return 1;
	}

	/**
	 * 获取指定ID的下载状态
	 * 
	 * @param downloadId
	 *            下载ID
	 * @return 下载状态
	 */
	public static int getInt(Context context, long downloadId) {
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		int result = -1;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getInt(c
						.getColumnIndex(DownloadManager.COLUMN_STATUS));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}

	/**
	 * 安装指定APK文件
	 * 
	 * @param context
	 *            上下文
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static boolean install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			i.setDataAndType(Uri.parse("file://" + filePath),
					"application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return true;
		}
		return false;
	}

}
