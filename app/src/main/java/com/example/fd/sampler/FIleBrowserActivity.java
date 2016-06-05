package com.example.fd.sampler;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FD on 03.06.2016.
 */
public class FileBrowserActivity extends Activity {

    public final static String mSelectedSamplePath = "com.example.fd.sampler.mSelectedSamplePath";
    public final static String mSelectedSampleName = "com.example.fd.sampler.mSelectedSampleName";
    static final String FILES_DIRECTORY = android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/SampleSounds/";
    private List<File> fileList = new ArrayList<File>();
    ArrayAdapter<String> adapter;
    ListView fileListView;
    TextView pathTextView;
    String[] fileArray;
    File[] files;
    File selected;
    String pathToChosenFile;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OnCreateBrowser", "WORKED");
        setContentView(R.layout.file_browser_activity);
        fileListView = (ListView) this.findViewById(R.id.fileListView);
        pathTextView = (TextView) this.findViewById(R.id.pathTextView);
        pathTextView.setText(FILES_DIRECTORY);

        selected = new File(FILES_DIRECTORY);
        if(!selected.exists()){
            if(new File(FILES_DIRECTORY).mkdir()) {
                selected = new File(FILES_DIRECTORY);
            }
        }
        if(selected.isDirectory()){
            files = new File[selected.listFiles().length];
            files = selected.listFiles();
            fileArray = new String[files.length];
            for(int i = 0; i < files.length; i++){
                fileArray[i] = files[i].getName();
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, fileArray);
        }
        Log.d("FOUND FILES:", fileArray.toString());

        fileListView.setAdapter(adapter);

        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(files[position].isFile()){
                    pathToChosenFile = files[position].getAbsolutePath();
                    String name = files[position].getName();
                    Intent intent = new Intent(FileBrowserActivity.this, MainActivity.class);

                    // в ключ username пихаем текст из первого текстового поля
                    intent.putExtra(mSelectedSamplePath, pathToChosenFile);
                    intent.putExtra(mSelectedSampleName, name);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                else{
                    selected = new File(files[position].getAbsolutePath());
                    files = new File[selected.listFiles().length];
                    files = selected.listFiles();
                    String[] titles = new String[files.length];
                    for(int i = 0; i < files.length; i++){
                        titles[i] = files[i].getName();
                    }
                    ArrayAdapter<String> secAdapter = new ArrayAdapter<String>(FileBrowserActivity.this,
                            android.R.layout.simple_list_item_1, titles);
                    fileListView.setAdapter(secAdapter);
                    Log.d("ENDED LISTENER"," pos");
                }

            }
        });




    }

}
