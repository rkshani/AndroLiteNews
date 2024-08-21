package com.andromob.androlite.utils;

import static android.view.View.GONE;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public class AdsManager {
    public int inter_i = 0;
    public InterstitialAd googleFullscreen;
    AdView admobSmallBanner;

    private static AdsManager mInstance;

    public static synchronized AdsManager getInstance() {
        if (mInstance == null) {
            mInstance = new AdsManager();
        }
        return mInstance;
    }

    public AdsManager() {
    }

    public interface InterAdListener {
        void onClick(String type);
    }


    public void showBannerAd(Activity activity, FrameLayout adContainerView) {
        if (Config.isAdsEnabled) {
            loadAdMOBBanner(activity, adContainerView);
        } else {
            adContainerView.setVisibility(GONE);
        }
    }

    public void loadAdMOBBanner(Activity activity, FrameLayout adContainerView) {
        adContainerView.setVisibility(GONE);
        admobSmallBanner = new AdView(activity);
        admobSmallBanner.setAdUnitId(Config.ADMOB_SMALL_BANNER_AD_ID);
        AdRequest adRequest =
                new AdRequest.Builder().build();
        AdSize adSize = getAdSize(activity);
        admobSmallBanner.setAdSize(adSize);
        admobSmallBanner.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adContainerView.setVisibility(GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adContainerView.removeAllViews();
                adContainerView.addView(admobSmallBanner);
                adContainerView.setVisibility(View.VISIBLE);
            }
        });
        admobSmallBanner.loadAd(adRequest);
    }

    public AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public void showInterAdOnClick(Activity activity, InterAdListener interAdListener, final String type) {
        if (Config.isAdsEnabled) {
            if (inter_i == Config.SHOW_INTER_ON_CLICKS) {
                inter_i = 0;
                if (googleFullscreen != null) {
                    googleFullscreen.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            interAdListener.onClick(type);
                            loadInterAd(activity);
                            super.onAdDismissedFullScreenContent();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            interAdListener.onClick(type);
                            loadInterAd(activity);
                            super.onAdFailedToShowFullScreenContent(adError);
                        }
                    });
                    googleFullscreen.show(activity);
                } else {
                    interAdListener.onClick(type);
                    loadInterAd(activity);
                }
            } else {
                inter_i++;
                interAdListener.onClick(type);
            }
        } else {
            interAdListener.onClick(type);
        }
    }


    public void loadInterAd(Activity activity) {
        if (Config.isAdsEnabled) {
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    googleFullscreen = null;
                    // Proceed to the next level.
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NotNull com.google.android.gms.ads.AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }
            };
            InterstitialAd.load(
                    activity,
                    Config.ADMOB_INTER_AD_ID,
                    new AdRequest.Builder().build(),
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd ad) {
                            googleFullscreen = ad;
                            googleFullscreen.setFullScreenContentCallback(fullScreenContentCallback);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            googleFullscreen = null;
                            inter_i = 0;
                        }
                    });
        }
    }

    public void destroyBannerAds() {
        if (Config.isAdsEnabled) {
            if (admobSmallBanner != null) {
                admobSmallBanner.removeAllViews();
                admobSmallBanner.destroy();
            }
        }
    }

    public void destroyInterAds() {
        if (Config.isAdsEnabled) {
            if (googleFullscreen != null) {
                googleFullscreen = null;
            }
        }
    }
}
