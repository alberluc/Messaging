package lucien.albert.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ChannelMessageActivity extends AppCompatActivity implements OnDownloadCompleteListener, View.OnClickListener {

    public static final String PREFS_NAME = "Stockage";
    private static final int PICTURE_REQUEST_CODE = 9;
    private String channelID;
    private String accesstoken;
    private Messages messages;
    private ListView lVMessage;
    private EditText txtMessage;
    private Button btnEnvoyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_message);
        //getIntent().getStringExtra()

        lVMessage = (ListView) findViewById(R.id.lVMessage);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        btnEnvoyer = (Button) findViewById(R.id.btnEnvoyer);
        btnEnvoyer.setOnClickListener((View.OnClickListener) this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        channelID = settings.getString("channelID", "");
        accesstoken = settings.getString("accesstoken", "");

        final HashMap<String, String> Params = new HashMap<>();
        Params.put("accesstoken", accesstoken);
        Params.put("channelid", channelID);

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                getMessage(Params);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

    }

    @Override
    public void onDownloadCompleted(String result, int type) {

        if(type==0){

            Gson gson = new Gson();
            messages = gson.fromJson(result, Messages.class);
            int index = lVMessage.getFirstVisiblePosition();

            View v = lVMessage.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - lVMessage.getPaddingTop());

            lVMessage.setAdapter(new MessageAdapter(getApplicationContext(), messages.messages));

            lVMessage.setSelectionFromTop(index, top);


        }
        else if(type==1){

            Gson gson = new Gson();
            Result r = gson.fromJson(result, Result.class);

            if(r.code==200){

                Toast.makeText(this, "Message envoyé !" ,Toast.LENGTH_SHORT).show();

            }
            else{

                Toast.makeText(this, "Erreur !" ,Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnEnvoyer){

            if(txtMessage.getText().toString() != ""){

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String username = settings.getString("username", "");

                HashMap<String, String> Params = new HashMap<>();
                Params.put("accesstoken", accesstoken);
                Params.put("channelid", channelID);
                Params.put("message", txtMessage.getText().toString());
                Params.put("username", username);

                Downloader d = new Downloader(this, "http://www.raphaelbischof.fr/messaging/?function=sendmessage" ,Params, 1);
                d.setDownloaderList(this);
                d.execute();

            }

        }

        if(view.getId() == R.id.btnImage){

            /*Uri uri = Uri.fromFile();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, PICTURE_REQUEST_CODE);*/

        }

    }

    public void getMessage(HashMap<String, String> Params){
        Downloader d = new Downloader(this, "http://www.raphaelbischof.fr/messaging/?function=getmessages" ,Params);
        d.setDownloaderList(this);
        d.execute();
    }
}
