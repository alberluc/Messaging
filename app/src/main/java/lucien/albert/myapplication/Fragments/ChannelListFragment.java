package lucien.albert.myapplication.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import lucien.albert.myapplication.activity.ChannelListActivity;
import lucien.albert.myapplication.adapter.ChannelsAdapter;
import lucien.albert.myapplication.network.Downloader;
import lucien.albert.myapplication.model.Channels;
import lucien.albert.myapplication.network.OnDownloadCompleteListener;
import lucien.albert.myapplication.R;

/**
 * Created by alberluc on 22/03/2017.
 */
public class ChannelListFragment extends Fragment implements  OnDownloadCompleteListener {

    private ListView lVChannels;
    private String accesstoken;
    private Channels chs;

    public static final String PREFS_NAME = "Stockage";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_channel_list,container);

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        accesstoken = settings.getString("accesstoken", "");

        HashMap<String, String> Params = new HashMap<String, String>();
        Params.put("accesstoken", accesstoken);

        Downloader d = new Downloader(getActivity(), "http://www.raphaelbischof.fr/messaging/?function=getchannels" , Params);
        d.setDownloaderList(this);
        d.execute();

        lVChannels = (ListView) v.findViewById(R.id.lVChannels);
        return v;
    }

    public Channels getChs() {
        return chs;
    }

    @Override
    public void onDownloadCompleted(String content, int type) {


        Gson gson = new Gson();
        chs = gson.fromJson(content, Channels.class);


        lVChannels.setAdapter(new ChannelsAdapter(getActivity(), chs.getChannels()));
        lVChannels.setOnItemClickListener((ChannelListActivity)getActivity());

    }
}
