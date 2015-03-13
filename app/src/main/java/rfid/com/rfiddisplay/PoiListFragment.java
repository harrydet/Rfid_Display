package rfid.com.rfiddisplay;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ListFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rfid.com.rfiddisplay.dummy.DummyContent;


public class PoiListFragment extends ListFragment {


    private static final String STATE_ACTIVATED_POSITION = "activated_position";


    private Callbacks mCallbacks = sDummyCallbacks;


    private int mActivatedPosition = ListView.INVALID_POSITION;


    public interface Callbacks {

        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };
    private List<PoiItem> ITEMS = new ArrayList<PoiItem>();;


    public PoiListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        String jsonString = settings.getString("json_object", null/*default value*/);
        try {
            JSONObject jObj = new JSONObject(jsonString);
            int length = jObj.length();
            for(int i = 0;i < length; i++){
                String index = String.valueOf(i);
                JSONObject innerObj = jObj.getJSONObject(index);
                String poiName = innerObj.getString("poiName");
                String poiDescription = innerObj.getString("poiDescription");
                int poiCategory = innerObj.getInt("poiCategory");
                ITEMS.add(new PoiItem(Integer.toString(i), poiName, poiDescription, poiCategory));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* setListAdapter(new ArrayAdapter<PoiItem>(
                getActivity(),
                R.layout.custom_list_view_layout,
                R.id.list_item_title,
                ITEMS));*/
        setListAdapter(new CustomAdapter(getActivity(), ITEMS));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
