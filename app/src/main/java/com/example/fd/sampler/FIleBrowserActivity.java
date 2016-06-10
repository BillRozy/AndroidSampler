package com.example.fd.sampler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;


public class FileBrowserActivity extends Activity {

    public final static String mSelectedSamplePath = "com.example.fd.sampler.mSelectedSamplePath";
    public final static String mSelectedSampleName = "com.example.fd.sampler.mSelectedSampleName";
    static final String FILES_DIRECTORY = android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/DrumSampler/";
    static String FILES_DIRECTORY_INTER = android.os.Environment.getDataDirectory()
            .getAbsolutePath() + "/DrumSampler/";
    BrowseFilesAdapter adapter;
    ListView fileListView;
    TextView pathTextView;
    Button backBtn;
    String[] fileArray;
    File[] files;
    File selected;
    ArrayList<File> lastSelected = new ArrayList<>();
    String pathToChosenFile;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OnCreateBrowser", "WORKED");
        setContentView(R.layout.file_browser_activity);
        fileListView = (ListView) this.findViewById(R.id.fileListView);
        pathTextView = (TextView) this.findViewById(R.id.pathTextView);
        pathTextView.setText(FILES_DIRECTORY);
        backBtn = (Button) findViewById(R.id.backButton);

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
            adapter = new BrowseFilesAdapter(this, fileArray);
        }

        fileListView.setAdapter(adapter);
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastSelected.add(selected);
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
                    BrowseFilesAdapter secAdapter = new BrowseFilesAdapter(FileBrowserActivity.this, titles);
                    fileListView.setAdapter(secAdapter);
                    Log.d("ENDED LISTENER"," pos");
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelected.size() != 0) {
                    files = new File[lastSelected.get(lastSelected.size() - 1).listFiles().length];
                    files = lastSelected.get(lastSelected.size() - 1).listFiles();
                    String[] titles = new String[files.length];
                    for (int i = 0; i < files.length; i++) {
                        titles[i] = files[i].getName();
                    }
                    BrowseFilesAdapter secAdapter = new BrowseFilesAdapter(FileBrowserActivity.this, titles);
                    fileListView.setAdapter(secAdapter);
                    Log.d("ENDED LISTENER", " pos");
                    selected = lastSelected.get(lastSelected.size() - 1);
                    lastSelected.remove(lastSelected.size() - 1);
                }
            }
        });




    }

}
