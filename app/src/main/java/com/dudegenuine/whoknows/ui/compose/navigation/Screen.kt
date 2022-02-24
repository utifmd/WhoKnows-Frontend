package com.dudegenuine.whoknows.ui.compose.navigation

import com.dudegenuine.model.common.ImageUtil.strOf

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class Screen(val route: String){

    companion object {
        const val ROOT_ROUTE = "root_route"
    }

    object Auth: Screen(strOf<Auth>()){
        object Login: Screen(strOf<Auth>()+strOf<Login>())
        object Register: Screen(strOf<Auth>()+strOf<Register>())
    }

    object Home: Screen(strOf<Home>()){
        object Summary: Screen(strOf<Home>()+strOf<Summary>()){
            object RoomFinder: Screen(strOf<Summary>()+strOf<RoomFinder>())
            object RoomCreator: Screen(strOf<Summary>()+strOf<RoomCreator>())
            object RoomDetail: Screen(strOf<Summary>()+strOf<RoomDetail>()){
                object QuizCreator: Screen(strOf<RoomDetail>()+strOf<QuizCreator>())
                object QuizDetail: Screen(strOf<RoomDetail>()+strOf<QuizDetail>())
                object ProfileDetail: Screen(strOf<RoomDetail>()+strOf<ProfileDetail>())
            }
            object OnBoarding: Screen(strOf<Summary>()+strOf<OnBoarding>()){
                object Result: Screen(strOf<OnBoarding>()+strOf<Result>())
            }
        }

        object Discover: Screen(strOf<Home>()+strOf<Discover>()){
            object Notification: Screen(strOf<Discover>()+strOf<Notification>())
            object RoomFinder: Screen(strOf<Discover>()+strOf<RoomFinder>())
            object RoomDetail: Screen(strOf<Discover>()+strOf<RoomDetail>())
            object ProfileDetail: Screen(strOf<Discover>()+strOf<ProfileDetail>())
        }

        object Setting: Screen(strOf<Home>()+strOf<Setting>()){
            object ProfileEditor: Screen(strOf<Setting>()+strOf<ProfileEditor>())
        }
    }

    // fun withKey(key: String): String = "$route/$key={$key}"

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)

            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
