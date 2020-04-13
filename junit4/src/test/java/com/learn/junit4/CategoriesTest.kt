package com.learn.junit4

import org.junit.Assert.fail
import org.junit.Test
import org.junit.experimental.categories.Categories
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author quest
 * @date 2020/4/13
 * Description:学习@Category、@IncludeCategory和@ExcludeCategory注解，以及Categories类。
 *
 * 对于一组测试类，可以使用@Category、@IncludeCategory来让Categories只执行注解标记的类或方法。
 * 可以使用@ExcludeCategory让Categories不执行注解标记的类或方法。
 * 而且，无论是class还是interface都可以被用作注解的值。
 *
 * Categories用于在测试中添加metadata，在很多测试场景中都会用到：
 * 1、自动化测试：单元测试、集成测试、冒烟测试、回归测试、性能测试等；
 * 2、慢速测试、快速测试
 * 3、CI构建中的NightlyBuildTest
 * 4、UnstableTests, InProgressTests
 */

interface FastTests
interface SlowTests

class A {
    @Test
    fun a() {
        println("test method a")
        fail()
    }

    @Category(SlowTests::class)
    @Test
    fun b() {
        println("test method b")
    }
}

@Category(SlowTests::class, FastTests::class)
class B {
    @Test
    fun c() {
        println("test method c")
    }
}

@RunWith(Categories::class)
@Categories.IncludeCategory(SlowTests::class)
@Suite.SuiteClasses(A::class, B::class)
/**
 * Categories类也是Suite的子类
 *
 * 这里测试类会执行方法A.b和B.c 但不会执行A.a
 */
class SlowTestSuite

@RunWith(Categories::class)
@Categories.IncludeCategory(SlowTests::class)
@Categories.ExcludeCategory(FastTests::class)
@Suite.SuiteClasses(A::class, B::class)
/**
 * 这里测试类会执行方法A.b 但不会执行A.a和B.c
 */
class SlowTestSuite2