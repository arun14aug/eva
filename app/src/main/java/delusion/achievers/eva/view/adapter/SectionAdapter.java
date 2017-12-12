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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.Sections;
import delusion.achievers.eva.utils.CustomVolleyRequestQueue;
import delusion.achievers.eva.utils.ServiceApi;
import delusion.achievers.eva.utils.Utils;


public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.MyViewHolder> {
    private ArrayList<Sections> data;
    private LayoutInflater inflater;
    private Activity activity;


    public SectionAdapter(Activity context, ArrayList<Sections> data) {
        this.activity = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Sections current = data.get(position);
        holder.arrow.setVisibility(View.GONE);
        holder.txt_title.setText(current.getSectionName());
        if (!Utils.isEmptyString(current.getImagePath())) {
            String url = ServiceApi.baseurl + current.getImagePath();
            Log.e(" URL : ", "" + url);
            ImageLoader mImageLoader;
            mImageLoader = new CustomVolleyRequestQueue(activity)
                    .getImageLoader();
////        if (!imageUrl.equalsIgnoreCase("null"))
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
        TextView txt_title;
        NetworkImageView imageView;
        ImageView arrow;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            imageView = itemView.findViewById(R.id.img_category);
            arrow = itemView.findViewById(R.id.img_arrow);
        }
    }
}
