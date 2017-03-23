package lucien.albert.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import lucien.albert.myapplication.AppConfig;
import lucien.albert.myapplication.fragments.ChannelListFragment;
import lucien.albert.myapplication.fragments.MessageListFragment;
import lucien.albert.myapplication.model.Channel;
import lucien.albert.myapplication.R;

import static lucien.albert.myapplication.R.id.lVChannels;

public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_channel_list);
        } else {
            setContentView(R.layout.activity_channel_list_landscape);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ChannelListFragment frag_channel = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_channel);
        MessageListFragment frag_message = (MessageListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_message);

        Channel myChannel = (Channel) frag_channel.lVChannels.getItemAtPosition(position);

        SharedPreferences settings = getSharedPreferences(AppConfig.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("channelID", String.valueOf(myChannel.getChannelID()));
        editor.commit();

        if(frag_message == null|| !frag_message.isInLayout()){
            Intent intent = new Intent(ChannelListActivity.this, ChannelMessageActivity.class);
            startActivity(intent);
        } else
        {
            frag_message.changeChannelId(myChannel.getChannelID());
        }

    }
}
