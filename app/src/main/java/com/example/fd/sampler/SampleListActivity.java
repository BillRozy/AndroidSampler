package com.example.fd.sampler;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SampleListActivity extends Activity {

    private ArrayList<SoundSample> tempSongList;
    public final static String mSelectedSamplePath = "com.example.fd.sampler.mSelectedSamplePath";
    public final static String mSelectedSampleName = "com.example.fd.sampler.mSelectedSampleName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_list);
        getSongList();
        Log.d("Content found: ",tempSongList.size()+"");
        String[] names = new String[tempSongList.size()];
        for(int i = 0; i < tempSongList.size();i++){
            names[i] = tempSongList.get(i).getTitle() + "     " + tempSongList.get(i).getSize();
        }
        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("LOG", "itemClick: position = " + position + ", id = "
                        + id);
                String path = tempSongList.get(position).getPath();
                String name = tempSongList.get(position).getTitle();
                Intent intent = new Intent(SampleListActivity.this, MainActivity.class);

                // в ключ username пихаем текст из первого текстового поля
                intent.putExtra(mSelectedSamplePath, path);
                intent.putExtra(mSelectedSampleName, name);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_sample_menu, menu);
        return true;
    }

    public void getSongList() {
        //retrieve song info
        tempSongList = new ArrayList<>();
        ContentResolver musicResolver = getApplicationContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumId = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int data= musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumkey=musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);
            int size = musicCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int type = musicCursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long thisalbumId = musicCursor.getLong(albumId);
                String thisdata= musicCursor.getString(data);
                String AlbumKey = musicCursor.getString(albumkey);
                long fileSize = musicCursor.getLong(size);
                String mime_type = musicCursor.getString(type);

                if(fileSize < 1000000 && mime_type.equals("audio/x-wav")){
                tempSongList.add(new SoundSample(thisId, thisTitle, thisArtist, thisalbumId, thisdata, AlbumKey,fileSize));}

            }
            while (musicCursor.moveToNext());

        }
    }
}
