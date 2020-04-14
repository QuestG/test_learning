package com.learn.unit_tests_tutorials

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assume.assumeThat
import org.junit.experimental.theories.DataPoint
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith

/**
 * @author quest
 * @date 2020/4/13
 * Description:学习@Theory和Theories
 *
 * Theory是指通过assert和assume来产生一种新的意图陈述（我理解为测试逻辑）。
 * 一般情况下，通过@Test测试一种特定情况下的预期行为，而@Theory目标是在很多情况下预期行为的某些方面。
 */

@RunWith(Theories::class)
class UserTest {

    /**
     * 这里就是所谓的通过@Theory、assert和assume产生新的意图陈述。
     *
     * 意图：只有用户名不包含斜杠的情况下，用户名才应包括在配置文件中。
     *
     * 执行逻辑：UserTest会对此类中每个@DatePoint注解的字段运行filenameIncludesUsername方法。
     *
     * 结果：对于DataPoint注解的值，如果assume失败，则静默忽略该data point。如果所有的assume均通过，但assert失败，整个测试失败。
     */
    @Theory
    fun filenameIncludesUsername(username: String) {
        assumeThat(username, not(containsString("/")))
        assertThat(AssertTests.User(username).configFileName(), containsString(username))
    }

    companion object {
        @DataPoint
        const val GOOD_USERNAME = "optimus"

        @DataPoint
        const val USERNAME_WITH_SLASH = "optimus/prime"
    }
}