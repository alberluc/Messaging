package lucien.albert.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import lucien.albert.myapplication.AppConfig;
import lucien.albert.myapplication.fragments.ChannelListFragment;
import lucien.albert.myapplication.model.Channel;
import lucien.albert.myapplication.R;

public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChannelListFragment frag_channel = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_channel);
        Channel myChannel = frag_channel.getChs().getChannels().get(position);

        SharedPreferences settings = getSharedPreferences(AppConfig.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("channelID", String.valueOf(myChannel.getChannelID()));
        editor.commit();

        Intent newActivity = new Intent(getApplicationContext(),ChannelMessageActivity.class);
        //newActivity.pu
        startActivity(newActivity);

    }
}
