//package com.mobiwardrobe.mobiwardrobe;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import android.view.View;
//
//import androidx.test.espresso.UiController;
//import androidx.test.espresso.ViewAction;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import org.hamcrest.Matcher;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class BottomNavigationTest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testFragmentSwitching() {
//        // Проверяем отображение фрагмента ClothesFragment по умолчанию
//        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
//        onView(withId(R.id.fragment_container)).check(matches(hasDescendant(withId(R.id.rv_clothes))));
//
//        // Переключаемся на фрагмент WeatherFragment
//        onView(withId(R.id.bottomNavigationView)).perform(selectBottomNavigationItem(R.id.item_weather));
//
//        // Проверяем отображение фрагмента WeatherFragment
//        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
////        onView(withId(R.id.fragment_container)).check(matches(hasDescendant(withId(R.id.some_view_from_weather_fragment))));
//
//        // Переключаемся на фрагмент OutfitFragment
//        onView(withId(R.id.bottomNavigationView)).perform(selectBottomNavigationItem(R.id.item_outfit));
//
//        // Проверяем отображение фрагмента OutfitFragment
//        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
////        onView(withId(R.id.fragment_container)).check(matches(hasDescendant(withId(R.id.some_view_from_outfit_fragment))));
//
//        // Переключаемся на фрагмент CalendarFragment
//        onView(withId(R.id.bottomNavigationView)).perform(selectBottomNavigationItem(R.id.item_calendar));
//
//        // Проверяем отображение фрагмента CalendarFragment
//        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
////        onView(withId(R.id.fragment_container)).check(matches(hasDescendant(withId(R.id.some_view_from_calendar_fragment))));
//    }
//
//    private static ViewAction selectBottomNavigationItem(final int itemId) {
//        return new ViewAction() {
//            @Override
//            public Matcher<View> getConstraints() {
//                return ViewMatchers.isAssignableFrom(BottomNavigationView.class);
//            }
//
//            @Override
//            public String getDescription() {
//                return "Select bottom navigation item";
//            }
//
//            @Override
//            public void perform(UiController uiController, View view) {
//                BottomNavigationView bottomNavigationView = (BottomNavigationView) view;
//                bottomNavigationView.setSelectedItemId(itemId);
//            }
//        };
//    }
//}