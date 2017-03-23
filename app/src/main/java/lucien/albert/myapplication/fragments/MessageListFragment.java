package lucien.albert.myapplication.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import lucien.albert.myapplication.AppConfig;
import lucien.albert.myapplication.R;
import lucien.albert.myapplication.adapter.MessageAdapter;
import lucien.albert.myapplication.model.Messages;
import lucien.albert.myapplication.model.Result;
import lucien.albert.myapplication.network.Downloader;
import lucien.albert.myapplication.network.OnDownloadCompleteListener;

/**
 * Created by alberluc on 22/03/2017.
 */
public class MessageListFragment extends Fragment implements OnDownloadCompleteListener, View.OnClickListener {

    private String channelID;
    private String accesstoken;
    private Messages messages;
    private ListView lVMessage;
    private EditText txtMessage;
    private Button btnEnvoyer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_message_list,container);

        lVMessage = (ListView) v.findViewById(R.id.lVMessage);
        txtMessage = (EditText) v.findViewById(R.id.txtMessage);
        btnEnvoyer = (Button) v.findViewById(R.id.btnEnvoyer);
        btnEnvoyer.setOnClickListener((View.OnClickListener) this);

        SharedPreferences settings = getActivity().getSharedPreferences(AppConfig.PREFS_NAME, 0);
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

        return v;
    }

    public void getMessage(HashMap<String, String> Params){
        Downloader d = new Downloader(getActivity(), "http://www.raphaelbischof.fr/messaging/?function=getmessages" ,Params);
        d.setDownloaderList(this);
        d.execute();
    }

    @Override
    public void onDownloadCompleted(String content, int type) {

        if(type==0){

            Gson gson = new Gson();
            messages = gson.fromJson(content, Messages.class);
            int index = lVMessage.getFirstVisiblePosition();

            View v = lVMessage.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - lVMessage.getPaddingTop());

            lVMessage.setAdapter(new MessageAdapter(getActivity().getApplicationContext(), messages.getMessages()));

            lVMessage.setSelectionFromTop(index, top);


        }
        else if(type==1){

            Gson gson = new Gson();
            Result r = gson.fromJson(content, Result.class);

            if(r.getCode()==200){

                Toast.makeText(getContext(), "Message envoyé !" ,Toast.LENGTH_SHORT).show();

            }
            else{

                Toast.makeText(getContext(), "Erreur !" ,Toast.LENGTH_SHORT).show();

            }

        }

    }



    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnEnvoyer){

            if(txtMessage.getText().toString() != ""){

                SharedPreferences settings = getActivity().getSharedPreferences(AppConfig.PREFS_NAME, 0);
                String username = settings.getString("username", "");

                HashMap<String, String> Params = new HashMap<>();
                Params.put("accesstoken", accesstoken);
                Params.put("channelid", channelID);
                Params.put("message", txtMessage.getText().toString());
                Params.put("username", username);

                Downloader d = new Downloader(getActivity(), "http://www.raphaelbischof.fr/messaging/?function=sendmessage" ,Params, 1);
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

    public void changeChannelId(String id)
    {
        channelID = id;
    }

}
