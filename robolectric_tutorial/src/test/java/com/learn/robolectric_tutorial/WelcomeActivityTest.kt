package com.learn.robolectric_tutorial

import android.content.Intent
import android.os.Build
import android.widget.Button
import org.junit.Assert.assertEquals
import org.junit.Before
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
 * 这里就是纯使用Robolectric的api，不过更推荐使用AndroidX Test APIs
 *
 * 比如RuntimeEnvironment.application 用ApplicationProvider来替代等；
 *
 * Robolectric中关于View的api非常少，仅限于使用findViewById。一般情况下，对于instrumentation tests还是建议使用Espresso。
 * Robolectric 在4.0之后，支持Espresso API。
 *
 * 注解@Config中可以为Robolectric指定测试的sdk，还可以指定类、方法、资源目录等，可以参考@Config的源码。
 *
 * 如果想将配置想集中，可以在module中合适的目录下（通常是src/test/resources）创建robolectric.properties文件。
 *
 * 如果需要改变默认的配置，可以创建RobolectricTestRunner子类，充血buildGlobalConfig方法，然后在@Runwith中指定这个子类。
 *
 *
 *
 */
@Config(sdk = [Build.VERSION_CODES.P], shadows = [])
@RunWith(RobolectricTestRunner::class)
class WelcomeActivityTest {

    @Before
    fun setup() {
    }

    @Test
    fun clickingLogin_shouldStartLoginActivity() {
        val activity = Robolectric.setupActivity(WelcomeActivity::class.java)
        activity.findViewById<Button>(R.id.login).performClick()

        val expectedIntent = Intent(activity, LoginActivity::class.java)
        val actualIntent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }
}