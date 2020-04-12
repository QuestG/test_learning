package com.learn.junit4

import org.junit.*
import java.io.Closeable

/**
 * @author quest
 * @date 2020/4/12
 * Description: 主要介绍在测试之前或测试前后准备的测试环境，通过@BeforeClass、@AfterClass、@Before、@After等注解来实现。
 * 关于这几个注解的进一步使用，可参考链接：https://garygregory.wordpress.com/2011/09/25/understaning-junit-method-order-execution/
 */
class TestFixturesExample {

    class ExpensiveManagedResource : Closeable {
        override fun close() {
        }
    }

    class ManagedResource : Closeable {
        override fun close() {
        }
    }

    private var managedResource: ManagedResource? = null

    @Before
    fun setup() {
        println("@Before setup")
        managedResource = ManagedResource()
    }

    @After
    fun teardown() {
        println("@After teardown")
        managedResource?.close()
        managedResource = null
    }

    @Test
    fun test1() {
        println("@Test test1()")
    }

    @Test
    fun test2() {
        println("@Test test2()")
    }

    companion object {
        @JvmStatic
        private var expensiveManagedResource: ExpensiveManagedResource? = null

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            println("@BeforeClass setupClass")
            expensiveManagedResource = ExpensiveManagedResource()
        }

        @AfterClass
        @JvmStatic
        fun teardownClass() {
            println("@AfterClass teardownClass")
            expensiveManagedResource?.close()
            expensiveManagedResource = null
        }
    }
}