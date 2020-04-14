package com.learn.unit_tests_tutorials

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * @author quest
 * @date 2020/4/14
 * Description: 学习Truth的用法，优势主要提示更友好，更符合人类阅读习惯。
 *
 * 从使用方式上，JUnit有点面向过程的感觉，而Truth有点面向对象的感觉。
 *
 * Truth的使用范式都是根据assertThat方法接收参数的类型获得Subject的子类对象，然后使用子类对象的方法接着编写测试的逻辑。
 *
 * 比如
 * ```
 * assertThat("guiuso@huami.com").contains("guisuo")
 * ```
 * 就是先得到StringSubject对象，然后再调用其中的实例方法。其他类型的参数，调用方式类似。
 *
 * Truth.java类中提供了很多静态方法，具体使用可以查看源码。
 *
 * TODO 2020/4/18 对于Floating Point和Fuzzy Truth的学习，后面会补充。
 */

class TruthSamples {

    @Test
    fun h() {
        assertThat("guiuso@huami.com").contains("guisuo")
    }
}