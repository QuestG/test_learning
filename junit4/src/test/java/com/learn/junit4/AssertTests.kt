package com.learn.junit4

import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.CombinableMatcher
import org.junit.Assert.*
import org.junit.Test

/**
 * @author quest
 * @date 2020/4/11
 * Description: 学习JUnit中的断言
 *
 * JUnit为所有基本类型、对象和数组提供了断言方法（assertXXX），且具有多个重载方法。
 * 断言方法的参数顺序是（1）可选参数：失败信息（2）期望值（3）实际值。
 * 不过，方法assertThat的参数顺序为（1）可选参数：失败信息（2）实际值（3）一个Matcher对象。
 * 也就是说，assertThat方法对于实际值，会通过Matcher对象设置的判断条件来测试实际值是否满足要求。
 */
class AssertTests {

    /**
     * JUnit4不再要求以test开头命名测试方法，但一般还是根据习惯以test作为测试方法的开头。
     */
    @Test
    fun testAssertArrayEquals() {
        val expected = "trial".toByteArray()
        val actual = "trial".toByteArray()
        /**判断数组是否相等:assertArrayEquals, 它有多个重载方法。*/
        assertArrayEquals("failure: byte array not same", expected, actual)
    }

    @Test
    fun testAssertEquals() {
        assertEquals("failure: strings are not equal.", "text", "txt")
    }

    @Test
    fun testAssertFalse() {
        assertFalse("failure: should be false", false)
    }

    @Test
    fun testAssertTrue() {
        assertTrue("failure: should be true", true)
    }

    @Test
    fun testAssertNotNull() {
        assertNotNull("should not be null", Any())
    }

    @Test
    fun testAssertNotSame() {
        val num = 200
        assertNotSame("should not be same object", num, num)
    }

    @Test
    fun testAssertNull() {
        assertNull("should be null", null)
    }

    @Test
    fun testAssertSame() {
        val hello = "hello world"
        /**这里判断的是同一个对象*/
        assertSame("should be same", hello, hello)
    }

    @Test
    fun testAssertThatBothContainsString() {
        /**检测实际值包含字符串"a"和"b"*/
        assertThat("albumen", both(containsString("a")).and(containsString("b")))
    }

    @Test
    fun testAssertThatHasItems() {
        /**Hamcrest的CoreMatcher中包含多种Matcher，可以满足不同条件*/
        assertThat(listOf("one", "two", "three"), hasItems("one", "three"))
    }

    @Test
    fun testAssertThatEveryItemContainsString() {
        assertThat(listOf("fun", "ban", "net"), everyItem(containsString("n")))
    }

    @Test
    fun testAssertThatHamcrestCoreMatchers() {
        assertThat("good", allOf(equalTo("good"), startsWith("good")))
        assertThat("good", not(allOf(equalTo("bad"), equalTo("good"))))
        assertThat("good", anyOf(equalTo("bad"), equalTo("good")))
        assertThat(7, not(CombinableMatcher.either(equalTo(3)).or(equalTo(4))))
        assertThat(Any(), not(sameInstance(Any())))
    }
}