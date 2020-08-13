package io.ona.rdt.job;

import android.content.Context;

import androidx.annotation.NonNull;
import io.ona.rdt.application.RDTApplication;

import org.smartregister.job.SyncServiceJob;
import org.smartregister.sync.intent.SyncIntentService;

/**
 * Created by Vincent Karuri on 28/08/2019
 */
public class RDTSyncServiceJob extends SyncServiceJob {

    public static final String TAG = "RDTSyncServiceJob";

    public RDTSyncServiceJob(Class<? extends SyncIntentService> serviceClass) {
        super(serviceClass);
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        return super.onRunJob(params);
    }

    @Override
    public Context getApplicationContext() {
        return RDTApplication.getInstance().getApplicationContext();
    }
}
