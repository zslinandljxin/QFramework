package com.zsl.zhaoqing.framework.diffupdate;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/10/23.
 */

public class ProducePatch {

    public static void main(String[] args) {
        String o = UpdateUtils.getStringFromPat("E:/old.bundle");
        String n = UpdateUtils.getStringFromPat("E:/new.bundle");

        // 对比
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diffs = dmp.diff_main(o, n);

        // 生成差异补丁包
        LinkedList<DiffMatchPatch.Patch> patches = dmp.patch_make(diffs);

        // 解析补丁包
        String patchesStr = dmp.patch_toText(patches);

        FileOutputStream os = null;
        try {
            // 将补丁文件写入到某个位置
            File file = new File("E:/patches.pat");
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            os = new FileOutputStream(file);
            os.write(patchesStr.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os != null){
                try {
                    os.close();
                    os = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
