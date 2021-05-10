package com.qurankareem.alaajami.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.qurankareem.alaajami.R;
import com.qurankareem.alaajami.View.ReadFileActivity;

import java.util.Random;


public class ServicePlayer extends Service implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{
    MediaPlayer mediaPlayer;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    boolean isRepeat, isShuffle, isBuffering = false,isPlayAfterBuffering = true;
    private int buffer;
    public static String PAUSE = "com.qurankareem.alaajami.PAUSE";
    public static String PLAY = "com.qurankareem.alaajami.PLAY";
    public static String NEXT = "com.qurankareem.alaajami.NEXT";
    public static String PREV = "com.qurankareem.alaajami.PREV";
    public static String CLOSE = "com.qurankareem.alaajami.CLOSE";
    public static String ALARM_PAUSE= "com.qurankareem.alaajami.ALARM_PAUSE";
    private final IBinder mBinder = new PlayerBinder();
    private int NOTIFICATION_ID = 111;
    public static final String BUFFERING = "com.qurankareem.alaajami.ServicePlayer.BUFFERING";
    public static final String UPDATE_UI = "com.qurankareem.alaajami.ServicePlayer.UPDATE_UI";
    public static final String UPDATE_UI_PRE = "com.qurankareem.alaajami.ServicePlayer.UPDATE_UI_PRE";
    public static final String UPDATE_UI_NEXT = "com.qurankareem.alaajami.ServicePlayer.UPDATE_UI_NEXT";
    public static boolean clickNext=false;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null) {
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // Call Dropped or rejected or Restart service play media
                    resume();
                } else {
                    pause();
                }
            }

            String action = intent.getAction();
            if (action != null) {

                if (action.equals(PAUSE) || action.equals(ALARM_PAUSE)) {
                    pause();
                }

                if (action.equals(PLAY)) {
                    resume();
                }

                if (action.equals(NEXT)) {
                    next();
                }

                if (action.equals(PREV)) {
                    pre();
                }
                if(action.equals(CLOSE)){
                    pause();
                    hideNotification();
                }

            }
        }
    };


    public void showNotification() {
        String CHANNEL_ID = "controls_channel_id";
        String CHANNEL_NAME = "controls_channel";
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null,null);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);

        }else{
            notificationBuilder = new NotificationCompat.Builder(
                    getApplicationContext(),CHANNEL_ID);
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tvTitle, ReadFileActivity.title);
        RemoteViews remoteSmallViews= new RemoteViews(getPackageName(),R.layout.layout_notification_small);
        remoteSmallViews.setTextViewText(R.id.tvTitle, ReadFileActivity.title);


        Intent nextIntent = new Intent(NEXT);
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnNext, pendingNextIntent);
        remoteSmallViews.setOnClickPendingIntent(R.id.btnNext, pendingNextIntent);

        Intent prevIntent = new Intent(PREV);
        PendingIntent pendingPrevIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnPrev, pendingPrevIntent);

        remoteSmallViews.setOnClickPendingIntent(R.id.btnPrev, pendingPrevIntent);


        if (isPlay()) {
            Intent pauseIntent = new Intent(PAUSE);
            PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, pauseIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btnPlay, pendingPauseIntent);
            remoteViews.setImageViewResource(R.id.btnPlay, R.drawable.ic_stop_no_circle);

            remoteSmallViews.setOnClickPendingIntent(R.id.btnPlay, pendingPauseIntent);
            remoteSmallViews.setImageViewResource(R.id.btnPlay, R.drawable.ic_stop_no_circle);
        } else {
            Intent playIntent = new Intent(PLAY);
            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, playIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btnPlay, pendingPlayIntent);
            remoteViews.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_no_circle);

            remoteSmallViews.setOnClickPendingIntent(R.id.btnPlay, pendingPlayIntent);
            remoteSmallViews.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_no_circle);
        }



        Intent closeIntent = new Intent(CLOSE);
        PendingIntent pendingCloseIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, closeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnClose, pendingCloseIntent);
        remoteSmallViews.setOnClickPendingIntent(R.id.btnClose, pendingCloseIntent);

        Random generator = new Random();

        Intent intent = new Intent(this, ReadFileActivity.class);
        intent.putExtra("pos","1");
        intent.putExtra("title",ReadFileActivity.title);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, generator.nextInt(),
                intent,  PendingIntent.FLAG_ONE_SHOT);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_play_no_circle)
                .setContentIntent(contentIntent)
                .setCustomContentView(remoteSmallViews)
                .setCustomBigContentView(remoteViews);

        Notification notification = notificationBuilder.build();
        startForeground(NOTIFICATION_ID,notification);

        remoteViews.setImageViewResource(R.id.imgThumb,R.drawable.bg);


    }



    public void hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return START_REDELIVER_INTENT;
    }

    public class PlayerBinder extends Binder {
        public  ServicePlayer getService() {
            return  ServicePlayer.this;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }

    public void onCreate() {
        super.onCreate();
        // instance player object
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnErrorListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction(PAUSE);
        filter.addAction(PLAY);
        filter.addAction(NEXT);
        filter.addAction(PREV);
        filter.addAction(CLOSE);
        filter.addAction(UPDATE_UI_NEXT);
        filter.addAction(ALARM_PAUSE);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.i("DESTROY SERVICE", "destroy");
        unregisterReceiver(receiver);
    }

    public void play(String path) {
        try {
            if (mediaPlayer != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mediaPlayer.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                }else {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(path);
                }catch (Exception e){

                }
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
                isBuffering = true;
                isPlayAfterBuffering = true;
                Intent intent = new Intent(BUFFERING);
                ServicePlayer.this.sendBroadcast(intent);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("LOI ME ROI","LOI CMNR");
        }
    };

    public void next(){

        try {
                Intent intent = new Intent(UPDATE_UI);
                intent.putExtra("click_next",true);
                ServicePlayer.this.sendBroadcast(intent);
                showNotification();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void pre(){

        try {
            Intent intent = new Intent(UPDATE_UI);
            intent.putExtra("click_pre",true);
            ServicePlayer.this.sendBroadcast(intent);
            showNotification();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    public void pause() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Intent intent = new Intent(UPDATE_UI);
                ServicePlayer.this.sendBroadcast(intent);
                showNotification();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void resume() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                Intent intent = new Intent(UPDATE_UI);
                 ServicePlayer.this.sendBroadcast(intent);
                showNotification();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public int getTotalTime() {
        if (isBuffering == false) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public int getElapsedTime() {
        return mediaPlayer.getCurrentPosition();
    }

    public boolean getBuffering() {
        return this.isBuffering;
    }

    public void setBuffering(boolean flag) {
        this.isBuffering = flag;
    }

    public boolean getPlayAfterBuffering() {
        return this.isPlayAfterBuffering;
    }

    public void setPlayAfterBuffering(boolean flag) {
        if (flag == false) {
            cancelNotification();
        } else {
            showNotification();
        }
        this.isPlayAfterBuffering = flag;
    }

    public void cancelNotification() {
        stopForeground(true);
    }


    public void seek(int pos) {
        mediaPlayer.seekTo(pos);
    }

    public boolean isPlay() {
        if (mediaPlayer.isPlaying()) {
            return true;
        } else {
            return false;
        }
    }


    public void setRepeat(boolean flag) {
        this.isRepeat = flag;
    }

    public void setShuffle(boolean flag) {
        this.isShuffle = flag;
    }

    public boolean getRepeat() {
        return this.isRepeat;
    }

    public boolean getShuffle() {
        return this.isShuffle;
    }

    public void onCompletion(MediaPlayer mp) {
        if (!isBuffering) {

            play(ReadFileActivity.path);
        }
    }

    public int getAudioSessionId(){
        return mediaPlayer.getAudioSessionId();
    }

    public void setAuxEffectSendLevel(float level){
        mediaPlayer.setAuxEffectSendLevel(level);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        this.buffer = i;
    }

    public int getBufferingDownload() {
        return this.buffer;
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        play(ReadFileActivity.path);
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (isPlayAfterBuffering) {
            mediaPlayer.start();
            showNotification();
            Intent intent = new Intent(UPDATE_UI);
             ServicePlayer.this.sendBroadcast(intent);
        }
        isBuffering=false;
    }
}