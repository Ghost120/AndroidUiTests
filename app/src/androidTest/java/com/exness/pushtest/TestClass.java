package com.exness.pushtest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.exness.pushtest.activities.MainActivity;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.exness.pushtest.CustomSteps.getText;
import static com.exness.pushtest.Helper.checkThatInsertRowInDB;
import static com.exness.pushtest.Helper.getCurrentQuote;
import static com.exness.pushtest.Helper.getSizeDB;
import static com.exness.pushtest.Helper.sendPushNotificationAndWaitResponse;

/**
 * Created by konstantin on 28.02.2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestClass {
    private static final long DELAY = 12000L;
    private static final String UI = "UI Test";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class,true,true);

    @Test
    public void testFirstStartApplication() {
        mActivityTestRule.getActivity().deleteDatabase("db");
        onView(withId(R.id.text))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(withText("PushTest")).check(matches(isDisplayed()));
    }

    @Test
    public void testAddNewQuote() {
        sendPushNotificationAndWaitResponse(DELAY);
        checkThatInsertRowInDB(DELAY, getSizeDB());
    }

    @Test
    public void testWorkPushNotificationOnStartApp() {
        testAddNewQuote();
        Float currentQuote = getCurrentQuote(DELAY);
        Log.i(UI,"current quote:" + currentQuote);
        Float lastQuteFromDB = Helper.getListQuoteFromDB().get(Helper.getSizeDB()-1).getValue();
        Log.i(UI,"quote from DB:" + currentQuote);
        Assert.assertEquals("Quote " + lastQuteFromDB + " not equals current quote "
                + currentQuote , currentQuote, lastQuteFromDB);
    }

    @Test
    public void testRotateScreen() {
        String textBeforeRotate = getText(withId(R.id.text));
        Log.i(UI, "text Before: "+textBeforeRotate);
        mActivityTestRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.text))
                .check(matches(withText(textBeforeRotate)));
        mActivityTestRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.text))
                .check(matches(withText(textBeforeRotate)));
    }

    @Test
    public void testWorkPushNotificationWhenAppIsNotActive() {
        // TODO: 02.03.2018 Необходимо найти способ нажатия кновпки назад без краша тестов
        Espresso.pressBack();
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        sendPushNotificationAndWaitResponse(DELAY);
        onView(withId(R.id.text))
                .check(matches(isDisplayed()));
        testAddNewQuote();
    }

    private void customStartApp() {
        // TODO: 02.03.2018 Необходима доработка
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Instrumentation.ActivityMonitor activityMonitor = instrumentation.addMonitor(MainActivity.class.getName(), null, false);
        Activity activity = instrumentation.waitForMonitorWithTimeout(activityMonitor, 1000);
    }
}
