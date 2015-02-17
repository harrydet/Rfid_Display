package rfid.com.rfiddisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Harry on 16/02/2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent){
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_airplane_white);

        return largeIcon;
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent){
        return AuthenticatedActivity.class;
    }
}
