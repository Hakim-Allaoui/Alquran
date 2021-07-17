package com.qurankareem.maheralmueaqly.Ads;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;


import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.qurankareem.maheralmueaqly.R;

import static com.ironsource.mediationsdk.IronSource.isInterstitialReady;


public class AdsHelper {

    private final Activity activity;
    private IronSourceBannerLayout banner;

    public AdsHelper(Activity p_activity) {
        activity = p_activity;
    }


    public void createBanner() {
        banner = IronSource.createBanner(activity, ISBannerSize.BANNER);
        final FrameLayout frameLayout = activity.findViewById(R.id.bannerContainer);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.addView(banner, 0, layoutParams);

        banner.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                // Called after a banner ad has been successfully loaded
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError error) {
                // Called after a banner has attempted to load an ad but failed.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frameLayout.removeAllViews();
                    }
                });
            }

            @Override
            public void onBannerAdClicked() {
                // Called after a banner has been clicked.
            }

            @Override
            public void onBannerAdScreenPresented() {
                // Called when a banner is about to present a full screen content.
            }

            @Override
            public void onBannerAdScreenDismissed() {
                // Called after a full screen content has been dismissed
            }

            @Override
            public void onBannerAdLeftApplication() {
                // Called when a user would be taken out of the application context.
            }
        });

        IronSource.loadBanner(banner);
    }

    public void distroyBanner() {
        IronSource.destroyBanner(banner);
    }



    public void loadInterstitial(final boolean reload) {


        IronSource.setInterstitialListener(new InterstitialListener() {
            /**
             * Invoked when Interstitial Ad is ready to be shown after load function was called.
             */
            @Override
            public void onInterstitialAdReady() {
            }
            /**
             * invoked when there is no Interstitial Ad available after calling load function.
             */
            @Override
            public void onInterstitialAdLoadFailed(IronSourceError error) {
            }
            /**
             * Invoked when the Interstitial Ad Unit is opened
             */
            @Override
            public void onInterstitialAdOpened() {
            }
            /*
             * Invoked when the ad is closed and the user is about to return to the application.
             */
            @Override
            public void onInterstitialAdClosed() {
                if(reload) IronSource.loadInterstitial();
            }
            /**
             * Invoked when Interstitial ad failed to show.
             * @param error - An object which represents the reason of showInterstitial failure.
             */
            @Override
            public void onInterstitialAdShowFailed(IronSourceError error) {
            }
            /*
             * Invoked when the end user clicked on the interstitial ad, for supported networks only.
             */
            @Override
            public void onInterstitialAdClicked() {
            }
            /** Invoked right before the Interstitial screen is about to open.
             *  NOTE - This event is available only for some of the networks.
             *  You should NOT treat this event as an interstitial impression, but rather use InterstitialAdOpenedEvent
             */
            @Override
            public void onInterstitialAdShowSucceeded() {
            }
        });

        IronSource.loadInterstitial();
    }



    public void loadInterstitial() {
        loadInterstitial(false);
    }

    public void showInterstitial() {
        if (!isInterstitialReady()) {
            IronSource.loadInterstitial();
//            return;
        }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (isInterstitialReady()) {
                    IronSource.showInterstitial(activity.getString(R.string.ironsource_interstitial_id));
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });
    }


}
