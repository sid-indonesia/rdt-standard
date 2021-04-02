package io.ona.rdt.robolectric.shadow;

import android.content.Context;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import io.ona.rdt.util.Utils;

/**
 * Created by Vincent Karuri on 20/07/2020
 */

@Implements(Utils.class)
public class UtilsShadow extends Shadow {

    private static MockCounter mockCounter;
    private static String parentLocationId = OpenSRPContextShadow.PARENT_LOCATION_ID;
    private static Context context;

    public static String getParentLocationId() {
        return parentLocationId;
    }

    public static void setParentLocationId(String parentLocationId) {
        UtilsShadow.parentLocationId = parentLocationId;
    }

    @Implementation
    public static void scheduleJobsImmediately() {
        getMockCounter().setCount(1);
    }

    @Implementation
    public static boolean isImageSyncEnabled() {
        return true;
    }

    public static synchronized void setMockCounter(MockCounter mockCounter) {
        UtilsShadow.mockCounter = mockCounter;
    }

    public static synchronized MockCounter getMockCounter() {
        return mockCounter;
    }

    public static void verifyUserAuthorization(Context context) {
        UtilsShadow.context = context;
    }

    public static Context getActiveContext() {
        return context;
    }
}
