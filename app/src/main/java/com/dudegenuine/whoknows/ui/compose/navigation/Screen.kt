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
        object Login: Screen(strOf<Login>())
        object Register: Screen(strOf<Register>())
    }

    object Home: Screen(strOf<Home>()){
        object Summary: Screen(strOf<Summary>()){
            object RoomFinder: Screen(strOf<RoomFinder>())
            object RoomCreator: Screen(strOf<RoomCreator>())
            object ListOwnerRoom: Screen(strOf<ListOwnerRoom>()) //object RoomCreator: Screen(strOf<RoomCreator>())
            object DetailOwnerRoom: Screen(strOf<DetailOwnerRoom>()){
                object QuizCreator: Screen(strOf<QuizCreator>()) //object RoomHome: Screen(strOf<RoomHome>()){}
            }
        }

        object Discover: Screen(strOf<Discover>()){
            object ListPublicRoom: Screen(strOf<ListPublicRoom>())
            object DetailPublicRoom: Screen(strOf<DetailPublicRoom>())
            object OnBoarding: Screen(strOf<OnBoarding>()){
                object Result: Screen(strOf<Result>())
            }
        }

        object Setting: Screen(strOf<Setting>()){
            /*object Profile: Screen(strOf<Profile>())*/
            object ProfileEditor: Screen(strOf<ProfileEditor>())
        }
    }

    fun withKey(key: String): String = "$route/$key={$key}"

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)

            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
