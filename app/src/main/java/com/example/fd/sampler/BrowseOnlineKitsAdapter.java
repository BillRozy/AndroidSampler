package com.example.fd.sampler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BrowseOnlineKitsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    public BrowseOnlineKitsAdapter(Context context, List<String> values) {
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
        Button prePlayBtn = (Button) rowView.findViewById(R.id.prePlayBtn);
        prePlayBtn.setVisibility(View.INVISIBLE);
        prePlayBtn.setEnabled(false);
        textView.setText(values.get(position));
        String s = values.get(position);
        ArrayList<String> list = getListOfCurrentKits();
        if (list.contains(s)) {
            imageView.setBackgroundResource(R.drawable.galka);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }

    private ArrayList<String> getListOfCurrentKits(){
        File path = new File(FileBrowserActivity.SAMPLES_DIRECTORY);
        String[] array = path.list();
        List<String> list = Arrays.asList(array);
        ArrayList<String> alist = new ArrayList(list);
        return alist;
    }
}

