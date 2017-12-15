package app.androidhive.info.realm.app;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class Prefs {

    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";
    private static Prefs instance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context) {
        if (instance == null) {
            instance = new Prefs(context);
        }
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void setPreLoad(boolean totalTime) {
        sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, totalTime)
                .apply();
    }

    public boolean getPreLoad(){
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }

}
