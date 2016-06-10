package com.example.fd.sampler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BrowseFilesAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public BrowseFilesAdapter(Context context, String[] values) {
        super(context, R.layout.list_item_file, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_file, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.file_description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_file);
        textView.setText(values[position]);
        // Изменение иконки для Windows и iPhone
        String s = values[position];
        if (s.contains(".")) {
            imageView.setImageResource(R.drawable.file_icon);
        } else {
            imageView.setImageResource(R.drawable.folder_icon);
        }

        return rowView;
    }
}

