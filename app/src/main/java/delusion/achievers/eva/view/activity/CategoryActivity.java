package delusion.achievers.eva.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import delusion.achievers.eva.R;
import delusion.achievers.eva.model.Category;
import delusion.achievers.eva.model.ModelManager;
import delusion.achievers.eva.utils.EvaLog;
import delusion.achievers.eva.utils.ServiceApi;
import delusion.achievers.eva.utils.Utils;
import delusion.achievers.eva.view.adapter.BeaconExpandableAdapter;

/*
 * Created by HP on 21-11-2017.
 */

public class CategoryActivity extends Activity {
    private static final String TAG = CategoryActivity.class.getName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private Activity activity;
    private ArrayList<Category> categoryArrayList;
    private String id = "", section_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        activity = CategoryActivity.this;

        id = getIntent().getStringExtra("spot_id");
        section_id = getIntent().getStringExtra("section_id");
        String name = getIntent().getStringExtra("name");

        TextView txt_header = findViewById(R.id.txt_header);
        txt_header.setText(name);
        ImageView imageView = findViewById(R.id.ic_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView searchView = findViewById(R.id.ic_search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView = findViewById(R.id.category_list);
        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getData();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getData();
    }

    private void getData() {
        Utils.showLoading(activity);
        String url;
        if (!Utils.isEmptyString(section_id))
            url = ServiceApi.GET_CATEGORIES + "Spotid=" + id + "&sectionid=" + section_id;
        else
            url = ServiceApi.GET_CATEGORIES + "Spotid=" + id;
        ModelManager.getInstance().getBeaconsManager().getCategories(activity, true, url);
    }

    private void setAdapter() {
        BeaconExpandableAdapter categoryAdapter = new BeaconExpandableAdapter(activity, categoryArrayList);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        categoryAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("GetCategory True")) {
            Utils.dismissLoading();
            categoryArrayList = ModelManager.getInstance().getBeaconsManager().getCategories(activity, false, "");
            if (categoryArrayList != null)
                if (categoryArrayList.size() > 0)
                    setAdapter();
                else
                    Utils.showMessage(activity, "No record found");
            else
                Utils.showMessage(activity, "No record found");
            EvaLog.e(TAG, "GetCategory True");
        } else if (message.equalsIgnoreCase("GetCategory False")) {
            Utils.showMessage(activity, getString(R.string.error_message));
            EvaLog.e(TAG, "GetCategory False");
            Utils.dismissLoading();
        }

    }
}
