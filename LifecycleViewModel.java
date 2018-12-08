package com.monyxApp.livedata;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ServiceLifecycleDispatcher;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LifecycleViewModel extends ViewModel implements LifecycleOwner {
    private final ViewModelLifecycleDispatcher mDispatcher = new ViewModelLifecycleDispatcher(this);

    protected LifecycleViewModel() {
        onCreateAndStart();
    }

    protected void onCreateAndStart() {
        mDispatcher.onCreateAndStart();
    }

    @Override
    protected void onCleared() {
        mDispatcher.onStopAndDestroy();
        super.onCleared();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mDispatcher.getLifecycle();
    }
}
