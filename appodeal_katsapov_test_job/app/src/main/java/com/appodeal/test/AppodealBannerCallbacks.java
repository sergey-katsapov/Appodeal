package com.appodeal.test;

import android.app.Activity;
import com.appodeal.ads.BannerCallbacks;

class AppodealBannerCallbacks implements BannerCallbacks {
    private final Activity mActivity;

    AppodealBannerCallbacks(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onBannerLoaded(int height, boolean isPrecache) {
        Utils.showToast(mActivity, String.format("onBannerLoaded method, %sdp, isPrecache: %s", height, isPrecache));
    }

    @Override
    public void onBannerFailedToLoad() {
        Utils.showToast(mActivity, "onBannerFailedToLoad method");
    }

    @Override
    public void onBannerShown() {
        Utils.showToast(mActivity, "onBannerShown method");
    }

    @Override
    public void onBannerClicked() {
        Utils.showToast(mActivity, "onBannerClicked method");
    }
}
