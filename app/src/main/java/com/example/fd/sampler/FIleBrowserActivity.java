package com.example.fd.sampler;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FD on 03.06.2016.
 */
public class FileBrowserActivity extends Activity {

    static final String FILES_DIRECTORY = android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/SampleSounds/";
    private List<File> fileList = new ArrayList<File>();
    ArrayAdapter<String> adapter;
    ListView fileListView;
    TextView pathTextView;
    String[] fileArray;
    File selected;




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
            File[] files = selected.listFiles();
            fileArray = new String[files.length];
            for(int i = 0; i < files.length; i++){
                fileArray[i] = files[i].getName();
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, fileArray);
        }
        Log.d("FOUND FILES:", fileArray.toString());

        fileListView.setAdapter(adapter);



    }

}
