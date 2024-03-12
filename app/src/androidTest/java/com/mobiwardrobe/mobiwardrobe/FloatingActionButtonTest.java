//package com.mobiwardrobe.mobiwardrobe;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.Espresso.pressBack;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.core.IsNot.not;
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class FloatingActionButtonTest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testFloatingActionButtonClick() {
//        // Проверяем, что FloatingActionButton отображается
//        onView(withId(R.id.fab_add)).check(matches(isDisplayed()));
//
//        // Кликаем на FloatingActionButton
//        onView(withId(R.id.fab_add)).perform(click());
//
//        // Проверяем, что меню открыто
//        onView(withId(R.id.fab_add_clothes)).check(matches(isDisplayed()));
//        onView(withId(R.id.fab_add_outfit)).check(matches(isDisplayed()));
//
//        // Кликаем на FloatingActionButton еще раз
//        onView(withId(R.id.fab_add)).perform(click());
//
//        // Проверяем, что меню закрыто
//        onView(withId(R.id.fab_add_clothes)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.fab_add_outfit)).check(matches(not(isDisplayed())));
//
//        // Кликаем на FloatingActionButton
//        onView(withId(R.id.fab_add)).perform(click());
//
//        // Кликаем на FloatingActionButton для добавления одежды
//        onView(withId(R.id.fab_add_clothes)).perform(click());
//
//        // Проверяем, что активность UploadImageActivity открыта
//        onView(withId(R.id.upload_image_activity_layout)).check(matches(isDisplayed()));
//
//        // Нажимаем системную кнопку "Назад"
//        pressBack();
//
//        // Кликаем на FloatingActionButton
//        onView(withId(R.id.fab_add)).perform(click());
//
//        // Кликаем на FloatingActionButton для создания комплекта
//        onView(withId(R.id.fab_add_outfit)).perform(click());
//
//        // Проверяем, что активность CreateOutfitActivity открыта
//        onView(withId(R.id.create_outfit_activity_layout)).check(matches(isDisplayed()));
//    }
//}