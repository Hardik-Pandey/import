package in.weclub.srmweclubapp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest{

    private String mStringToBetyped;
    private String mStringToBetypedForEmail;

    @Rule
    public ActivityTestRule<LoginActivity1> mActivityRule = new ActivityTestRule<>(LoginActivity1.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetypedForEmail = "abc@gmail.com";
        mStringToBetyped = "qwerty";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.

        onView(withId(R.id.moNo))
                .perform(typeText(mStringToBetypedForEmail), closeSoftKeyboard());

        onView(withId(R.id.pass3))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.moNo))
                .check(matches(withText(mStringToBetypedForEmail)));
        onView(withId(R.id.pass3))
                .check(matches(withText(mStringToBetyped)));

    }
}