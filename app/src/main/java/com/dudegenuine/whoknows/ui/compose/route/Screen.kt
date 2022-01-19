package com.dudegenuine.whoknows.ui.compose.route

import com.dudegenuine.model.common.ImageUtil.strOf

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class Screen(val route: String){
    object AuthScreen: Screen(strOf<AuthScreen>()){
        object LoginScreen: Screen(strOf<LoginScreen>())
        object RegisScreen: Screen(strOf<RegisScreen>())
    }
    object MainScreen: Screen(strOf<MainScreen>()){
        object SummaryScreen: Screen(strOf<SummaryScreen>())
        object DiscoverScreen: Screen(strOf<DiscoverScreen>()){
            object RoomHomeScreen: Screen(strOf<RoomHomeScreen>()){
                object DetailRoomScreen: Screen(strOf<DetailRoomScreen>())
                object QuizCreatorScreen: Screen(strOf<QuizCreatorScreen>())
                object OnBoardingScreen: Screen(strOf<OnBoardingScreen>())
                object BoardingResultScreen: Screen(strOf<BoardingResultScreen>())
            }
            object RoomCreatorScreen: Screen(strOf<RoomCreatorScreen>())
            object RoomFinderScreen: Screen(strOf<RoomFinderScreen>())
        }
        object SettingScreen: Screen(strOf<SettingScreen>())/*{
            object FieldEditScreen: Screen(strOf<FieldEditScreen>())
        }*/
    }

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)

            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
