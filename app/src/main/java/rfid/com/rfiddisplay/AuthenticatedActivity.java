package rfid.com.rfiddisplay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: Save the pois locally as they dissappear when the user taps back to authenticated activity
 */

public class AuthenticatedActivity extends Activity implements OnTaskCompleted, View.OnClickListener {

    HashMap<String, String> serialCache;
    JSONObject jsonObject;
    TextView areaID;
    private JSONParser asyncRequest;
    private static String responseURL = "http://178.62.34.201/phpAppResponse/replyApp.php";
    private Button exploreAreaButton;
    private Button logoutButton;
    private Button navigateButton;
    private Button informationButton;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_authenticated);
        String json = getIntent().getStringExtra("com.parse.Data");
        areaID = (TextView) findViewById(R.id.gateNumberText);
        exploreAreaButton = (Button) findViewById(R.id.explore_area_button);
        exploreAreaButton.setOnClickListener(this);
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);
        navigateButton = (Button)findViewById(R.id.navigate_to_gate_button);
        navigateButton.setOnClickListener(this);
        informationButton = (Button) findViewById(R.id.information_button);
        informationButton.setOnClickListener(this);


        serialCache = new HashMap<String, String>();
        serialCache.put("335391", "1");
        serialCache.put("335392", "2");
        serialCache.put("335393", "3");


        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        try {
            if (json != null) {
                jsonObject = new JSONObject(json);
                jsonObject = jsonObject.getJSONObject("payload");
                areaID.setText(serialCache.get(jsonObject.get("serial_id")));

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("area_id", serialCache.get(jsonObject.get("serial_id")));
                editor.commit();
            } else {
                String area_ID = settings.getString("area_id", null/*default value*/);
                areaID.setText(area_ID);
            }
        } catch (JSONException e) {
            Log.e("JSON ERROR", "Error converting String to JSON Object.");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authenticated, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent switchActivity = new Intent(this, UnderConstructionActivity.class);
        switch (v.getId()) {
            case R.id.logout_button:
                logoutUser();
                break;
            case R.id.explore_area_button:
                setProgressBarIndeterminateVisibility(true);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "explore_more"));
                params.add(new BasicNameValuePair("area_id", areaID.getText().toString()));
                asyncRequest = new JSONParser(this, responseURL);
                asyncRequest.execute(params);
                break;
            case R.id.navigate_to_gate_button:
                this.startActivity(switchActivity);
                break;
            case R.id.information_button:
                this.startActivity(switchActivity);
                break;
        }
    }

    private void logoutUser() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("auth_token", "NOT_LOGGED_IN");
        editor.commit();
        Intent switchActivity = new Intent(this, SplashScreen.class);
        this.startActivity(switchActivity);
    }

    @Override
    public void onTaskCompleted(JSONObject jObj) {
        setProgressBarIndeterminateVisibility(false);

        Intent newActivity = new Intent(this, PoiListActivity.class);

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("json_object", jObj.toString());
        editor.commit();

        this.startActivity(newActivity);

    }
}
