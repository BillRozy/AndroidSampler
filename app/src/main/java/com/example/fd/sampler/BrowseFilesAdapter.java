package com.example.fd.sampler;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class BrowseFilesAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private String[] refs;
    private SoundPool pool;

    public BrowseFilesAdapter(Context context, String[] values, String[] refs) {
        super(context, R.layout.list_item_file, values);
        this.context = context;
        this.values = values;
        this.refs = refs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_file, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.file_description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_file);
        Button prePlayBtn = (Button) rowView.findViewById(R.id.prePlayBtn);
        textView.setText(values[position]);
        String s = values[position];
        if (s.contains(".")) {
            imageView.setImageResource(R.drawable.file_icon);
            prePlayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
                    pool.load(refs[position], 1);
                    pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            soundPool.play(sampleId, 1, 1, 1, 0, 1);
                        }
                    });

                }
            });
            if (!(s.contains(".wav") || s.contains(".mp3") || s.contains(".ogg"))) {
                prePlayBtn.setVisibility(View.INVISIBLE);
                prePlayBtn.setEnabled(false);
            }
        } else {
            imageView.setImageResource(R.drawable.folder_icon);
            prePlayBtn.setVisibility(View.INVISIBLE);
            prePlayBtn.setEnabled(false);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileBrowserActivity act = (FileBrowserActivity) context;
                act.lastSelected.add(act.selected);
                if (act.files[position].isFile()) {
                    Log.d("CLICKED", act.files[position].getName());
                    act.pathToChosenFile = act.files[position].getAbsolutePath();
                    String name = act.files[position].getName();
                    Intent intent = new Intent(act, MainActivity.class);

                    // в ключ username пихаем текст из первого текстового поля
                    intent.putExtra(act.mSelectedSamplePath, act.pathToChosenFile);
                    intent.putExtra(act.mSelectedSampleName, name);
                    act.setResult(act.RESULT_OK, intent);
                    act.finish();
                } else {

                    act.selected = new File(act.files[position].getAbsolutePath());
                    act.pathTextView.setText(act.selected.getAbsolutePath());
                    act.files = new File[act.selected.listFiles().length];
                    act.files = act.selected.listFiles();
                    String[] titles = new String[act.files.length];
                    String[] refsArray = new String[act.files.length];
                    for (int i = 0; i < act.files.length; i++) {
                        titles[i] = act.files[i].getName();
                        refsArray[i] = act.files[i].getAbsolutePath();
                    }
                    BrowseFilesAdapter secAdapter = new BrowseFilesAdapter(act, titles, refsArray);
                    act.fileListView.setAdapter(secAdapter);
                    Log.d("ENDED LISTENER", " pos");
                }
            }
        });
        return rowView;
    }
}

