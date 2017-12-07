package radonsoft.firenotes;

import android.app.Application;
import android.content.Context;

/**
 * Created by RRCFo on 08.12.2017.
 */

public class FireNotes extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
