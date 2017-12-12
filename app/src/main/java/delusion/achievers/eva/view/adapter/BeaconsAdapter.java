package delusion.achievers.eva.view.adapter;

/*
 * Created by arun.sharma on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.BeaconValues;
import delusion.achievers.eva.utils.CustomVolleyRequestQueue;
import delusion.achievers.eva.utils.ServiceApi;
import delusion.achievers.eva.utils.Utils;


public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.MyViewHolder> {
    private ArrayList<BeaconValues> data;
    private LayoutInflater inflater;
    private Activity context;


    public BeaconsAdapter(Activity context, ArrayList<BeaconValues> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

//    public void delete(int position) {
//        data.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_beacons, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BeaconValues current = data.get(position);
        holder.txt_name.setText(current.getName());
        if (!Utils.isEmptyString(current.getImagePath())) {
            String url = ServiceApi.baseurl + current.getImagePath();
            Log.e(" URL : ", "" + url);
            ImageLoader mImageLoader;
            mImageLoader = new CustomVolleyRequestQueue(context)
                    .getImageLoader();
            mImageLoader.get(url, ImageLoader.getImageListener(holder.imageView,
                /*R.drawable.logo*/ 0, /*R.drawable.logo*/ 0));
            holder.imageView.setImageUrl(url, mImageLoader);
            holder.imageView.setTag(url);

        } else
            holder.imageView.setImageResource(R.mipmap.ic_launcher_round);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        NetworkImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.img_beacon);
        }
    }
}
