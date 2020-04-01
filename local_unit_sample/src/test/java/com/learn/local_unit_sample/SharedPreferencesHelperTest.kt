package com.learn.local_unit_sample

import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

/**
 * @author quest
 * @date 2020/4/1
 * Description:对SharedPreferencesHelperTest进行单元测试，通过Mockito模拟SharedPreferences
 */

//此注解可告知 Mockito 测试运行程序验证您对框架的使用是否正确无误并简化了模拟对象的初始化。
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {

    private val TEST_NAME = "Test name"
    private val TEST_EMAIL = "test@email.com"
    private val TEST_DATE_OF_BIRTH = Calendar.getInstance().apply { set(1980, 1, 1) }

    private lateinit var sharedPreferenceEntry: SharedPreferenceEntry
    private lateinit var mockSharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var mockBrokenSharedPreferencesHelper: SharedPreferencesHelper

    /**
     * 如果需要模拟Android依赖项中的对象，则添加@Mock注解
     */
    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockBrokenSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockBrokenEditor: SharedPreferences.Editor

    @Before
    fun initMocks() {
        sharedPreferenceEntry = SharedPreferenceEntry(TEST_NAME, TEST_DATE_OF_BIRTH, TEST_EMAIL)
        //创建一个模拟的SharedPreferences
        mockSharedPreferencesHelper = createMockSharedPreferences()
        //创建一个模拟的SharedPreferences，但不能保存数据。即一个失败的SharedPreferences
        mockBrokenSharedPreferencesHelper = createBrokenMockSharedPreferences()
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        //保存个人信息到SharedPreferences
        assertTrue(mockSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry))
        //从SharedPreferences读取个人信息
        val savedEntry = mockSharedPreferencesHelper.getPersonalInfo()
        //确保写入的个人信息和读取的个人信息是一致的
        assertEquals(sharedPreferenceEntry.name, savedEntry.name)
        assertEquals(sharedPreferenceEntry.dateOfBirth, savedEntry.dateOfBirth)
        assertEquals(sharedPreferenceEntry.email, savedEntry.email)
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        assertFalse(mockBrokenSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry))
    }

    /**
     * 创建一个SharedPreferences管理类，在创建之前，模拟从SharedPreferences对象中读取内容
     */
    private fun createMockSharedPreferences(): SharedPreferencesHelper {
        //模拟读取SharedPreferences
        given(
            mockSharedPreferences.getString(
                eq(SharedPreferencesHelper.KEY_NAME),
                anyString()
            )
        ).willReturn(sharedPreferenceEntry.name)

        given(
            mockSharedPreferences.getString(
                eq(SharedPreferencesHelper.KEY_EMAIL),
                anyString()
            )
        ).willReturn(sharedPreferenceEntry.email)

        given(
            mockSharedPreferences.getLong(
                eq(SharedPreferencesHelper.KEY_DOB),
                anyLong()
            )
        ).willReturn(sharedPreferenceEntry.dateOfBirth.timeInMillis)

        //模拟一次成功的提交
        given(mockEditor.commit()).willReturn(true)

        //当调用edit时，返回MockEditor
        given(mockSharedPreferences.edit()).willReturn(mockEditor)

        return SharedPreferencesHelper(mockSharedPreferences)
    }

    private fun createBrokenMockSharedPreferences(): SharedPreferencesHelper {
        //模拟提交失败
        given(mockBrokenEditor.commit()).willReturn(false)
        given(mockBrokenSharedPreferences.edit()).willReturn(mockBrokenEditor)
        return SharedPreferencesHelper(mockBrokenSharedPreferences)
    }
}