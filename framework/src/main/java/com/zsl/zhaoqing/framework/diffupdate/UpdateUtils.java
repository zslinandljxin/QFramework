package com.zsl.zhaoqing.framework.diffupdate;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by zsl on 2017/10/23.
 */

public class UpdateUtils {

    /* 解压
     */
    public static void decompression(String path) {

        try {

            ZipInputStream inZip = new ZipInputStream(new FileInputStream(path));
            ZipEntry zipEntry;
            String szName;
            try {
                while((zipEntry = inZip.getNextEntry()) != null) {

                    szName = zipEntry.getName();
                    if(zipEntry.isDirectory()) {

                        szName = szName.substring(0,szName.length()-1);
                        File folder = new File(path + File.separator + szName);
                        folder.mkdirs();

                    }else{

                        File file1 = new File(path + File.separator + szName);
                        boolean s = file1.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file1);
                        int len;
                        byte[] buffer = new byte[1024];

                        while((len = inZip.read(buffer)) != -1) {
                            fos.write(buffer, 0 , len);
                            fos.flush();
                        }
                        fos.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            inZip.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 将.pat文件转换为String
     * @param patPath 下载的.pat文件所在目录
     * @return
     */
    public static String getStringFromPat(String patPath) {

        FileReader reader = null;
        String result = "";
        try {
            reader = new FileReader(patPath);
            int ch = reader.read();
            StringBuilder sb = new StringBuilder();
            while (ch != -1) {
                sb.append((char)ch);
                ch  = reader.read();
            }
            reader.close();
            result = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /* 获取Assets目录下的bundle文件
     * @return
     */
    public static String getJsBundleFromAssets(Context context, String assetFile) {

        String result = "";
        try {

            InputStream is = context.getAssets().open(assetFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer,"UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 合并原始jsBundle文件和新的jsPatch
     * @param context
     * @param assetFile 原始js文件
     * @param jsPatch  新的js插件
     * @param newJsBundle 新的js文件
     */
    private void mergePatAndAsset(Context context,String assetFile, String jsPatch, String newJsBundle) {

        // 1.获取Assets目录下的bunlde
        String assetsBundle = getJsBundleFromAssets(context.getApplicationContext(), assetFile);
        // 2.获取.pat文件字符串
        String patcheStr = getStringFromPat(jsPatch);
        // 3.初始化 dmp
        DiffMatchPatch dmp = new DiffMatchPatch();
        // 4.转换pat
        LinkedList<DiffMatchPatch.Patch> pathes = (LinkedList<DiffMatchPatch.Patch>) dmp.patch_fromText(patcheStr);
        // 5.与assets目录下的bundle合并，生成新的bundle
        Object[] bundleArray = dmp.patch_apply(pathes,assetsBundle);
        // 6.保存新的bundle
        try {
            Writer writer = new FileWriter(newJsBundle);
            String newBundle = (String) bundleArray[0];
            writer.write(newBundle);
            writer.close();
            // 7.删除.pat文件
            File patFile = new File(jsPatch);
            patFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
