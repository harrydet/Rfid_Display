package rfid.com.rfiddisplay;

/**
 * Created by Harry on 11/03/2015.
 */
public class PoiItem {
    public String id;
    public String poiName;
    public String poiDescription;
    public int category;
    public String locationURL;

    public PoiItem(String id, String poiName, String poiDescription, int category, String locationURL){
        this.id = id;
        this.poiName = poiName;
        this.poiDescription = poiDescription;
        this.category = category;
        this.locationURL = locationURL;
    }

    public String toString(){
        return this.poiName;
    }
}
