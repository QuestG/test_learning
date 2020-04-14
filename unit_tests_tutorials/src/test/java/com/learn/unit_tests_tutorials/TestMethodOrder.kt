package com.learn.unit_tests_tutorials

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * @author quest
 * @date 2020/4/12
 * Description:设置测试执行的顺序
 *
 * 从JUnit 4.11开始，JUnit默认使用MethodSorters.default作为测试方法的执行顺序。
 * 如果想主动改变执行顺序的话，可以使用@FixMethodOrder注解，来配置MethodSorters的类型。
 *
 * MethodSorters.NAME_ASCENDING 按照字母升序方式来执行测试方法。
 *
 * MethodSorters.JVM 测试方法按照JVM返回的顺序执行，此顺序可能因JVM而异。
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestMethodOrder {

    @Test
    fun testA() {
        println("first")
    }

    @Test
    fun testB() {
        println("second")
    }

    @Test
    fun testC() {
        println("third")
    }
}