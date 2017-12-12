package delusion.achievers.eva.view.adapter;

/*
 * Created by arun.sharma on 29/07/15.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.Spots;
import delusion.achievers.eva.utils.Utils;


public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.MyViewHolder> {
    private ArrayList<Spots> data;
    private LayoutInflater inflater;
    private Activity activity;


    public SpotsAdapter(Activity context, ArrayList<Spots> data) {
        this.activity = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

//    public void delete(int position) {
//        data.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_spots, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Spots current = data.get(position);
        holder.txt_title.setText(current.getShopName());
        holder.txt_address.setText(current.getAddressLine1());
        if (!Utils.isEmptyString(current.getEmail()))
            holder.txt_email.setText(current.getEmail());
        else if (!Utils.isEmptyString(current.getEmail1()))
            holder.txt_email.setText(current.getEmail1());
        else {
            holder.txt_email.setVisibility(View.GONE);
            holder.ic_email.setVisibility(View.GONE);
        }

        if (!Utils.isEmptyString(current.getMobileNo1()))
            holder.txt_mobile.setText(current.getMobileNo1());
        else if (!Utils.isEmptyString(current.getMobileNo2()))
            holder.txt_mobile.setText(current.getMobileNo2());
        else if (!Utils.isEmptyString(current.getMobileNo3()))
            holder.txt_mobile.setText(current.getMobileNo3());
        else {
            holder.txt_mobile.setVisibility(View.GONE);
            holder.ic_call.setVisibility(View.GONE);
        }
//        if (!Utils.isEmptyString(current.getImagePath())) {
//            String url = ServiceApi.baseurl + current.getImagePath();
//            Log.e(" URL : ", "" + url);
//            ImageLoader mImageLoader;
//            mImageLoader = new CustomVolleyRequestQueue(activity)
//                    .getImageLoader();
//////        if (!imageUrl.equalsIgnoreCase("null"))
//            mImageLoader.get(url, ImageLoader.getImageListener(holder.imageView,
//                /*R.drawable.logo*/ 0, /*R.drawable.logo*/ 0));
//            holder.imageView.setImageUrl(url, mImageLoader);
//            holder.imageView.setTag(url);
//
//        } else
        holder.imageView.setImageResource(R.mipmap.ic_launcher_round);

        holder.ic_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + current.getMobileNo1()));
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                activity.startActivity(callIntent);
            }
        });

        holder.ic_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(new String[]{current.getEmail()});
            }
        });

    }

    private void sendEmail(String[] recipientList) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipientList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Query");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I am body");
        activity.startActivity(Intent.createChooser(emailIntent, "Sending Email"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_address, txt_email, txt_mobile;
        NetworkImageView imageView;
        ImageView ic_favorite, ic_email, ic_call;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_email = itemView.findViewById(R.id.txt_email);
            txt_mobile = itemView.findViewById(R.id.txt_mobile);
            imageView = itemView.findViewById(R.id.img_category);
            ic_favorite = itemView.findViewById(R.id.ic_favorite);
            ic_email = itemView.findViewById(R.id.ic_email);
            ic_call = itemView.findViewById(R.id.ic_call);
        }
    }
}
