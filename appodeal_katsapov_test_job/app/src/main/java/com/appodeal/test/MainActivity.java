package com.appodeal.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.utils.Log;
import com.appodeal.test.layout.AdTypeViewPager;
import com.appodeal.test.layout.SlidingTabLayout;

import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String CONSENT = "consent";

    public static final String APP_KEY = "fee50c333ff3825fd6ad6d38cff78154de3025546d47a84f";
    private String[] interstitial_networks, rewarded_video_networks, native_networks, banner_networks;
    boolean[] bannerNetworks;
    boolean[] rewardedNetworks;
    boolean[] nativeNetworks;
    boolean consent;

    public enum AdType {
        RewardedVideo(Appodeal.REWARDED_VIDEO),
        Banner(Appodeal.BANNER),
        Native(Appodeal.NATIVE);
        private final int mValue;
        AdType(int value) {
            mValue = value;
        }
        public int getValue() {
            return mValue;
        }
    }

    public enum AdTypePages {
        RewardedVideo(R.layout.rewarded_video, R.id.rewardedVideoLayout),
        Banner(R.layout.banner, R.id.bannerLayout),
        Native(R.layout.native_ad, R.id.nativeLayout);

        private final int mLayout;
        private final int mId;

        AdTypePages(int layout, int id) {
            mLayout = layout;
            mId = id;
        }
        public int getLayout() {
            return mLayout;
        }
        public int getId() {
            return mId;
        }
    }

    public static Intent getIntent(Context context, boolean consent) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(CONSENT, consent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        consent = getIntent().getBooleanExtra(CONSENT, false);
        android.util.Log.d("Appodeal", "Consent: " + consent);
        banner_networks = getResources().getStringArray(R.array.banner_networks);
        bannerNetworks = new boolean[banner_networks.length];
        for (int i = 0; i < banner_networks.length; i++) {
            bannerNetworks[i] = true;
        }
        rewarded_video_networks = getResources().getStringArray(R.array.rewarded_video_networks);
        rewardedNetworks = new boolean[rewarded_video_networks.length];
        for (int i = 0; i < rewarded_video_networks.length; i++) {
            rewardedNetworks[i] = true;
        }
        native_networks = getResources().getStringArray(R.array.native_networks);
        nativeNetworks = new boolean[native_networks.length];
        for (int i = 0; i < native_networks.length; i++) {
            nativeNetworks[i] = true;
        }

        if (Build.VERSION.SDK_INT >= 23 && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
        }

        Appodeal.setLogLevel(Log.LogLevel.none);
        ViewPager pager = (AdTypeViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(AdTypePages.values().length);
        pager.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child.findViewById(AdTypePages.RewardedVideo.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                }
                if (child.findViewById(AdTypePages.Native.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                }
                if (child.findViewById(AdTypePages.Banner.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });

        AdTypeAdapter adTypeAdapter = new AdTypeAdapter(getSupportFragmentManager());
        pager.setAdapter(adTypeAdapter);
        SlidingTabLayout slidingTabLayout = findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
        Appodeal.onResume(this, Appodeal.MREC);
    }

    @Override
    public void onBackPressed() {
        ViewGroup root = findViewById(android.R.id.content);
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            Object tag = child.getTag();
            if (tag != null && tag.equals("appodeal")) {
                root.removeView(child);
                return;
            }
        }
        super.onBackPressed();
    }


    public void initRewardedVideoSdkButton(View v) {
        if (consent) {
            Appodeal.initialize(this, APP_KEY, Appodeal.REWARDED_VIDEO);
            Appodeal.setRewardedVideoCallbacks(new AppodealRewardedVideoCallbacks(this));
        } else {
            android.util.Log.d("Appodeal", "SDK not initialized, consent false");
            Utils.showToast(this, "SDK not initialized, consent false");
        }
    }


    public void rewardedVideoShowButton(View v) {
        boolean isShown = Appodeal.show(this, Appodeal.REWARDED_VIDEO);
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void initBannerSdkButton(View v) {
        if (consent) {
            Appodeal.setBannerViewId(R.id.appodealBannerView);
            Appodeal.initialize(this, APP_KEY, Appodeal.BANNER);
            Appodeal.setBannerCallbacks(new AppodealBannerCallbacks(this));
        } else {
            android.util.Log.d("Appodeal", "SDK not initialized, consent false");
            Utils.showToast(this, "SDK not initialized, consent false");
        }
    }

    public void bannerShowButton(View v) {
        boolean isShown = Appodeal.show(this, Appodeal.BANNER);
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void bannerHideButton(View v) {
        Appodeal.hide(this, Appodeal.BANNER);
    }

    public void initNativeSdkButton(View v) {
        if (consent) {
            Appodeal.setNativeCallbacks(new AppodealNativeCallbacks(this));
            Appodeal.initialize(this, APP_KEY, Appodeal.NATIVE);
            Appodeal.setAutoCacheNativeIcons(true);
            Appodeal.setAutoCacheNativeMedia(true);
        } else {
            android.util.Log.d("Appodeal", "SDK not initialized, consent false");
            Utils.showToast(this, "SDK not initialized, consent false");
        }
    }

    public void nativeHideButton(View v) {
        hideNativeAds();
    }

    public void hideNativeAds() {
        LinearLayout nativeListView = findViewById(R.id.nativeAdsListView);
        nativeListView.removeAllViews();
        NativeListAdapter nativeListViewAdapter = (NativeListAdapter) nativeListView.getTag();
        if (nativeListViewAdapter != null) {
            for (int i = 0; i < nativeListViewAdapter.getCount(); i++) {
                NativeAd nativeAd = (NativeAd) nativeListViewAdapter.getItem(i);
                nativeAd.unregisterViewForInteraction();
            }
            nativeListViewAdapter.clear();
        }
    }

    public void nativeShowButton(View v) {
        hideNativeAds();
        List<NativeAd> nativeAds = Appodeal.getNativeAds(2);
        LinearLayout nativeAdsListView = findViewById(R.id.nativeAdsListView);
        NativeListAdapter nativeListViewAdapter = new NativeListAdapter(nativeAdsListView, 1);
        for (NativeAd nativeAd : nativeAds) {
            nativeListViewAdapter.addNativeAd(nativeAd);
        }
        nativeAdsListView.setTag(nativeListViewAdapter);
        nativeListViewAdapter.rebuild();
    }

    public static class AdTypeAdapter extends FragmentPagerAdapter {

        AdTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return AdTypePages.values().length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AdTypeFragment();
            Bundle args = new Bundle();
            args.putInt("layout", AdTypePages.values()[position].getLayout());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return AdTypePages.values()[position].name();
        }
    }

    public static class AdTypeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int layoutId = args.getInt("layout");
            return inflater.inflate(layoutId, container, false);
        }
    }
}
