package com.qurankareem.eldjazairiyassin.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;*/

import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;

import com.ironsource.mediationsdk.IronSource;
import com.qurankareem.eldjazairiyassin.Adapters.ItemAdapter;
import com.qurankareem.eldjazairiyassin.Ads.AdsHelper;
import com.qurankareem.eldjazairiyassin.Models.DownloadReceiver;
import com.qurankareem.eldjazairiyassin.Models.DownloadService;
import com.qurankareem.eldjazairiyassin.Models.Helpers;
import com.qurankareem.eldjazairiyassin.R;
import com.qurankareem.eldjazairiyassin.Services.ServicePlayer;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ReadFileActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener {


    public static int postion;
    private StringBuilder text;
    Handler handler = new Handler();
    private TextView tvmain;
    private AdView adView;
    private int index = 20;
    private BufferedReader reader;
    private LinearLayout lyread;
    private boolean swithnight = false;
    private static final int PERMISSION_REQUEST_CODE = 101;
    public static String title = "";
    private String TAG = "ReadFileActivity";
    private SparseArray<Float> progressMap = new SparseArray<>();
    private String pos;
    public static String path;
    private String stateapp = "";
    private SeekBar prgTrack;
    private ImageButton btnShuffle, btnRepeat, btnPrev, btnNext, btnPlayAndPause;
    private TextView tvTotalTime, tvElapsedTime;
    private AVLoadingIndicatorView indicator;
    private InterstitialAd mInterstitialAd;
    ServicePlayer audioPlayerService;
    boolean isRepeat, isShuffle;
    public static final String ON_TRACK_CLICK_PLAY = "com.qurankareem.eldjazairiyassin.ON_TRACK_CLICK_PLAY";
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private AdView mAdView;
    private LinearLayout adContainer;
    int adCounter = 0;

    AdsHelper ads = new AdsHelper(ReadFileActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);
        editor = getSharedPreferences("pos", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("pos", MODE_PRIVATE);

        tvmain = findViewById(R.id.tvTEXT);
        lyread = findViewById(R.id.lyread);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        tvElapsedTime = (TextView) findViewById(R.id.tvElapsedTime);
        prgTrack = (SeekBar) findViewById(R.id.seekTrack);
        btnPlayAndPause = (ImageButton) findViewById(R.id.btnPlay);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        indicator = (AVLoadingIndicatorView) findViewById(R.id.mainIndicator);


        // final FrameLayout frameLayout = findViewById(R.id.bannerContainer);
        ads.createBanner();
        ads.loadInterstitial(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ReadFileActivity.this,
                        ServicePlayer.class);
                getApplicationContext().bindService(intent, serviceConnection,
                        Context.BIND_AUTO_CREATE);
            }
        }).run();

        title = getIntent().getStringExtra("title");
        pos = getIntent().getStringExtra("pos");
        postion = Integer.parseInt(pos);


        if (postion < 10) {
            path = getString(R.string.url_sound) + "00" + postion + ".mp3";
        } else if (postion > 10 && postion < 100) {
            path = getString(R.string.url_sound) + "0" + postion + ".mp3";
        } else {
            path = getString(R.string.url_sound) + postion + ".mp3";
        }

        getSupportActionBar().setTitle(title);


        text = new StringBuilder();

        try {
            InputStream input = getAssets().open(pos);
            InputStreamReader isr = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvmain.setText(text);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });

        btnPlayAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayerService.isPlay()) {
                    audioPlayerService.pause();

                    btnPlayAndPause
                            .setImageResource(R.drawable.ic_play);
                } else {
                    audioPlayerService.resume();

                    btnPlayAndPause
                            .setImageResource(R.drawable.ic_stop);
                }
                if(adCounter % 4 == 0 ){
                    ads.showInterstitial();
                }
                adCounter++;
                Log.v("ReadFileActivity","adCounter " + adCounter);
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShuffle) {
                    isShuffle = true;
                    audioPlayerService.setShuffle(true);
                    btnShuffle
                            .setImageResource(R.drawable.ic_shuffle_gr);
                } else {
                    isShuffle = false;
                    audioPlayerService.setShuffle(false);
                    btnShuffle.setImageResource(R.drawable.ic_shuffle);
                }
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isRepeat) {
                    isRepeat = true;
                    audioPlayerService.setRepeat(true);
                    btnRepeat.setImageResource(R.drawable.ic_repeat_gr);
                } else {
                    isRepeat = false;
                    audioPlayerService.setRepeat(false);
                    btnRepeat.setImageResource(R.drawable.ic_repeat);
                }
            }
        });

    }



    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ServicePlayer.UPDATE_UI:
                    updatePlaybackUI();
                    indicator.setVisibility(View.GONE);
                    btnPlayAndPause.setVisibility(View.VISIBLE);

                    boolean click_next = intent.getBooleanExtra("click_next", false);
                    boolean click_pre = intent.getBooleanExtra("click_pre", false);
                    if (click_next) {
                        next();
                    }
                    if (click_pre) {
                        previous();
                    }
                    if(adCounter % 4 == 0 ){
                        ads.showInterstitial();
                    }
                    adCounter++;
                    Log.v("ReadFileActivity","adCounter " + adCounter);
                    break;

                case ServicePlayer.BUFFERING:
                    indicator.setVisibility(View.VISIBLE);
                    btnPlayAndPause.setVisibility(View.INVISIBLE);
                    break;


                case ON_TRACK_CLICK_PLAY:
                    if (audioPlayerService != null) {
                        audioPlayerService.play(path);
                    }
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        ads.showInterstitial();
        super.onBackPressed();
    }

    private void previous() {
        if (postion == 1) {
            postion = 1;
        } else {
            postion--;
        }

        getSupportActionBar().setTitle(MainActivity.listItems.get(postion - 1).getTitle());
        ReadFileActivity.title = MainActivity.listItems.get(postion - 1).getTitle();
        tvmain.setText("");
        text = new StringBuilder();
        try {
            InputStream input = getAssets().open(String.valueOf(postion));
            InputStreamReader isr = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvmain.setText(text);

        if (postion < 10) {
            path = getString(R.string.url_sound) + "00" + postion + ".mp3";
        } else if (postion > 10 && postion < 100) {
            path = getString(R.string.url_sound) + "0" + postion + ".mp3";
        } else {
            path = getString(R.string.url_sound) + postion + ".mp3";
        }

        editor.putInt("currentPos", postion);
        editor.apply();

        if (audioPlayerService != null) {
            audioPlayerService.play(path);
        }
    }

    public void updatePlaybackUI() {


        if (audioPlayerService.isPlay()) {
            btnPlayAndPause.setImageResource(R.drawable.ic_stop);
        } else {
            btnPlayAndPause.setImageResource(R.drawable.ic_play);
        }


        prgTrack.setOnSeekBarChangeListener(this);
        updateProgress();

    }

    public void updateProgress() {
        handler.postDelayed(updateTime, 100);
    }

    private void next() {
        if (postion == 114) {
            postion = 1;
        } else {
            postion++;
        }

        getSupportActionBar().setTitle(MainActivity.listItems.get(postion - 1).getTitle());
        ReadFileActivity.title = MainActivity.listItems.get(postion - 1).getTitle();
        tvmain.setText("");
        text = new StringBuilder();
        try {
            InputStream input = getAssets().open(String.valueOf(postion));
            InputStreamReader isr = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvmain.setText(text);

        if (postion < 10) {
            path = getString(R.string.url_sound) + "00" + postion + ".mp3";
        } else if (postion > 10 && postion < 100) {
            path = getString(R.string.url_sound) + "0" + postion + ".mp3";
        } else {
            path = getString(R.string.url_sound) + postion + ".mp3";
        }

        editor.putInt("currentPos", postion);
        editor.apply();

        if (audioPlayerService != null) {
            audioPlayerService.play(path);
        }


    }

    private Runnable updateTime = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            prgTrack.setSecondaryProgress(audioPlayerService
                    .getBufferingDownload());

            long totalDuration = audioPlayerService.getTotalTime();
            long currentDuration = audioPlayerService.getElapsedTime();
            tvTotalTime.setText("" + Helpers.timer(totalDuration));
            tvElapsedTime.setText("" + Helpers.timer(currentDuration));
            int progress = (int) (Helpers.getProgressPercentage(currentDuration,
                    totalDuration));
            prgTrack.setProgress(progress);
            handler.postDelayed(this, 100);
        }
    };


    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            // audioPlayerService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            audioPlayerService = ((ServicePlayer.PlayerBinder) service)
                    .getService();
            updatePlaybackUI();
            if (Integer.parseInt(pos) != -1) {

                int currentPos = prefs.getInt("currentPos", 0);
                if (ItemAdapter.click && Integer.parseInt(pos) != currentPos) {
                    audioPlayerService.play(path);

                    editor.putInt("currentPos", postion);
                    editor.apply();

                }

            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }


    /// selected items menu:
    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int check = item.getItemId();
        switch (check) {

            case R.id.icDownload:

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    downloadSound();
                }

                break;
            case R.id.share_read:
                onClickShareApp();
                break;

            case R.id.swithnight:
                setModeNight();
                break;

            case R.id.size_change:
                if (index >= 20 && index <= 30) {
                    index += 3;
                    setTextSize(index);
                    //Toast.makeText(this, "Up : "+String.valueOf(index), Toast.LENGTH_SHORT).show();
                }
                if (index > 30) {
                    index = 20;
                    setTextSize(index);
                    //Toast.makeText(this, "Down : "+String.valueOf(index), Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                ads.showInterstitial();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    private void setModeNight() {
        if (!swithnight) {
            swithnight = true;
            lyread.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            tvmain.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvmain.setBackground(ContextCompat.getDrawable(this, R.drawable.border_night));

        } else {
            swithnight = false;
            lyread.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvmain.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tvmain.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permision), Toast.LENGTH_SHORT).show();

                    downloadSound();


                } else {
                    Toast.makeText(this, getString(R.string.requst_perm_failed), Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel(getString(R.string.need_perm),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermission();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            requestPermission();
                                            Toast.makeText(ReadFileActivity.this, getString(R.string.please_accept_perm), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void downloadSound() {

        // execute this when the downloader must be fired

        ProgressDialog mProgressDialog;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(ReadFileActivity.this);
        mProgressDialog.setMessage(getString(R.string.download_sprah));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        // this is how you fire the downloader
        mProgressDialog.show();
        Intent intent = new Intent(ReadFileActivity.this, DownloadService.class);
        intent.putExtra("quran_sound", path);
        intent.putExtra("surah_name", title);
        intent.putExtra("receiver", new DownloadReceiver(ReadFileActivity.this, mProgressDialog, new Handler()));
        startService(intent);
        ads.showInterstitial();

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListner) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), cancelListner)
                .create()
                .show();
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }


    public void setTextSize(int size) {
        tvmain.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void onClickShareApp() {
        Intent sharePst = new Intent(Intent.ACTION_SEND);
        sharePst.setType("text/plain");
        sharePst.putExtra(Intent.EXTRA_TEXT, tvmain.getText().toString());

        startActivity(Intent.createChooser(sharePst, getString(R.string.choose_app_share)));
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.postDelayed(updateTime, 100);

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTime);
        int totalDuration = audioPlayerService.getTotalTime();
        int currentPosition = Helpers.progressToTimer(seekBar.getProgress(),
                totalDuration);
        audioPlayerService.seek(currentPosition);
        updateProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
        // final FrameLayout frameLayout = findViewById(R.id.bannerContainer);
        ads.createBanner();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServicePlayer.UPDATE_UI);
        intentFilter.addAction(ServicePlayer.BUFFERING);
        intentFilter.addAction(ON_TRACK_CLICK_PLAY);
        registerReceiver(notificationReceiver, intentFilter);

        if (audioPlayerService != null) {
            updatePlaybackUI();
        }

    }

    @Override
    protected void onPause() {
        IronSource.onPause(this);
        ads.distroyBanner();
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }

}
