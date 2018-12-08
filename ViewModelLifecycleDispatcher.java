package com.monyxApp.livedata;

import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ServiceLifecycleDispatcher;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

public class ViewModelLifecycleDispatcher {
    private final LifecycleRegistry mRegistry;
    private final Handler mHandler;
    private ViewModelLifecycleDispatcher.DispatchRunnable mLastDispatchRunnable;

    /**
     * @param provider {@link LifecycleOwner} for a ViewModel
     */
    public ViewModelLifecycleDispatcher(@NonNull LifecycleOwner provider) {
        mRegistry = new LifecycleRegistry(provider);
        mHandler = new Handler();
    }

    private void postDispatchRunnable(Lifecycle.Event event) {
        if (mLastDispatchRunnable != null) {
            mLastDispatchRunnable.run();
        }
        mLastDispatchRunnable = new ViewModelLifecycleDispatcher.DispatchRunnable(mRegistry, event);
        mHandler.postAtFrontOfQueue(mLastDispatchRunnable);
    }

    /**
     * Must be called in order to start listening (Preferred at ViewModel constructor).
     */
    public void onCreateAndStart() {
        postDispatchRunnable(Lifecycle.Event.ON_CREATE);
        postDispatchRunnable(Lifecycle.Event.ON_START);
    }

    /**
     * Must be a first call in {@link android.arch.lifecycle.ViewModel#onCleared()} method, even before super.OnCleared
     * call.
     */
    public void onStopAndDestroy() {
        postDispatchRunnable(Lifecycle.Event.ON_STOP);
        postDispatchRunnable(Lifecycle.Event.ON_DESTROY);
    }

    /**
     * @return {@link Lifecycle} for the given {@link LifecycleOwner}
     */
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    static class DispatchRunnable implements Runnable {
        private final LifecycleRegistry mRegistry;
        final Lifecycle.Event mEvent;
        private boolean mWasExecuted = false;

        DispatchRunnable(@NonNull LifecycleRegistry registry, Lifecycle.Event event) {
            mRegistry = registry;
            mEvent = event;
        }

        @Override
        public void run() {
            if (!mWasExecuted) {
                mRegistry.handleLifecycleEvent(mEvent);
                mWasExecuted = true;
            }
        }
    }
}
