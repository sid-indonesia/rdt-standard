package io.ona.rdt_app.util;

import org.smartregister.job.SyncServiceJob;

import java.util.ArrayList;

import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.job.ImageUploadSyncServiceJob;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class Utils {
    public static final ArrayList<String> ALLOWED_LEVELS;
    public static final String DEFAULT_LOCATION_LEVEL = Constants.Tags.HEALTH_CENTER;

    static {
        ALLOWED_LEVELS = new ArrayList<>();
        ALLOWED_LEVELS.add(DEFAULT_LOCATION_LEVEL);
        ALLOWED_LEVELS.add(Constants.Tags.COUNTRY);
        ALLOWED_LEVELS.add(Constants.Tags.PROVINCE);
        ALLOWED_LEVELS.add(Constants.Tags.DISTRICT);
        ALLOWED_LEVELS.add(Constants.Tags.VILLAGE);
    }

    public static void scheduleJobsPeriodically() {
        ImageUploadSyncServiceJob
                .scheduleJob(ImageUploadSyncServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                        getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));

        SyncServiceJob
                .scheduleJob(SyncServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                        getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));
    }

    public static void scheduleJobsImmediately() {
        ImageUploadSyncServiceJob
                .scheduleJobImmediately(ImageUploadSyncServiceJob.TAG);

        SyncServiceJob
                .scheduleJobImmediately(SyncServiceJob.TAG);
    }

    private static long getFlexValue(long value) {
        final long MINIMUM_JOB_FLEX_VALUE = 1;
        long minutes = MINIMUM_JOB_FLEX_VALUE;
        if (value > MINIMUM_JOB_FLEX_VALUE) {
            minutes = (long) Math.ceil(value / 3);
        }
        return minutes;
    }
}
