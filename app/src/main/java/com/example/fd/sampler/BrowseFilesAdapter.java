package com.example.fd.sampler;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


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
        // Изменение иконки для Windows и iPhone
        String s = values[position];
        if (s.contains(".")) {
            imageView.setImageResource(R.drawable.file_icon);
            prePlayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
                    int id = pool.load(refs[position],1);
                    pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            soundPool.play(sampleId,1,1,1,0,1);
                        }
                    });

                }
            });
        } else {
            imageView.setImageResource(R.drawable.folder_icon);
            prePlayBtn.setVisibility(View.INVISIBLE);
            prePlayBtn.setEnabled(false);
        }

        return rowView;
    }
}

