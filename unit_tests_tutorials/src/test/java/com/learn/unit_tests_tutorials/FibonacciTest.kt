package com.learn.unit_tests_tutorials

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * @author quest
 * @date 2020/4/12
 * Description:斐波那契数列
 */
@RunWith(Parameterized::class)
class FibonacciTest constructor(
    private val fInput: Int, private val fExpected: Int
) {


    @Test
    fun test() {
        println("fExpected: $fExpected fInput $fInput")
        assertEquals(fExpected, compute(fInput))
    }

    companion object {
        @Parameterized.Parameters(name = "{index}: fib({0} = {1})")
        @JvmStatic
        fun data(): Array<Array<Int>> {
            return arrayOf(
                arrayOf(0, 0),
                arrayOf(1, 1),
                arrayOf(2, 1),
                arrayOf(3, 2),
                arrayOf(4, 3),
                arrayOf(5, 5),
                arrayOf(6, 8)
            )
        }

        fun compute(n: Int): Int {
            return if (n <= 1) {
                n
            } else {
                compute(n - 1) + compute(n - 2)
            }
        }
    }
}