package lucien.albert.myapplication.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import lucien.albert.myapplication.network.Downloader;
import lucien.albert.myapplication.adapter.MessageAdapter;
import lucien.albert.myapplication.model.Messages;
import lucien.albert.myapplication.network.OnDownloadCompleteListener;
import lucien.albert.myapplication.R;
import lucien.albert.myapplication.model.Result;

public class ChannelMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_message);
        //getIntent().getStringExtra()

    }

}
