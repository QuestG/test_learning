package com.learn.junit4

import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * @author quest
 * @date 2020/4/12
 * Description: 针对异常情况的几种测试方法
 * 1、使用assertThrows,在JUnit 4.13版本才被加入Assert类中。
 * 2、try/catch  它对应的情况是使用的JUnit版本不是4.13或者项目不支持lambda
 * 3、在@Test注解上设置expected参数
 * 4、使用ExpectedException rule，不过这种方式已在JUnit 4.13被废弃，可以使用assertThrows代替。
 * 它不仅可以指明预期的异常，也可以指明预期的异常信息。
 * 这里补充一下，在Kotlin语言中使用@Rule，需要额外添加@JvmField来解决"The @Rule must be public"的问题。
 */
class ExceptionTests {

    @Test
    fun testExceptionAndState() {
        val list = mutableListOf<Int>()

        //assertThrow可以对一个指定的方法进行指定的异常（这里是IndexOutOfBoundsException）判断，
        //如果发生指定类型的异常，则返回具体的异常对象。
        val thrown = assertThrows(
            IndexOutOfBoundsException::class.java
        ) { list.add(1, 2) }
        //assert异常的信息
        assertEquals("Index: 1, Size: 0", thrown.message)
        //对异常抛出后的对象状态进行判断。
        assertTrue(list.isEmpty())
    }

    @Test
    fun testExceptionMessage() {
        val list = listOf<Int>()
        try {
            list[0]
            //查看源码，实际是抛出了AssertError
            fail("Expected an IndexOutOfBoundsException to be thrown")
        } catch (e: IndexOutOfBoundsException) {
        }
    }

    /**
     * expected表示此测试方法抛出期望的异常，该测试方法中的代码就被认为测试通过
     * 所以，在使用时还是要谨慎，而且这种方式只能知道抛出了异常，对于异常信息以及异常发生后对象的状态都是不可知的。
     * 一般建议还是使用assertThrows。
     */
    @Test(expected = IndexOutOfBoundsException::class)
    fun empty() {
        listOf<Int>()[0]
    }

    @Rule
    @JvmField
    val thrown = ExpectedException.none()

    @Test
    fun shouldTestExceptionMessage() {
        val list = listOf<Int>()
        thrown.expect(IndexOutOfBoundsException::class.java)
//        thrown.expectMessage("Index: 1, Size: 0")
        thrown.expectMessage(containsString("index 0"))
        list[0]
    }
}