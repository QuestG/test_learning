package com.learn.junit4

import org.junit.Assert.*
import org.junit.AssumptionViolatedException
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.results.PrintableResult.testResult
import org.junit.experimental.results.ResultMatchers.isSuccessful
import org.junit.rules.*
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.model.Statement
import java.io.File
import java.util.logging.Logger

/**
 * @author quest
 * @date 2020/4/12
 * Description: @Rule注解的各种使用方式
 * 它可以灵活添加或重新定义测试方法的行为，可以重用或扩展JUnit提供的规则，或者实现自己的规则。
 */
class DigitalAssetManagerTest {
    /**
     * TemporaryFolder的作用是创建一个临时文件夹，使用系统默认的临时文件目录创建临时资源。
     * 当测试方法结束时，临时文件夹会被删除。默认情况下，如果无法删除资源，则不会引发异常。
     * 从JUnit 4.13版本开始，TemporaryFolder允许对已删除的资源进行验证，如果无法删除资源，则通过AssertionError抛出测试失败。
     * 这个功能只在使用TemporaryFolder.builder()创建时才可用。即TemporaryFolder.builder().assureDeletion().build();
     * 默认情况下，禁用严格验证，以保持向后兼容性。
     */
    @Rule
    @JvmField
    val tempFolder = TemporaryFolder()

    @Rule
    @JvmField
    val exception = ExpectedException.none()

    @Test
    fun countsAssets() {
        val iconFile = tempFolder.newFile("icon.png")
        val assetsFile = tempFolder.newFolder("assets")
        createAssets(assetsFile, 3)
        val digitalAssetManager = DigitalAssetManager(iconFile, assetsFile)
        assertEquals(192, digitalAssetManager.getAssetsCount())
    }

    private fun createAssets(assets: File, numberOfAssets: Int) {
        println("file: ${assets.absolutePath}")
        for (index in 0..numberOfAssets) {
            val file = File(assets, String.format("asset-%d.mpg", index))
            assertTrue("Asset couldn't be created.", file.createNewFile())
        }
    }

    @Test
    fun throwsIllegalArgumentExceptionIfIconIsNull() {
        exception.expect(IllegalArgumentException::class.java)
        exception.expectMessage("Icon is null, not a file, or doesn't exist.")
    }
}

class DigitalAssetManager(private val icon: File, private val assets: File) {

    fun getAssetsCount() =
        if (assets.isDirectory) {
            assets.length()
        } else {
            0
        }
}

/**
 * ExternalResource规则的使用，它是各种规则类的基类，
 * 在测试之前设置外部资源（比如file、socket、server、database连接等），并保证测试完成后将外部资源删除。
 *
 */
class UsesExternalResource {

    val server = Server()

    @Rule
    @JvmField
    val resource = object : ExternalResource() {
        override fun before() {
            super.before()
            server.connect()
        }

        override fun after() {
            super.after()
            server.disconnect()
        }
    }

    @Test
    fun testFoo() {
        Client().run(server)
    }

    class Client {
        fun run(server: Server) {
            server.run()
            println("run server")
        }
    }

    class Server {
        fun connect() {
            println("server connected")
        }

        fun run() {
            println("server run")
        }

        fun disconnect() {
            println("server disconnected")
        }
    }
}

/**
 * ErrorCollector允许测试在遇到第一个错误之后可以继续执行，比如收集数据库表中所有的不正确行，然后一次性报告。
 */
class UsersErrorCollectorTwice {
    @Rule
    @JvmField
    val collector = ErrorCollector()

    @Test
    fun example() {
        collector.addError(Throwable("first thing went wrong"))
        collector.addError(Throwable("second thing went wrong"))
    }
}

/**
 * Verifier也是类似ErrorCollector的规则的基类
 */
class UsesVerifier {
    private var sequence: String = ""

    @Rule
    @JvmField
    val collector = object : Verifier() {
        override fun verify() {
            sequence += "verify "
        }
    }

    @Test
    fun example() {
        sequence += "test "
    }

    @Test
    fun verifierRunsAfterTest() {
        sequence = ""
        assertThat(testResult(UsesVerifier::class.java), isSuccessful())
        assertEquals("test verify", sequence)
    }
}

/**
 * 从4.9版本开始，TestWatcher代替被废弃的TestWatchman。
 * TestWatcher实现了TestRule，而TestWatchman实现了MethodRule。
 *
 * 不过它们都是测试规则的基类，用来记录测试的步骤，但无法修改。
 */
class WatchmanTest {
    private var watchedLog: String = ""

    @Rule
    @JvmField
    val watchman = object : TestWatcher() {

        override fun succeeded(description: Description?) {
            watchedLog += description?.displayName + " success!\n"
        }

        override fun failed(e: Throwable?, description: Description?) {
            watchedLog += description?.displayName + " " + e?.javaClass?.simpleName + "\n"
        }

        override fun skipped(e: AssumptionViolatedException?, description: Description?) {
            watchedLog += description?.displayName + " " + e?.javaClass?.simpleName + "\n"
        }

    }

    @Test
    fun fails() {
        fail()
    }

    @Test
    fun succeeds() {

    }
}

/**
 * TestName的作用就是让当前测试名称在测试方法中可获取。
 *
 * 而Timeout和ExpectedException 可以参考HasGlobalTimeout类和ExceptionTests这两个示例。
 */
class NameRuleTest {
    @Rule
    @JvmField
    val name = TestName()

    @Test
    fun testA() {
        assertEquals("testA", name.methodName)
    }
}

/**
 * 这个类是一个测试套件，它在SuiteClasses包含的所有测试类执行之前，执行server.connect的操作，
 * 然后在所有测试执行之后，完成server.disconnect()的操作。
 *
 * 注解ClassRule扩展了方法级规则的作用范围，可以添加影响整个类的操作的静态字段。
 * ParentRunner的任何子类，包括BlockJUnit4ClassRunner和Suite，都支持@ClassRule。
 *
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(NameRuleTest::class)
class UsesClassRuleTest {

    private val server = UsesExternalResource.Server()

    @ClassRule
    @JvmField
    val resource = object : ExternalResource() {
        override fun before() {
            super.before()
            server.connect()
        }

        override fun after() {
            super.after()
            server.disconnect()
        }
    }
}

/**
 * RuleChain允许对TestRule进行排序
 * 更多内容看源码吧
 */
class UseRuleChain {
    @Rule
    @JvmField
    val chain = RuleChain.outerRule(LoggingRule("outer rule"))
        .around(LoggingRule("middle rule"))
        .around(LoggingRule("inner rule"))

    /**
     * 很多自定义的规则都可以通过扩展ExternalResource规则来实现。
     * 不过，如果需要知道测试类或方法的更多信息，则需要实现TestRule接口。
     * 主要优势在于，自定义规则可以使用自定义构造函数，将方法添加到类中以供在测试中使用。
     * 或者，将Statement嵌套包装形成新的Statement。
     */
    class LoggingRule(private val des: String) : TestRule {
        override fun apply(base: Statement?, description: Description?): Statement {
            println("des: $des")
            return base!!
        }
    }

    @Test
    fun example() {
        assertTrue(true)
    }
}

/**
 * 自定义一个日志输出，每次测试都会有日志输出。
 */
class LoggerRule : TestRule {

    private lateinit var logger: Logger

    fun getLogger() = logger

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                logger =
                    Logger.getLogger(description?.testClass?.name + "." + description?.displayName)
                base?.evaluate()
            }
        }
    }
}

class LoggerRuleTest {
    @Rule
    @JvmField
    val logger = LoggerRule()

    @Test
    fun checkoutLogger() {
        val log = logger.getLogger()
        log.warning("your test is showing")
    }
}