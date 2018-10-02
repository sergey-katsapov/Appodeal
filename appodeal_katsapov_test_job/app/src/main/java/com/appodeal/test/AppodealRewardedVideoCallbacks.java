package com.appodeal.test;

import android.app.Activity;
import com.appodeal.ads.RewardedVideoCallbacks;

class AppodealRewardedVideoCallbacks implements RewardedVideoCallbacks {
    private final Activity mActivity;

    AppodealRewardedVideoCallbacks(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onRewardedVideoLoaded() {
        Utils.showToast(mActivity, "onRewardedVideoLoaded method");
    }

    @Override
    public void onRewardedVideoFailedToLoad() {
        Utils.showToast(mActivity, "onRewardedVideoFailedToLoad method");
    }

    @Override
    public void onRewardedVideoShown() {
        Utils.showToast(mActivity, "onRewardedVideoShown method");
    }

    @Override
    public void onRewardedVideoFinished(int amount, String name) {
        Utils.showToast(mActivity, String.format("onRewardedVideoFinished method. Reward: %d %s", amount, name));
    }

    @Override
    public void onRewardedVideoClosed(boolean finished) {
        Utils.showToast(mActivity, String.format("onRewardedVideoClosed method,  finished: %s", finished));
    }
}
