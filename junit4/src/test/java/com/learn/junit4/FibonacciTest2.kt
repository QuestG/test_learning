package com.learn.junit4

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * @author quest
 * @date 2020/4/12
 * Description: 对于参数进行测试，需要看一下Parameterized的源码。
 * Parameterized是Suite的子类，它的作用是可以一次测试多个参数。
 */
@RunWith(Parameterized::class)
class FibonacciTest2 {

    /**
     * 这里表示为默认第一个参数。而@Parameterized.Parameter(value),value表示为第几个参数。
     * 对于kotlin中使用JUnit，如果涉及到参数要求为public，虽然kotlin中可见性默认为public，但还是需要另外添加@JvmField，否则会报错。
     */
    @Parameterized.Parameter
    @JvmField
    var fInput: Int = 0


    @Parameterized.Parameter(1)
    @JvmField
    var fExpected: Int = 0

    @Test
    fun test() {
        println("fExpected: $fExpected fInput $fInput")
        Assert.assertEquals(fExpected, compute(fInput))
    }

    companion object {
        @Parameterized.Parameters
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

        /**
         * 如果只需要测试一个参数，可以不用Array包裹Array，可以使用Iterable或者Array对象。
         * 不过这种方式JUnit 4.12才支持
         */
        @Parameterized.Parameters
        fun data2(): Iterable<Int> {
            return listOf(1, 2)
        }

        @Parameterized.Parameters
        fun data3(): Array<Int> {
            return arrayOf(1, 2)
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