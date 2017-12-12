package delusion.achievers.eva.model;

/*
 * Created by HP on 21-11-2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class SubCategory implements Parcelable {
    private String SpotCategoryID, Name, SpotId, ImagePath, categoryID;

    SubCategory(String SpotCategoryID, String Name, String SpotId, String ImagePath, String categoryID) {
        this.SpotCategoryID = SpotCategoryID;
        this.Name = Name;
        this.SpotId = SpotId;
        this.ImagePath = ImagePath;
        this.categoryID = categoryID;
    }

    private SubCategory(Parcel in) {

    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
