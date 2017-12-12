package delusion.achievers.eva.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.SearchContent;
import delusion.achievers.eva.utils.CustomVolleyRequestQueue;
import delusion.achievers.eva.utils.ServiceApi;
import delusion.achievers.eva.utils.Utils;

/*
 * Created by hp on 16/11/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>
        implements Filterable {
    private Activity context;
    private List<SearchContent> contactList;
    private List<SearchContent> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView arrow;
        NetworkImageView imageView;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            arrow = view.findViewById(R.id.img_arrow);
            imageView = view.findViewById(R.id.img_category);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public SearchAdapter(Activity context, List<SearchContent> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SearchContent contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.arrow.setVisibility(View.GONE);

        if (!Utils.isEmptyString(contact.getImagePath())) {
            String url = ServiceApi.baseurl + contact.getImagePath();
            Log.e(" URL : ", "" + url);
            ImageLoader mImageLoader;
            mImageLoader = new CustomVolleyRequestQueue(context)
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
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<SearchContent> filteredList = new ArrayList<>();
                    for (SearchContent row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<SearchContent>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SearchContent contact);
    }
}
