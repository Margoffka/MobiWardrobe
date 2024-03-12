//package com.mobiwardrobe.mobiwardrobe;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.CoreMatchers.not;
//
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.test.espresso.contrib.RecyclerViewActions;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.mobiwardrobe.mobiwardrobe.clothes.ClothesFragment;
//import com.mobiwardrobe.mobiwardrobe.clothes.DetailsClothesActivity;
//import com.mobiwardrobe.mobiwardrobe.model.Upload;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(AndroidJUnit4.class)
//public class ClothesFragmentUITest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testRecyclerViewAndProgressBarVisibility() {
//        // Проверка отображения RecyclerView
//        onView(withId(R.id.rv_clothes)).check(matches(isDisplayed()));
//
//        // Проверка отсутствия ProgressBar
//        onView(withId(R.id.pb_clothes)).check(matches(not(isDisplayed())));
//
//        // Дополнительные проверки на отображение других элементов интерфейса, если необходимо
//        // onView(withId(R.id.some_other_view)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testItemClickOpensDetailsActivity() {
//        // Подготавливаем тестовые данные
//        List<Upload> uploads = new ArrayList<>();
//        Upload upload1 = new Upload();
//        // Заполнение полей объекта upload1
//        uploads.add(upload1);
//        // Добавление тестовых данных в базу данных или другое хранилище
//
//        // Запускаем фрагмент
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            FragmentManager fragmentManager = activity.getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, new ClothesFragment());
//            fragmentTransaction.commit();
//        });
//
//        // Кликаем на элемент RecyclerView
//        onView(withId(R.id.rv_clothes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//        // Проверяем, что открылась активность DetailsClothesActivity
//        intended(hasComponent(DetailsClothesActivity.class.getName()));
//    }
//}
//
//
//
//
//
//
