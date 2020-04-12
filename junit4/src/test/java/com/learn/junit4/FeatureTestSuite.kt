package com.learn.junit4

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author quest
 * @date 2020/4/12
 * Description:使用Suite，手动构建一个包含多个测试类的suite。这样可以一次执行多个测试类，且按照类声明的顺序。
 * 使用方式为@RunWith(Suite.class)和@SuiteClasses(TestClass1.class,...)
 */

@RunWith(Suite::class)
@Suite.SuiteClasses(AssertTests::class)
class FeatureTestSuite {
    /**这个类只作为占位，来承载上面两个注解，无其他作用*/
}