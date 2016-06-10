package com.example.fd.sampler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
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
    /** Called when the activity is first created. */
    public ListView listview;
    public List<String> refs;
    public String zipName;
    public final static String SITE_ADDRESS = "http://xbeat.ucoz.net/index.html";
        // Progress Dialog
        private ProgressDialog pDialog;
        //Диалог ожидания
          private ProgressDialog pd;
        // Progress dialog type (0 - for Horizontal progress bar)
        public static final int progress_bar_type = 0;

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_parse);
            pd = ProgressDialog.show(SiteParserActivity.this, "Working...", "request to server", true, false);
            //Запускаем парсинг
            new ParseSite().execute(SITE_ADDRESS);

    }


    private class ParseSite extends AsyncTask<String, Void, List<String>> {
        //Фоновая операция
        protected List<String> doInBackground(String... arg) {
            List<String> output = new ArrayList<>();
            refs = new ArrayList<>();
            try
            {
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                List<TagNode> links = hh.getLinksByClass("hyperlink");

                for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();)
                {
                    TagNode divElement = iterator.next();
                    output.add(divElement.getText().toString());
                    refs.add(divElement.getAttributeByName("href"));
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return output;
        }

        //Событие по окончанию парсинга
        protected void onPostExecute(final List<String> output) {
            //Убираем диалог загрузки
            pd.dismiss();
            //Находим ListView
            listview = (ListView) findViewById(R.id.listViewData);
            //Загружаем в него результат работы doInBackground
            listview.setAdapter(new ArrayAdapter<>(SiteParserActivity.this,
                    android.R.layout.simple_list_item_1, output));

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
                // getting file length
                conection.getContentType();
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() +"/" + zipName + ".zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
           File zip = new File(Environment.getExternalStorageDirectory() +"/" + zipName + ".zip");
           File target = new File(FileBrowserActivity.FILES_DIRECTORY + "/RockKits");
            try {
                unzip(zip, target);
            }catch (IOException exc){Log.e("Error", "Cant extract" + zipName);}
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
}