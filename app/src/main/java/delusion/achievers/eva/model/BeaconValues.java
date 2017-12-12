package delusion.achievers.eva.model;

/*
 * Created by HP on 16-11-2017.
 */

import java.util.ArrayList;

public class BeaconValues {
    private String UID, SpotId, Name, ImagePath, Type;
    private ArrayList<Sections> sectionsArrayList;

    private String UniqueKey, UUID;
    private Integer Major, Minor;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getSpotId() {
        return SpotId;
    }

    public void setSpotId(String spotId) {
        SpotId = spotId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public ArrayList<Sections> getSectionsArrayList() {
        return sectionsArrayList;
    }

    public void setSectionsArrayList(ArrayList<Sections> sectionsArrayList) {
        this.sectionsArrayList = sectionsArrayList;
    }

    public String getUniqueKey() {
        return UniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        UniqueKey = uniqueKey;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Integer getMajor() {
        return Major;
    }

    public void setMajor(Integer major) {
        Major = major;
    }

    public Integer getMinor() {
        return Minor;
    }

    public void setMinor(Integer minor) {
        Minor = minor;
    }
}
