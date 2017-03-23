package lucien.albert.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.util.ArrayList;

import lucien.albert.myapplication.model.Message;
import lucien.albert.myapplication.network.OnDownloadCompleteListener;
import lucien.albert.myapplication.R;

/**
 * Created by alberluc on 27/01/2017.
 */
public class MessageAdapter extends ArrayAdapter<Message> implements OnDownloadCompleteListener {

    private final Context context;
    private final ArrayList<Message> values;
    private View rowView;
    private String url;

    public MessageAdapter(Context context, ArrayList<Message> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.activity_row_message, parent, false);

        TextView txtMsg = (TextView) rowView.findViewById(R.id.txtMessage);
        txtMsg.setText(values.get(position).getMessage());

        TextView txtUser = (TextView) rowView.findViewById(R.id.txtUser);
        txtUser.setText(values.get(position).getUsername() + " : ");

        TextView txtDate = (TextView) rowView.findViewById(R.id.txtDate);
        txtDate.setText(values.get(position).getDate());

        url = values.get(position).getImageUrl();
        final ImageView imgUser = (ImageView) rowView.findViewById(R.id.imgUser);

        //J'ai préféré la facilité ! :)
        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imgUser) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgUser.setImageDrawable(circularBitmapDrawable);
            }
        });

        //Mais j'ai quand même essayé de faire le downloader
        /*DownloaderImage d = new DownloaderImage(this, url , "");
        d.setDownloaderList(this);
        d.execute();*/

        return rowView;

    }

    @Override
    public void onDownloadCompleted(String content, int type) {

        File imgFile = new File(url);
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
            imgUser.setImageBitmap(myBitmap);
        }

    }
}
