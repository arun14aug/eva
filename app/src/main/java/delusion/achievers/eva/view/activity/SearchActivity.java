package delusion.achievers.eva.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.ArrayList;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.ModelManager;
import delusion.achievers.eva.model.SearchContent;
import delusion.achievers.eva.utils.Utils;
import delusion.achievers.eva.view.adapter.SearchAdapter;

/*
 * Created by HP on 09-12-2017.
 */

public class SearchActivity extends Activity implements SearchAdapter.ContactsAdapterListener {

    private Activity activity;
    private SearchView searchView;
    private ArrayList<SearchContent> searchContents;
    private SearchAdapter mAdapter;
    private String section_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activity = SearchActivity.this;
        section_id = getIntent().getStringExtra("section_id");

        searchContents = ModelManager.getInstance().getBeaconsManager().getSearchContentArrayList();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.edt_search);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.requestFocus();

        ImageView ic_close = findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.search_list);

        mAdapter = new SearchAdapter(this, searchContents, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if (!Utils.isEmptyString(query)) {
                    mAdapter.getFilter().filter(query);
                    recyclerView.setAdapter(mAdapter);
                } else
                    recyclerView.setAdapter(null);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (!Utils.isEmptyString(query)) {
                    mAdapter.getFilter().filter(query);
                    recyclerView.setAdapter(mAdapter);
                } else
                    recyclerView.setAdapter(null);
                return false;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(activity, SpotsActivity.class);
                intent.putExtra("category_id", searchContents.get(position).getCategoryID());
                if (!Utils.isEmptyString(section_id))
                    intent.putExtra("section_id", section_id);
                if (!Utils.isEmptyString(searchContents.get(position).getSubCategoryID()))
                    intent.putExtra("subCategory_id", searchContents.get(position).getSubCategoryID());
                intent.putExtra("spot_id", searchContents.get(position).getSpotId());
                intent.putExtra("name", searchContents.get(position).getName());
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onContactSelected(SearchContent contact) {

    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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
