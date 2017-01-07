package ch.christofbuechi.delaywarner.network;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerWrapper {

    public Scheduler getMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler getIOScheduler() {
        return Schedulers.io();
    }
}
