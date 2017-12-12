package delusion.achievers.eva.model;

/*
 * Created by HP on 21-11-2017.
 */

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Category extends ExpandableGroup<SubCategory> {
    private String SpotCategoryID, Name, SpotId, ImagePath;
//    private List<SubCategory> items;
//
//    @Override
//    public List<SubCategory> getItems() {
//        return items;
//    }
//
//    public void setItems(List<SubCategory> items) {
//        this.items = items;
//    }

    protected Category(Parcel in) {
        super(in);
    }

    public Category(String title, List<SubCategory> items) {
        super(title, items);
    }
//    public Category(String SpotCategoryID, String Name, String SpotId, String ImagePath, List<SubCategory> items) {
//        this.SpotCategoryID = SpotCategoryID;
//        this.Name = Name;
//        this.SpotId = SpotId;
//        this.ImagePath = ImagePath;
//        this.items = items;
//    }
    public String getSpotCategoryID() {
        return SpotCategoryID;
    }

    public void setSpotCategoryID(String spotCategoryID) {
        SpotCategoryID = spotCategoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpotId() {
        return SpotId;
    }

    public void setSpotId(String spotId) {
        SpotId = spotId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
