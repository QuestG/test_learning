package com.learn.unit_tests_tutorials.mockito

import org.junit.Test
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.util.*

/**
 * @author quest
 * @date 2020/4/15
 * Description: Mockito的教程
 */

class Tutorials {

    /**
     * 模拟一个List。
     * 实际测试时，使用真实的需要测试的类的实例。
     *
     * 一旦创建一个模拟对象后，mock会记住所有的调用，而测试时只需要验证需要关注的调用逻辑即可。
     */
    @Test
    fun sampleVerifyBehavior() {
        //1、创建模拟对象
        val mockedList = mock(mutableListOf<Int>().javaClass)
        //2、使用模拟对象
        mockedList.add(1)
        mockedList.clear()
        //3、验证。
        verify(mockedList).add(1)
//        verify(mockedList).add(2) //如果验证此逻辑，则测试会失败。因为mockedList没有过这样的逻辑。
        verify(mockedList).clear()
    }

    /**
     * 默认情况下，对于所有有返回值的方法，mock将根据返回值的类型返回合适的值，比如null、基础类型值、基础类型包装类值或者空集合。
     * 比如，对于返回值类型为int/Integer，mock调用时返回0；对于boolean/Boolean，mock调用时返回false。
     *
     * stubbing可以被覆盖。不过过多的stubbing可能造成代码问题。
     *
     * 一旦stubbed，mock调用的方法总是返回stubbed值。
     *
     * 总的来说，不是太理解，而且看文档说明的意思一般也是不建议使用。
     */
    @Test
    fun sampleVerifyStubbing() {
        val linkedList = LinkedList<String>()
        val mockLinkedList = mock(linkedList.javaClass)

        //这里模拟的动作就是所谓的stubbing，暂时不知该如何翻译。
        `when`(mockLinkedList[0]).thenReturn("first")
        `when`(mockLinkedList[1]).thenThrow(RuntimeException())

        //输出"first"
        println(mockLinkedList[0])
        //抛出runtime exception
//        println(mockLinkedList[1])
        //输出"null"
        println(mockLinkedList[99])

        //一般情况下，验证stubbed调用是多余的。如果代码逻辑确实关心mockLinkedList[0]的返回值，
        //如果不关心mockLinkedList[0]返回的内容，则就不应该stub
        verify(mockLinkedList[0])
    }

    /**
     * mockito一般使用equals来验证参数值。如果需要额外的灵活性 ，可以使用ArgumentMatchers，即配合使用equals和anyX。
     *
     * 使用argument matcher时需要注意，如果mock调用模拟对象的方法传入的参数有一个是ArgumentMatcher，其他参数也必须用ArgumentMatcher包装。
     *
     * ArgumentMatchers提供的方法，比如anyObject、eq等，返回类型并不是ArgumentMatcher，
     * 从源码来看是用Stack将ArgumentMatcher对象存储起来，然后返回一个null。这么实现的原因是受制于Java编译器的静态类型安全检查.
     * 所以ArgumentMatchers中的静态方法只能在stubbed或者verified方法中使用。
     *
     * 关于stubbing和verify还不是很能理解
     */
    @Test
    fun sampleArgumentMatchers() {
        val mockedList = mock(mutableListOf<String?>().javaClass)

//        //两次stubbing
        `when`(mockedList[anyInt()]).thenReturn("element")
        `when`(mockedList.contains(argThat(isValid()))).thenReturn(true)

        //输出"element"
        println(mockedList[99])
        //也可以使用ArgumentMatchers来验证
        verify(mockedList)[ArgumentMatchers.anyInt()]
        //此代码会抛异常
//        verify(mockedList).add(argThat { it?.length!! > 5 })

        val addAll = mockedList.add(argThat(isValid()))
        `when`(addAll).thenReturn(true)

        mockedList.add("fifth")
        verify(mockedList).add(argThat(isValid()))
    }

    /**
     * 自定义实现的ArgumentMatcher
     */
    private fun isValid(): ArgumentMatcher<String>? {
        return ArgumentMatcher {
            it.length == 5
        }
    }
}