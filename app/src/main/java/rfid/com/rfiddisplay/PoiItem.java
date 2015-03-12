package rfid.com.rfiddisplay;

/**
 * Created by Harry on 11/03/2015.
 */
public class PoiItem {
    public String id;
    public String poiName;
    public String poiDescription;

    public PoiItem(String id, String poiName, String poiDescription){
        this.id = id;
        this.poiName = poiName;
        this.poiDescription = poiDescription;
    }

    public String toString(){
        return this.poiName;
    }
}
