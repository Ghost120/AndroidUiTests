package com.exness.pushtest;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import com.exness.pushtest.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.tinkoff.allure.annotations.DisplayName;

import static com.exness.pushtest.Helper.checkThatInsertRowInDB;
import static com.exness.pushtest.Helper.getSizeDB;
import static com.exness.pushtest.Helper.sendPushNotificationAndWaitResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by konstantin on 02.04.2018.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TestWithUIAutomator {
    private UiDevice mDevice;
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String BASIC_SAMPLE_PACKAGE = "com.exness.pushtest";
    private static final long DELAY = 12000L;


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        launchApplication();
    }

    @Test
    @DisplayName("Check that work push notification")
    public void testWorkPushNotificationWhenAppIsNotActive(){
        mDevice.pressBack();
        sendPushNotificationAndWaitResponse(DELAY);
        launchApplication();
        checkThatInsertRowInDB(DELAY, getSizeDB());
    }


    private void launchApplication() {
        Context context = InstrumentationRegistry.getContext();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);

        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }
}
