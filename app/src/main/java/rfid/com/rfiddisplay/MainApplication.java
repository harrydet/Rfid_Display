package rfid.com.rfiddisplay;


import android.app.Application;
import android.util.Log;

import com.parse.Parse;

public class MainApplication extends Application{
    private static MainApplication instance = new MainApplication();

    public MainApplication(){
        instance = this;
    }

    public void onCreate(){
        super.onCreate();
        Parse.initialize(this, "g53i1BHiKF9rTuMqZk0ctwtU0QPnpYIPCMspbCv8", "QtXRVNhcWgvSZF58wxBcHSyvYOgAp2rJbGUwCv69");
        Log.e("Status", "Initialized");
    }
}
