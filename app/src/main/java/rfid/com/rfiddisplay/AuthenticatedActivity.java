package rfid.com.rfiddisplay;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AuthenticatedActivity extends Activity implements OnTaskCompleted, View.OnClickListener{

    HashMap<String, String> serialCache;
    JSONObject jsonObject;
    TextView gateNumber;
    private JSONParser asyncRequest;
    private static String responseURL = "http://178.62.34.201/phpAppResponse/replyApp.php";
    private Button exploreAreaButton;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_authenticated);
        String json = getIntent().getStringExtra("com.parse.Data");
        gateNumber = (TextView)findViewById(R.id.gateNumberText);
        exploreAreaButton = (Button) findViewById(R.id.explore_area_button);
        exploreAreaButton.setOnClickListener(this);

        serialCache = new HashMap<String, String>();
        serialCache.put("335391", "1");
        serialCache.put("335392", "2");
        serialCache.put("335393", "3");




        try {
            if(json != null){
                jsonObject = new JSONObject(json);
                jsonObject = jsonObject.getJSONObject("payload");
                gateNumber.setText(serialCache.get(jsonObject.get("serial_id")));

                int totalPois = Integer.parseInt(jsonObject.get("total_pois").toString());
                TextView [] pois = new TextView[totalPois];
                for(int i = 0; i < totalPois; i++){
                    pois[i] = new TextView(this);
                    pois[i].setText(jsonObject.get("poi" + (i+1)).toString());
                    pois[i].setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    pois[i].setBackground(getResources().getDrawable(R.drawable.edittext_bg));
                    pois[i].setPadding(3, 3, 3, 3);
                    //scroller.addView(pois[i]);

                }
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
        switch(id){
            case R.id.action_logout:
                logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.logout_button:
                logoutUser();
                break;
            case R.id.explore_area_button:
                setProgressBarIndeterminateVisibility(true);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "explore_more"));
                params.add(new BasicNameValuePair("gate_number", gateNumber.getText().toString()));
                asyncRequest = new JSONParser(this, responseURL);
                asyncRequest.execute(params);
                break;
        }
    }

    private void logoutUser(){
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
        Toast.makeText(getApplicationContext(), "Result: " + jObj.toString(), Toast.LENGTH_LONG).show();
    }
}
