package com.learn.robolectric_tutorial

import android.content.Intent
import android.os.Build
import android.widget.Button
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * @author quest
 * @date 2020/5/12
 * Description: RobolectricTestRunner只能用于单元测试，即只能在test/java目录下才能被引用
 *
 * 注解@Config中可以为Robolectric指定测试的sdk
 */
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class WelcomeActivityTest {
    @Test
    fun clickingLogin_shouldStartLoginActivity() {
        val activity = Robolectric.setupActivity(WelcomeActivity::class.java)
        activity.findViewById<Button>(R.id.login).performClick()

        val expectedIntent = Intent(activity, LoginActivity::class.java)
        val actualIntent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }
}