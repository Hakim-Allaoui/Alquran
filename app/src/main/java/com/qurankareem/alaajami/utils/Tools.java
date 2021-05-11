package com.qurankareem.alaajami.utils;

import android.app.Activity;
import android.content.Intent;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.qurankareem.alaajami.R;
import com.qurankareem.alaajami.View.MainActivity;
import com.qurankareem.alaajami.View.SplashActivity;

import static android.os.SystemClock.sleep;
import static androidx.core.content.ContextCompat.startActivity;

public class Tools {

    public static FirebaseRemoteConfig mFirebaseRemoteConfig;

    public static String adNetwork = "fb";
    public static String fb_banner = "IMG_16_9_APP_INSTALL#182230885712915_182234792379191";
    public static String fb_Inter = "IMG_16_9_APP_INSTALL#182230885712915_182234475712556";
    public static String admob_banner = "ca-app-pub-3940256099942544/6300978111";
    public static String admob_Inter = "ca-app-pub-3940256099942544/1033173712";

    public static String fetchRemoteConfig(String key) {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        return mFirebaseRemoteConfig.getString(key);
    }

    static public void fetchAdNetwork(final Activity activity) {
        adNetwork = fetchRemoteConfig("adNetwork");
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(activity.getLocalClassName(), "Config params updated: " + updated);
                            Toast.makeText(activity.getApplicationContext(), "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();

                            Log.v( "Ads", "adNetwork :" + adNetwork);

                            Intent i=new Intent(activity.getBaseContext(), com.qurankareem.alaajami.View.MainActivity.class);
                            activity.startActivity(i);

                        } else {
                            Toast.makeText(activity.getApplicationContext(), "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                            sleep(10000);
                            Intent i=new Intent(activity.getBaseContext(), com.qurankareem.alaajami.View.MainActivity.class);
                            activity.startActivity(i);
                        }
                    }
                });
    }
}
