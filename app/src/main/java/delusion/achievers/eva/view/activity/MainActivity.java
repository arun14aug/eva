package delusion.achievers.eva.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import delusion.achievers.eva.R;
import delusion.achievers.eva.model.BeaconValues;
import delusion.achievers.eva.model.ModelManager;
import delusion.achievers.eva.model.Sections;
import delusion.achievers.eva.utils.EvaLog;
import delusion.achievers.eva.utils.ServiceApi;
import delusion.achievers.eva.utils.Utils;
import delusion.achievers.eva.view.adapter.BeaconsAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ArrayList<BeaconValues> beaconValuesList;
    private ArrayList<BeaconValues> beaconNameList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private Activity activity;

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private boolean is_need = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        TextView txt_header = findViewById(R.id.txt_header);
        txt_header.setText(getString(R.string.app_name));
        ImageView imageView = findViewById(R.id.ic_back);
        imageView.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.beacon_list);
        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {

            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                swipeContainer.setRefreshing(false);
                if (!is_need) {
                    if (!beacons.isEmpty()) {
                        beaconValuesList = new ArrayList<>();
                        for (int i = 0; i < beacons.size(); i++) {
                            Beacon nearestBeacon = beacons.get(i);
                            BeaconValues beaconValues = new BeaconValues();
                            beaconValues.setMajor(nearestBeacon.getMajor());
                            beaconValues.setMinor(nearestBeacon.getMinor());
                            beaconValues.setUniqueKey(nearestBeacon.getUniqueKey());
                            beaconValues.setUUID(nearestBeacon.getProximityUUID().toString());
//                        List<String> places = placesNearBeacon(nearestBeacon);
                            beaconValuesList.add(beaconValues);
                            // TODO: update the UI here
                        }

                        if (beaconValuesList.size() > 0) {
                            Utils.showLoading(activity);
                            StringBuilder URL = new StringBuilder(ServiceApi.GET_NAMES);
                            for (int i = 0; i < beaconValuesList.size(); i++) {
                                URL.append("uids=").append(beaconValuesList.get(i).getUUID()).append("&");
                            }
                            String url = URL.toString().substring(0, URL.toString().length() - 1);
                            ModelManager.getInstance().getBeaconsManager().getNames(activity, true, url);
                        }
                        is_need = true;
                    }
                }

            }
        });

        // temporary basis beacon
        Utils.showLoading(activity);
        String url = ServiceApi.GET_NAMES + "uids=" + ServiceApi.UUID;
        ModelManager.getInstance().getBeaconsManager().getNames(activity, true, url);

        region = new BeaconRegion("ranged region", null, Integer.parseInt(ServiceApi.MAJOR), Integer.parseInt(ServiceApi.MINOR));
        recyclerView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(activity, recyclerView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArrayList<Sections> sections = beaconNameList.get(position).getSectionsArrayList();
                if (sections == null)
                    sections = new ArrayList<>();
                Intent intent;
                if (sections.size() == 0) {
                    intent = new Intent(activity, CategoryActivity.class);
                } else {
                    intent = new Intent(activity, SectionsActivity.class);
                }
                intent.putExtra("spot_id", beaconNameList.get(position).getSpotId());
                intent.putExtra("name", beaconNameList.get(position).getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                is_need = false;
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setAdapter() {
        BeaconsAdapter beaconsAdapter = new BeaconsAdapter(MainActivity.this, beaconNameList);
        recyclerView.setAdapter(beaconsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        beaconsAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
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
        if (message.equalsIgnoreCase("GetNames True")) {
            Utils.dismissLoading();
            beaconNameList = ModelManager.getInstance().getBeaconsManager().getNames(activity, false, "");
            if (beaconNameList != null)
                if (beaconNameList.size() > 0)
                    setAdapter();
                else
                    Utils.showMessage(activity, "No record found");
            else
                Utils.showMessage(activity, "No record found");
            EvaLog.e(TAG, "GetNames True");
        } else if (message.equalsIgnoreCase("GetNames False")) {
            Utils.showMessage(activity, getString(R.string.error_message));
            EvaLog.e(TAG, "GetNames False");
            Utils.dismissLoading();
        }

    }
}