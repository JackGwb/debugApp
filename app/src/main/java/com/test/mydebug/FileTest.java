package com.test.mydebug;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileTest {
    private final static String TAG = FileTest.class.getSimpleName();

    public FileTest(Context context) {
        mContext = context;
    }

    public void showPath() {
        String a = Environment.getDataDirectory().toString();
        String b = mContext.getFilesDir().getAbsolutePath();
        String c = mContext.getCacheDir().getAbsolutePath();
        String d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        String e = Environment.getExternalStorageDirectory().getPath();
        String f = mContext.getExternalFilesDir("Documents").getPath();
        Log.i(TAG, "Environment.getDataDirectory().toString():-----" + a);
        Log.i(TAG, "getFilesDir().getAbsolutePath():----- " + b);
        Log.i(TAG, "getCacheDir().getAbsolutePath():----- " + c);
        Log.i(TAG, "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath():----- " + d);
        Log.i(TAG, "Environment.getExternalStorageDirectory().getPath():----- " + e);
        Log.i(TAG, "getExternalFilesDir(\"Documents\").getPath():----- " + f);
    }

    public File testCreate(String filedirpath, String name) {
        File fileDir = new File(filedirpath);
        if (!fileDir.exists()) {
            try {
                fileDir.mkdir();
                Log.i(TAG, "文件夹创建成功    TestFilePathExternalData（）");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "文件夹创建错误   TestFilePathExternalData()" + e.getMessage());
            }
        }
        //拼接字符串  文件的存储路径
        String fileName = filedirpath + File.separator + name;
        //文件夹路径和文件路径   判断文件是否存在
        File subFile = new File(fileName);
        if (subFile.exists()) {
            subFile.setWritable(true);
            boolean readable = subFile.canRead();
            boolean writeable = subFile.canWrite();
            Log.i(TAG, "文件创建成功" + "readable:" + readable + " writeable:" + writeable);
        } else {
            try {
                subFile.createNewFile();
            } catch (IOException e) {
                Log.i(TAG, "文件创建出错  " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return subFile;
    }

    public void testWrite(String filedirpath, String name) {
        BufferedWriter bufferedWriter = null;
        FileOutputStream fileOutputStream = null;
        File f = testCreate(filedirpath, name);
        Log.d(TAG, "befor write File length = " + f.length());

        try {
            //fileOutputStream = openFileOutput(FileName, Context_Mode);  contains a path separator 报错
            fileOutputStream = new FileOutputStream(f);
            //解决输入中文的问题
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "utf-8"));
            bufferedWriter.write("12345678");
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "写入数据出错 " + e.getMessage());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d(TAG, "after write File length = " + f.length());
    }

    public void testRead(String filedirpath, String name) {
        try {
            File f = testCreate(filedirpath, name);
            String fileContent = null;
            InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent += line;
            }
            reader.close();
            read.close();
            Log.i(TAG, fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

    public void test() {
        String cacheDir = mContext.getCacheDir().getAbsolutePath();
        String file = "gwbtest.txt";
        File f = testCreate(cacheDir, file);

        testWrite(cacheDir, file);
        testRead(cacheDir, file);
    }
    private Context mContext;
}
