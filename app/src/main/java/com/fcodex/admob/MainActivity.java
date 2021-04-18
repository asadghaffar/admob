package com.fcodex.admob;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class MainActivity extends AppCompatActivity {

    private UnifiedNativeAd nativeAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, initializationStatus -> {});

        bannerAdMethod();
        interstitialAdMethod();
        nativeAdMethod();

    }

    private void bannerAdMethod() {

        AdView bannerAdView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        /*bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d("fail", "ad fail");
            }

            @Override
            public void onAdOpened() {
                Log.d("open", "ad open");
            }

            @Override
            public void onAdLoaded() {
                Log.d("load", "ad load");
            }
        });*/
    }

    // Interstitial ad start
    private void interstitialAdMethod() {
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_test_id));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    // Interstitial ad end

    // Native Ad Start
    private void nativeAdMethod() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_advance_test_id));
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            if (nativeAd != null)
                nativeAd = unifiedNativeAd;
            CardView cardView = findViewById(R.id.main_ad_container);
            @SuppressLint("InflateParams") UnifiedNativeAdView adView = (UnifiedNativeAdView)
                    getLayoutInflater().inflate(R.layout.native_ad_layout, null);
            populateNativeAdView(unifiedNativeAd, adView);
            cardView.removeAllViews();
            cardView.addView(adView);
        });

        AdLoader adLoader = builder.withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                Toast.makeText(MainActivity.this, "Fail to load ad", Toast.LENGTH_SHORT).show();
                super.onAdFailedToLoad(i);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd1, UnifiedNativeAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_text_body));
        adView.setStarRatingView(adView.findViewById(R.id.ad_star_rating));
        adView.setMediaView(adView.findViewById(R.id.ad_media_view));
        adView.setCallToActionView(adView.findViewById(R.id.ad_button_call_to_action_view));
        adView.setIconView(adView.findViewById(R.id.adv_icon));

        adView.getMediaView().setMediaContent(nativeAd1.getMediaContent());
        ((TextView) adView.getHeadlineView()).setText(nativeAd1.getHeadline());

        if (nativeAd1.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getBodyView()).setText(nativeAd1.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
        }

        if (nativeAd1.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd1.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        if (nativeAd1.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd1.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd1.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView())
                    .setImageDrawable((nativeAd1.getIcon()).getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd1.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            ((Button) adView.getCallToActionView()).setText(nativeAd1.getCallToAction());
        }

        adView.setNativeAd(nativeAd1);

    }
    // Native Ad End

    @Override
    protected void onDestroy() {
        if (nativeAd!=null)
            nativeAd.destroy();
        super.onDestroy();
    }
}