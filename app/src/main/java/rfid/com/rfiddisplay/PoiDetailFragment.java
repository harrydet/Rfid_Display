package rfid.com.rfiddisplay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rfid.com.rfiddisplay.dummy.DummyContent;

/**
 * A fragment representing a single Poi detail screen.
 * This fragment is either contained in a {@link PoiListActivity}
 * in two-pane mode (on tablets) or a {@link PoiDetailActivity}
 * on handsets.
 */
public class PoiDetailFragment extends Fragment implements ObservableScrollViewCallbacks {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private  Map<String, PoiItem> ITEM_MAP = new HashMap<String, PoiItem>();

    private View mImageView;
    private View mOverlayView;

    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private PoiItem mItem;
    private float mFlexibleSpaceImageHeight = 200;
    private TextView mTitleView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PoiDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
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
                    ITEM_MAP.put(Integer.toString(i), new PoiItem(Integer.toString(i), poiName, poiDescription, poiCategory));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mItem = ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poi_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            final ObservableScrollView scrollView = (ObservableScrollView) rootView.findViewById(R.id.obsScrollView);
            scrollView.setScrollViewCallbacks(this);
            mImageView = rootView.findViewById(R.id.image);
            mOverlayView = rootView.findViewById(R.id.overlay);
            mTitleView = (TextView) rootView.findViewById(R.id.title);
            mTitleView.setText("Test Poi");

            ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, ((int) mFlexibleSpaceImageHeight));

                }
            });

        }

        return rootView;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float flexibleRange = mFlexibleSpaceImageHeight;
        int minOverlayTransitionY = -mOverlayView.getHeight();

        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));


        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;

        ViewHelper.setTranslationY(mTitleView, titleTranslationY);




    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
