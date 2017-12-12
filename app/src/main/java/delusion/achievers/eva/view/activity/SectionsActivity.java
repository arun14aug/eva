package delusion.achievers.eva.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.BeaconValues;
import delusion.achievers.eva.model.ModelManager;
import delusion.achievers.eva.model.Sections;
import delusion.achievers.eva.view.adapter.SectionAdapter;

/*
 * Created by HP on 09-12-2017.
 */

public class SectionsActivity extends Activity {

    private Activity activity;
    private String id = "";
    private RecyclerView recyclerView;
    private ArrayList<Sections> sectionsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = SectionsActivity.this;

        id = getIntent().getStringExtra("spot_id");
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

        recyclerView = findViewById(R.id.section_list);
        // Lookup the swipe container view
        recyclerView.addOnItemTouchListener(new SectionsActivity.RecyclerTouchListener(activity, recyclerView, new SectionsActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(activity, CategoryActivity.class);
                intent.putExtra("spot_id", id);
                intent.putExtra("section_id", sectionsArrayList.get(position).getSectionID());
                intent.putExtra("name", sectionsArrayList.get(position).getSectionName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getData();
    }

    private void getData() {
        ArrayList<BeaconValues> beaconValuesArrayList = ModelManager.getInstance().getBeaconsManager().getNames(activity, true, "");
        for (int i = 0; i < beaconValuesArrayList.size(); i++) {
            if (beaconValuesArrayList.get(i).getSpotId().equalsIgnoreCase(id)) {
                sectionsArrayList = beaconValuesArrayList.get(i).getSectionsArrayList();
                setAdapter();
                break;
            }
        }
    }

    private void setAdapter() {
        SectionAdapter sectionAdapter = new SectionAdapter(activity, sectionsArrayList);
        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        sectionAdapter.notifyDataSetChanged();
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SectionsActivity.ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SectionsActivity.ClickListener clickListener) {
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

}
