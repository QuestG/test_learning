package com.learn.junit4

import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.TypeSafeMatcher
import org.junit.Test
import java.math.BigDecimal
import kotlin.math.sqrt

/**
 * @author quest
 * @date 2020/4/13
 * Description:Hamcrest Tutorial示例
 *
 * Hamcrest可以和不同的单元测试框架集成使用，除了常见的JUnit，还有TestNG。
 * Hamcrest还可以与Mock框架一起使用，即通过适配器将mock对象的matcher过渡到Hamcrest的matcher。
 *
 * Hamcrest中比较重要的Matcher，如果按照包来划分的话，有如下几个：
 * 1、org.hamcrest.core
 * (1)CoreMatchers.anything(),对应类IsAnything
 * (2)CoreMatchers.describedAs(),对应类DescribedAs
 * (3)CoreMatchers.is(),对应类Is
 * (4)CoreMatchers.allOf(),对应类AllOf
 * (5)CoreMatchers.anyOf(),对应类AnyOf
 * (6)CoreMatchers.not(),对应类IsNot
 * (7)CoreMatchers.equalTo(),对应类IsEqual
 * (8)CoreMatchers.instanceOf(),对应类IsInstanceOf
 * (9)CoreMatchers.notNullValue()/nullValue(),对应类IsNull
 * (10)CoreMatchers.sameInstance(),对应类IsSame
 * (11)CoreMatchers.hasItem()/hasItems,对应类IsIterableContaining
 * (12)CoreMatchers.containsString(),对应类StringContains
 * (13)CoreMatchers.endsWith(),对应类StringEndsWith
 * (14)CoreMatchers.startsWith(),对应类StringStartsWith
 *
 * 2、org.hamcrest.object
 * (1)HasToString
 *
 * 3、org.hamcrest.beans
 * (1)HasProperty.hasProperty
 *
 * 4、org.hamcrest.collection
 * (1)IsArray.array
 * (2)IsMapContaining.hasEntry/hasKey/hasValue
 * (3)ArrayMatching.hasItemInArray
 *
 * 5、org.hamcrest.number
 * (1)IsCloseTo.closeTo
 * (2)OrderingComparison.greaterThan/greaterThanOrEqualTo/lessThan/lessThanOrEqualTo
 *
 * 6、org.hamcrest.text
 * (1)IsEqualIgnoringCase.equalToIgnoringCase
 * (2)IsEqualCompressingWhiteSpace.equalToIgnoringWhiteSpace
 *
 * 7、还有集成TypeSafeMatcher的自定义Matcher
 *
 * 其余的Matcher，可自行发现。
 */

class Biscuit(private val name: String) {
    override fun equals(other: Any?): Boolean {
        val otherName = (other as? Biscuit)?.name
        return name == otherName
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class BiscuitTest {
    @Test
    fun testEquals() {
        val biscuit1 = Biscuit("Ginger")
        val biscuit2 = Biscuit("Ginger")
        assertThat(biscuit1, equalTo(biscuit2))
    }
}

/**
 * 一些重要的matcher介绍
 */
class MatchersTests {

    /**
     * 测试总是通过。如果不关心测试的对象是什么，则可以使用。
     */
    @Test
    fun testAnything() {
        anything()
        anything("desc")
    }

    @Test
    fun testDescribedAs() {
        val myBigDecimal = BigDecimal.ZERO
        describedAs(
            "a big decimal equal to %0",
            equalTo(myBigDecimal),
            myBigDecimal.toPlainString()
        )
    }

    /**
     * is是equalTo的简化，当然它的参数也可以是一个Matcher
     */
    @Test
    fun testIs() {
        assertThat("hello", `is`("hello"))
    }

    /**
     * 如果被测试的对象匹配了allOf指定的所有Matcher，则测试通过。
     */
    @Test
    fun testAllOf() {
        assertThat("myValue", allOf(startsWith("my"), containsString("Val")))
    }

    /**
     * 如果被测试的对象匹配了anyOf指定的任意一个Matcher，则测试通过。
     */
    @Test
    fun testAnyOf() {
        assertThat("myValue", anyOf(startsWith("my"), containsString("t")))
    }

    /**
     * not是not(equalTo(x))的简化，not的参数也可以是一个Matcher
     */
    @Test
    fun testNot() {
        assertThat("hello", not("a"))
        assertThat("hello", not(startsWith("a")))
    }

    @Test
    fun testObjectMatcher() {
        assertThat("hello", equalTo("hello"))
    }

    /**
     * 使用自定义的Matcher
     */
    @Test
    fun testSquareRootOfMinusOneIsNotANumber() {
        assertThat(sqrt(Double.NaN), `is`(IsNotANumber.notANumber()))
    }
}

/**
 * 自定义一个Matcher，判断值不是数字
 */
class IsNotANumber : TypeSafeMatcher<Double>() {
    override fun describeTo(description: Description?) {
        description?.appendText("not a number")
    }

    override fun matchesSafely(item: Double?): Boolean {
        return item?.isNaN()!!
    }

    companion object {
        fun notANumber(): IsNotANumber {
            return IsNotANumber()
        }
    }
}