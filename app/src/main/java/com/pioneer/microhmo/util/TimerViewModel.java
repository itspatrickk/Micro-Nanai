package com.pioneer.microhmo.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {
    private final MutableLiveData<Long> remainingTime = new MutableLiveData<>(120000L); // 2 minutes in milliseconds
    private final MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<>(false);

    public LiveData<Long> getRemainingTime() {
        return remainingTime;
    }

    public LiveData<Boolean> isTimerRunning() {
        return isTimerRunning;
    }

    public void setRemainingTime(long time) {
        remainingTime.setValue(time);
    }

    public void setTimerRunning(boolean running) {
        isTimerRunning.setValue(running);
    }
}
