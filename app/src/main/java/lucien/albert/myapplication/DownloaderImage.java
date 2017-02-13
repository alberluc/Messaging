package lucien.albert.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by alberluc on 30/01/2017.
 */
public class DownloaderImage extends AsyncTask<String, String, String>{


    MessageAdapter myContext;
    ArrayList<OnDownloadCompleteListener> listeners = new ArrayList<>();
    String url;
    String name;
    int type;

    public DownloaderImage(MessageAdapter myContext, String url, String name) {
        this.myContext = myContext;
        this.url = url;
        this.name = name;
    }

    public DownloaderImage(){}

    public void setDownloaderList(OnDownloadCompleteListener listener) {
        this.listeners.add(listener);
    }

    @Override
    protected String doInBackground(String... input) {
        String retour = "";
        downloadFromUrl(url, name);
        return retour;
    }

    public void downloadFromUrl(String fileURL, String fileName) {
        try {
            URL url = new URL(fileURL);
            File file = new File(fileName);
            file.createNewFile();
            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();
            /* Define InputStreams to read from the URLConnection.*/
            InputStream is = ucon.getInputStream();
            /* Read bytes to the Buffer until there is nothing more to
            read(-1) and write on the fly in the file.*/
            FileOutputStream fos = new FileOutputStream(file);
            final int BUFFER_SIZE = 23 * 1024;
            BufferedInputStream bis = new BufferedInputStream(is,
                    BUFFER_SIZE);
            byte[] baf = new byte[BUFFER_SIZE];
            int actual = 0;
            while (actual != -1) {
                fos.write(baf, 0, actual);
                actual = bis.read(baf, 0, BUFFER_SIZE);
            }
            fos.close();
        } catch (IOException e) {
            //TODO HANDLER
        }
    }

    @Override
    protected void onPostExecute(String result) {

        for (OnDownloadCompleteListener oneListener : listeners)
        {
            oneListener.onDownloadCompleted(result, type);
        }

    }

}
