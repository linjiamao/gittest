package yeeaoo.frist2016_06_15;

import android.app.Application;
import android.content.Context;

/**
 * Created by yo on 2016/6/15.
 */
public class MyApplication extends Application{
    private static Context context;
    private static String name;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        name = "zhang";
    }

    public static Context getContext(){
        return context;
    }
}
