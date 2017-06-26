package io.github.ovso.separategarbege;

import android.app.Application;
import android.content.ContextWrapper;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by jaeho on 2017. 6. 25
 */

public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    new Prefs.Builder()
        .setContext(this)
        .setMode(ContextWrapper.MODE_PRIVATE)
        .setPrefsName(getPackageName())
        .setUseDefaultSharedPreference(true)
        .build();
  }
}
