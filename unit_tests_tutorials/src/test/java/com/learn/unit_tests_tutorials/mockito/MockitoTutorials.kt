package com.learn.unit_tests_tutorials.mockito

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import kotlin.collections.ArrayList

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

    /**
     * times、never、atMost、atLeast等关于次数的方法，即VerificationMode
     */
    @Test
    fun sampleVerifyNumber() {
        val mockedList = mock(mutableListOf<String?>().javaClass)
        //"once"添加一次
        mockedList.add("once")
        //"twice"添加两次
        mockedList.add("twice")
        mockedList.add("twice")
        //"three times"添加三次
        mockedList.add("three times")
        mockedList.add("three times")
        mockedList.add("three times")

        //times(1)默认被调用，这两次的验证逻辑是相同的，都想验证"once"是否被添加一次。
        verify(mockedList).add("once")
        verify(mockedList, times(1)).add("once")

        //分别验证mockedList是否添加了两次"twice"和三次"three times"
        verify(mockedList, times(2)).add("twice")
        verify(mockedList, times(3)).add("three times")

        verify(mockedList, never()).add("never happened")

        verify(mockedList, atMostOnce()).add("once")
        verify(mockedList, atMost(5)).add("three times")
        verify(mockedList, atLeast(2)).add("three times")
        verify(mockedList, atLeastOnce()).add("three times")
    }

    @Test
    fun sampleDoThrow() {
        val mockedList = mock(mutableListOf<String?>().javaClass)
        //当mockedList调用clear时，抛出RuntimeException
        doThrow(RuntimeException()).`when`(mockedList).clear()
        //模拟实际的调用，抛出异常。
        mockedList.clear()
    }

    /**
     * inOrder方法的使用
     */
    @Test
    fun sampleVerifyInOrder() {
        //创建一个mock对象，它的方法按照特定的顺序执行
        val singleMock = mock(mutableListOf<String?>().javaClass)

        singleMock.add("was added first")
        singleMock.add("was added second")

        //创建一个按照顺序验证的对象
        val inOrder = inOrder(singleMock)

        //verify验证代码逻辑时，singleMock必须按照原来调用的顺序。所以这里如果方法调换位置，则测试会不通过。
        inOrder.verify(singleMock).add("was added first")
        inOrder.verify(singleMock).add("was added second")

        val firstMock = mock(mutableListOf<String?>().javaClass)
        val secondMock = mock(mutableListOf<String?>().javaClass)

        firstMock.add("was added first")
        secondMock.add("was added second")

        val inOrder2 = inOrder(firstMock, secondMock)

        inOrder2.verify(firstMock).add("was added first")
        inOrder2.verify(secondMock).add("was added second")
    }

    /**
     * 验证某些mocked对象未曾发生过交互
     */
    @Test
    fun sampleVerifyZeoInteractions() {
        val firstMock = mock(mutableListOf<String?>().javaClass)
        val secondMock = mock(mutableListOf<String?>().javaClass)
        val thirdMock = mock(mutableListOf<String?>().javaClass)

        firstMock.add("one")

        verify(firstMock).add("one")

        verify(firstMock, never()).add("two")

        //mockito 3.1之后被废弃，使用verifyNoInteractions代替。
        verifyZeroInteractions(secondMock, thirdMock)
        verifyNoInteractions(secondMock, thirdMock)
    }

    /**
     * verifyNoMoreInteractions，确保参数对象任何一个调用行为都被验证，但凡有一个未验证，都会导致测试失败。
     */
    @Test
    fun sampleVerifyNoMoreInteract() {
        val mockedList = mock(mutableListOf<String?>().javaClass)

        mockedList.add("one")
        mockedList.add("two")

        verify(mockedList).add("one")
        //这里少验证了add("two")的行为，所以，verifyNoMoreInteractions验证时会测试失败。
        verifyNoMoreInteractions(mockedList)
    }


    /**
     * 注解@Mock，必须配合MockitoJUnitRunner才能生效，另外kotlin下，@Mock模拟生成对象，需要通过lateinit声明变量
     */
    @RunWith(MockitoJUnitRunner::class)
    class MockAnnotationTest {

        @Mock
        private lateinit var mockedList: ArrayList<String>

        @Test
        fun sample() {
            mockedList.add("one")

            verify(mockedList).add("one")
        }
    }

    /**
     * stubbing，我理解为当mock对象调用某个方法时，会执行另一个操作。
     * 有点类似key-value的感觉，执行某个方法的过程作为key，然后对应会执行thenXXX方法。
     * 而且对于thenXXX方法的返回值类型，范型的类型就是mock对象调用的这个方法的返回类型。
     *
     * 比如
     * ```
     * `when`(mockedList.add("some arg"))
     *   .thenReturn(true)
     * ```
     * 因为add方法的返回类型是Boolean，所以thenReturn的参数类型只能是Boolean
     *
     * 那stubbing的逻辑如何通过源码来实现的呢？
     *
     * 而迭代式的stubbing，即stubbing对于方法不同时机的调用，对应的结果是不一样的。
     * 这里的时机，可能指调用次数，可能指是否连续调用等。可参考本函数的示例。不过这种场景比较少见。
     */
    @Test
    fun sampleIteratorStubbing() {
        val mockedList = mock(mutableListOf<String?>().javaClass)

        `when`(mockedList.add("hello"))
//            .thenThrow(RuntimeException())
            .thenReturn(false)
            .thenReturn(true)

        //也可以通过可变参数来简化
//        `when`(mockedList.add("hello"))
//            .thenReturn(false, true)

        //如果不使用链式调用，那么对于同一个方法或参数的多次stubbing则会使后者stubbing覆盖前者
//        `when`(mockedList.add("hello"))
//            .thenReturn(true)
//        `when`(mockedList.add("hello"))
//            .thenReturn(false)

        //第一次调用,输出false
        println(mockedList.add("hello"))
        //第二次调用，返回true
        println(mockedList.add("hello"))

        //超过stubbing次数的调用，则返回最后一次stubbing的内容。
        //这里输出"true"
        println(mockedList.add("hello"))
    }


    /**
     * 使用Answer的实现类 进行stubbing
     *
     * 一般情况下通过thenReturn和thenThrow就能测试需要测试的代码了。
     */
    @Test
    fun sampleStubbingWithCallback() {
        val mockedList = mock(mutableListOf<String?>().javaClass)

        `when`(mockedList[0]).thenAnswer {
            "called with arguments : ${Arrays.toString(it.arguments)}"
        }

        //打印 "called with arguments: [0]"
        println(mockedList[0])
    }

    /**
     * 对于插桩void方法，需要使用doXXXX。
     * 如果对同一个方法进行多次插桩测试，在测试过程中更改模拟对行为，也可以使用doXXX方法来实现。
     * 一般情况下，测试范式为：
     * doXXXX(...).when(...).someMethod(...)
     * 意思就是当执行someMethod时，使用doXXX的行为来代替someMethod
     *
     * doReturn、doThrow、doAnswer、doNothing、doCallRealMethod
     */
    @Test
    fun sampleDoXXX() {
        val mockedList = mock(mutableListOf<String?>().javaClass)
        doThrow(RuntimeException()).`when`(mockedList).add("hello")

        //抛出异常
        mockedList.add("one")
    }

    /**
     * spy方法，为实际的对象设置别名。然后可以用别名来调用对象的方法。注意这里不是mock出来的对象。
     *
     * 当然，一般是不建议使用spy。除非是针对所谓的"部分模拟"，比如第三方库的接口，遗留代码的临时重构。
     */
    @Test
    fun sampleSpy() {
        //对于真实的对象，调用它的方法时，会按照实际的代码来进行判断。如果有异常则抛出异常。
        //针对这两行已知代码，如果接着执行spy[0]，程序会抛出越界异常，因为实际的对象还没有添加任何值。
        val linkedList = LinkedList<String>()
        val spy = spy(linkedList)

        `when`(spy.size).thenReturn(100)

        //使用spy 调用实际对象的方法
        spy.add("one")
        spy.add("two")
        //输出"one"
        println(spy[0])
        //输出"100"
        println(spy.size)

        verify(spy).add("one")

        //有时，spy和when结合使用可能会出现错误，这时可考虑将when替换为doXXX方法。
        //由于spy对应的对象还没有存储任何值，所以会抛异常。
//        `when`(spy[0]).thenReturn("foo")
        //对于上面的异常，应该改写为：
        doReturn("foo").`when`(spy)[0]


        //mockito并不会将方法的调用委托传递给实际的实例，而是创建实例的副本。
        //所以如果通过spy来使用真实的实例，就要注意真实的实例可能出现的异常，以及实例目前真实的变量、方法逻辑等。
        //另外，通过spy调用未打桩的方法，则不对真实实例产生任何影响。也就是说，（个人理解）从测试角度来看，没有任何意义。
        spy.clear()

        //mockito不会mock final方法，所以如果针对final method使用spy和stubbing，则意味着异常。也不应该通过verify方法来验证这些final方法。
    }


    @RunWith(MockitoJUnitRunner::class)
    class SampleArgumentCaptor {

        @Mock
        private lateinit var utility: Utility

        /**
         * 对于方法的参数，如何在单元测试中验证？
         * mockito提供了ArgumentCaptor,使用上概括来讲分为以下几步：
         * （0）mock一个对象A，它有方法a(arg:T),其中arg表示要进行单元测试的参数，T为参数类型
         * （1）通过ArgumentCaptor.forClass为要测试的参数类型创建一个ArgumentCaptor对象
         * （2）通过verify来调用A对象的a方法，这时传入的参数为ArgumentCaptor的capture方法，
         * （3）进行单元测试的验证，通过ArgumentCaptor.value获取参数的值。
         *
         * 比如这里的示例，为Utility的countNumberCharacter的参数count写单元测试
         */
        @Test
        fun sample() {
            val targetClass = TargetClass(utility)
            targetClass.countNumberCharacter("this is a test", 'i')

            val captor = ArgumentCaptor.forClass(Int::class.java)

            verify(utility, times(1)).logToConsole(captor.capture())

            assertThat(captor.value).isEqualTo(2)
        }


        open class TargetClass(var utility: Utility) {

            /**
             * 计算一个字符串中有多少个指定的Char
             */
            fun countNumberCharacter(text: String, ch: Char) {
                val charArray = text.toCharArray()
                var count = 0

                for (char in charArray) {
                    if (char == ch) {
                        count++
                    }
                }
                utility.logToConsole(count)
            }
        }

        open class Utility {
            open fun logToConsole(count: Int) {
                println("utility count: $count")
            }
        }
    }

    /**
     * 正常测试情况下，不要使用这种方式测试的代码
     *
     * 除非是第三方接口、老旧代码的临时重构这样的情况，考虑使用partial mock。
     */
    @Test
    fun samplePartialMock() {
        //使用spy可以创建partial mock
        val spy = spy(LinkedList::class.java)

        //或者mock第三方库中的类
        val mock = mock(SampleArgumentCaptor.Utility::class.java)
        `when`(mock.logToConsole(1)).thenCallRealMethod()
    }

    /**
     * reset方法重置mock对象。但一般都尽量避免使用reset，因为都是重新创建mock。如果使用reset，一般表明测试方法写的有问题。
     */
    @Test
    fun sampleResetMock() {
        val mock = mock(mutableListOf<Int>().javaClass)
        `when`(mock.size).thenReturn(10)

        mock.add(1)

        reset(mock)
    }

    /**
     * 行为驱动开发(BDD)：http://en.wikipedia.org/wiki/Behavior_Driven_Development
     * BDD下的测试一般是三步given、when、then：
     * （1）given: BDDMockito.given方法，获得mock的对象
     * （2）when:
     * （3）then:
     *
     * 暂时不理解BDD，以及如何对BDD进行自动化测试。
     */
    @RunWith(MockitoJUnitRunner::class)
    class BDDTest

    /**
     * mock是可以序列化的，通过MockSettings.serializable(),但这个特性在单元测试中不常用
     *
     * 它用来处理BDD规范的特定用例，而BDD规范可能具有不可靠的外部依赖关系，主要是web环境中。
     *
     * MockSettings接口中定义了一些可以在mock创建对象时生效的设置。
     */
    @Test
    fun sampleSerializableMock() {
        //一般情况下但serializable mock
        val serializableMock = mock(List::class.java, withSettings().serializable())

        //对spy对象进行 serializable
        val arrayList = ArrayList<Objects>()
        val spy = mock(
            ArrayList::class.java, withSettings()
                .spiedInstance(arrayList)
                .defaultAnswer(CALLS_REAL_METHODS)
                .serializable()
        )
    }

    /**
     * timeout方法用来验证方法调用是否超时。
     */
    @Test
    fun sampleTimeout() {
        val mock = mock(mutableListOf<String>().javaClass)

        //只要mock对象的某个方法调用两次的耗时在100ms以内，即测试通过
        mock.add("hello")
        mock.add("hello")
        verify(mock, timeout(100).times(2)).add("hello")
        verify(mock, timeout(100).atLeast(2)).add("hello")

//        verify(mock, timeout(100)).add("hello")
//        verify(mock, timeout(100).times(1)).add("hello")
    }
}