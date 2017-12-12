package delusion.achievers.eva.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import delusion.achievers.eva.R;
import delusion.achievers.eva.model.Category;
import delusion.achievers.eva.model.SubCategory;

public class BeaconExpandableAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder, SubCategoryViewHolder> {

    private Activity activity;

    public BeaconExpandableAdapter(Activity activity, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.activity = activity;
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.row_category, parent, false);

        return new CategoryViewHolder(view, activity);
    }

    @Override
    public SubCategoryViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.row_sub_category, parent, false);

        return new SubCategoryViewHolder(view, activity);
    }

    @Override
    public void onBindChildViewHolder(SubCategoryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final SubCategory phone = ((Category) group).getItems().get(childIndex);
        holder.onBind(phone);
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        Category category = ((Category) group);
        holder.setGroupName(group, category);
    }
}
