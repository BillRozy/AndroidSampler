package com.example.fd.sampler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.htmlcleaner.TagNode;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class SiteParserActivity extends Activity {

    public ListView listview;
    public List<String> refs;
    public String zipName;
    public final static String SITE_ADDRESS = "http://xbeat.ucoz.net/index.html";
    private ProgressDialog pDialog;
    private ProgressDialog pd;
    public static final int progress_bar_type = 0;
    private TextView aboutSituationView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_parse);
        final LinearLayout lMain = (LinearLayout) findViewById(R.id.parse_frame);
        final Button reconnectBtn = (Button) findViewById(R.id.reconnectBtn);
        reconnectBtn.setEnabled(true);
        reconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    pd = ProgressDialog.show(SiteParserActivity.this, "Working...", "request to server", true, false);
                    new ParseSite().execute(SITE_ADDRESS);
                    Log.d("reconnect", "clicked");
                    lMain.removeView(aboutSituationView);
                }
            }
        });
        if (isOnline()) {
            pd = ProgressDialog.show(SiteParserActivity.this, "Working...", "request to server", true, false);
            new ParseSite().execute(SITE_ADDRESS);
        } else {
            aboutSituationView = new TextView(this);
            aboutSituationView.setText("It seems, that you haven't internet connection! Fix it please and push refresh.");
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            aboutSituationView.setGravity(Gravity.CENTER_HORIZONTAL);
            aboutSituationView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            aboutSituationView.setTextSize(18F);
            aboutSituationView.setTextColor(Color.WHITE);
            lMain.addView(aboutSituationView, lParams);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SiteParser Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.fd.sampler/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SiteParser Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.fd.sampler/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class ParseSite extends AsyncTask<String, Void, List<String>> {
        protected List<String> doInBackground(String... arg) {
            List<String> output = new ArrayList<>();
            refs = new ArrayList<>();
            try {
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                List<TagNode> links = hh.getLinksByClass("hyperlink");

                for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext(); ) {
                    TagNode divElement = iterator.next();
                    output.add(divElement.getText().toString());
                    refs.add(divElement.getAttributeByName("href"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        protected void onPostExecute(final List<String> output) {
            pd.dismiss();
            listview = (ListView) findViewById(R.id.listViewData);
            listview.setAdapter(new BrowseOnlineKitsAdapter(SiteParserActivity.this,
                    output));

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    zipName = output.get(position);
                    new DownloadFileFromURL().execute(refs.get(position));
                }
            });
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                conection.getContentType();
                int lenghtOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + zipName + ".zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    output.write(data, 0, count);
                }

                output.flush();

                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            File zip = new File(Environment.getExternalStorageDirectory() + "/" + zipName + ".zip");
            File target = new File(FileBrowserActivity.SAMPLES_DIRECTORY);
            try {
                unzip(zip, target);
            } catch (IOException exc) {
                Log.e("Error", "Cant extract" + zipName);
            }
            dismissDialog(progress_bar_type);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "File was Loaded", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}