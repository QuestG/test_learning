package com.learn.robolectric_tutorial

import android.content.Intent
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows

/**
 * @author quest
 * @date 2020/5/12
 * Description: truth只能在单元测试引用，无法在instrument test中使用
 *
 * 对于Fragment，androidx提供了FragmentScenario API来进行测试。
 */
@RunWith(AndroidJUnit4::class)
class WelcomeActivityAndroidTest {

    @Test
    fun clickingLogin_shouldStartLoginActivity() {
        ActivityScenario.launch(WelcomeActivity::class.java).onActivity {
            it.findViewById<Button>(R.id.login).performClick()

            val expectedIntent = Intent(it, LoginActivity::class.java)
            val actualIntent = Shadows.shadowOf(it.application).nextStartedActivity
            assertEquals(expectedIntent.component, actualIntent.component)
        }
    }
}