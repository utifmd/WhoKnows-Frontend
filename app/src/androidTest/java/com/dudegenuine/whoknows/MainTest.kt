package com.dudegenuine.whoknows

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class MainTest {
    @get:Rule
     var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun someTest(){
        hiltRule.inject()
    }
}