package com.learn.junit4

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.results.PrintableResult.testResult
import org.junit.experimental.results.ResultMatchers.isSuccessful
import org.junit.rules.*
import java.io.File

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