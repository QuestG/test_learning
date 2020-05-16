package com.learn.robolectric_tutorial

import android.content.Intent
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import org.junit.Assert.assertEquals
import org.junit.Test
import org.robolectric.Shadows

/**
 * @author quest
 * @date 2020/5/12
 * Description: trueth只能在单元测试引用，无法在instrument test中使用
 */
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