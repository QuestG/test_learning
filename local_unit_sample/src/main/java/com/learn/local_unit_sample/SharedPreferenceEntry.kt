package com.learn.local_unit_sample

import java.util.*

/**
 * @author quest
 * @date 2020/4/1
 * Description: 保存个人信息的model类
 */
data class SharedPreferenceEntry(val name: String, val dateOfBirth: Calendar, val email: String)