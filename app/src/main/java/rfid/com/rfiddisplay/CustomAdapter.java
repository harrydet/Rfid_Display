package rfid.com.rfiddisplay;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on 13/03/2015.
 */
public class CustomAdapter  extends BaseAdapter{

    List<PoiItem> items;
    Activity activity;
    LayoutInflater mInflater;

    public CustomAdapter(Activity activity, List<PoiItem> items){
        this.items = items;
        this.activity = activity;
        this.mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View V = convertView;
        if(V == null) {
            V = mInflater.inflate(R.layout.custom_list_view_layout, parent, false);
        }

        TextView title =  (TextView)V.findViewById(R.id.list_item_title);
        ImageView icon = (ImageView)V.findViewById(R.id.list_item_icon);

        title.setText(items.get(position).toString());
        int category = 0;
        switch(items.get(position).category){
            case 1:
                category = R.drawable.ic_restaurant;
                break;
            case 2:
                category = R.drawable.ic_bank;
                break;
            case 3:
                category = R.drawable.ic_shopping;
                break;
            case 4:
                category = R.drawable.ic_gate;
                break;
            case 5:
                category = R.drawable.ic_toilet;
                break;
            default:
                category = 0;
                break;
        }
        icon.setImageResource(category);

        return V;
    }
}
