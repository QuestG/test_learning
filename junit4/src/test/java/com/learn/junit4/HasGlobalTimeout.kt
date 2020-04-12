package com.learn.junit4

import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author quest
 * @date 2020/4/12
 * Description:设置测试超时条件有两种方式：
 * 1、@Test(timeout=...) 针对指定的测试方法，且该方法在单独的线程中执行。如果超时，则测试失败，JUnit会中断该线程。
 * 2、通过@Rule和Timeout，针对所有测试方法
 *
 * 超时规则中设置的超时时间，适用于整个测试过程，包括@Before或@After方法。
 * 不过如果测试方法出于无限循环中，@After方法不会被执行。
 *
 * 另外，可以使用Github上的JUnit Foundation来管理全局的超时设置。
 */
class HasGlobalTimeout {

    private val latch = CountDownLatch(1)

    @Rule
    @JvmField
    val globalTimeout: Timeout = Timeout.seconds(10)

    @Test
    fun testSleepForTooLong() {
        TimeUnit.SECONDS.sleep(15)
    }

    @Test
    fun testBlockForever() {
        latch.await()
    }
}