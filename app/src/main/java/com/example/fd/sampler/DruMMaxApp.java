package com.example.fd.sampler;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;

/**
 * Created by FD on 31.05.2016.
 */
public class DruMMaxApp extends Application {

    final private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 1;
    @Override
    public void onCreate() {
        super.onCreate();
       AssetManager assetManager = getAssets();
        //File dir = new File(FileBrowserActivity.FILES_DIRECTORY);
      //  if (dir.mkdir()) {
      //          Log.d("Created", dir.getAbsolutePath());
    //            myCopy();
      //  }
    }

    private void myCopy(){
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            InputStream indexFile = assetManager.open("assets.index");
            String test = convertStreamToString(indexFile);
            assets = test.split("\\n");
            for(String url : assets){
                File file = new File(url);
                if(file.getName().contains(".")) {
                    copyFile(url);
                }
                else
                {
                    File dir = new File(FileBrowserActivity.FILES_DIRECTORY + url);
                    dir.mkdirs();
                }
            }
        }catch (IOException exc){}
    }

    private void copyFilesToSdCard() {
        copyFileOrDir(""); // copy all files in assets folder in my project
    }

    private void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();

        String assets[] = null;
        String assetsTwo[] = null;
        try {
            InputStream indexFile = assetManager.open("assets.index");
            String test = convertStreamToString(indexFile);
            assets = test.split("\\n");
            assetsTwo = assetManager.list(path);
            Log.d("LIST", assetsTwo[0]);
            Log.i("tag", "copyFileOrDir() "+path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath = FileBrowserActivity.FILES_DIRECTORY + path;
                Log.i("tag", "path="+fullPath);
                File dir = new File(fullPath);
                if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                    if (!dir.mkdirs())
                        Log.i("tag", "could not create dir "+fullPath);
                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (path.equals(""))
                        p = "";
                    else
                        p = path + "/";

                    if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                        copyFileOrDir( p + assets[i]);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.i("tag", "copyFile() "+filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
                newFileName = FileBrowserActivity.FILES_DIRECTORY + filename.substring(0, filename.length()-4);
            else
                newFileName = FileBrowserActivity.FILES_DIRECTORY + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of "+newFileName);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }

    }

    static String convertStreamToString(java.io.InputStream is) {
        Scanner scanner = new Scanner(is);
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }
}
