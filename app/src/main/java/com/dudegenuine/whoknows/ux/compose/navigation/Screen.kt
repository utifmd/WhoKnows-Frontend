package com.dudegenuine.whoknows.ux.compose.navigation

import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.BuildConfig

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class Screen(
    val route: String,
    val uriPattern: String? = null){

    companion object {
        const val ROOT_ROUTE = "root_route"
    }

    object Auth: Screen(strOf<Auth>()){
        object Login: Screen(strOf<Auth>()+strOf<Login>())
        object Register: Screen(strOf<Auth>()+strOf<Register>())
    }

    object Home: Screen(strOf<Home>()){
        object Summary: Screen(strOf<Home>()+strOf<Summary>()){
            object Notification: Screen(
                strOf<Summary>()+strOf<Notification>(),
                "${BuildConfig.BASE_CLIENT_URL}/who-knows/notifications")
            object RoomFinder: Screen(strOf<Summary>()+strOf<RoomFinder>())
            object RoomCreator: Screen(strOf<Summary>()+strOf<RoomCreator>())
            object RoomDetail: Screen(
                strOf<Summary>()+strOf<RoomDetail>(),
                "${BuildConfig.BASE_CLIENT_URL}/who-knows/room"){
                object QuizCreator: Screen(strOf<RoomDetail>()+strOf<QuizCreator>())
                object QuizDetail: Screen(strOf<RoomDetail>()+strOf<QuizDetail>())
                object ProfileDetail: Screen(
                    strOf<RoomDetail>()+strOf<ProfileDetail>(),
                "${BuildConfig.BASE_CLIENT_URL}/who-knows/user")
                object ResultDetail: Screen(strOf<RoomDetail>()+strOf<ResultDetail>())
            }
            object Participation: Screen(strOf<Summary>()+strOf<Participation>()){
                //object Result: Screen(strOf<Participation>()+strOf<Result>())
            }
        }

        object Discover: Screen(strOf<Home>()+strOf<Discover>()){
            object SearchScreen: Screen(strOf<Discover>()+strOf<SearchScreen>())
            object RoomFinder: Screen(strOf<Discover>()+strOf<RoomFinder>())
            object RoomDetail: Screen(strOf<Discover>()+strOf<RoomDetail>())
            //object ProfileDetail: Screen(strOf<Discover>()+strOf<ProfileDetail>())
        }

        object Setting: Screen(strOf<Home>()+strOf<Setting>()){
            object ProfileEditor: Screen(strOf<Setting>()+strOf<ProfileEditor>())
        }

        object Preview: Screen(
            strOf<Home>()+strOf<Preview>(),
            "${BuildConfig.BASE_CLIENT_URL}/who-knows/image-viewer")
    }

    // fun withKey(key: String): String = "$route/$key={$key}"

    fun routeWithArgs(vararg args: String): String {
        return buildString {
            append(route)

            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun uriWithArgs(vararg args: String): String {
        return buildString {
            append(uriPattern)

            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
