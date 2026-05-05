package com.androidforge.streakhappit.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidforge.streakhappit.core.common.Constants
import com.androidforge.streakhappit.presentation.ui.addedit.AddEditHabitScreen
import com.androidforge.streakhappit.presentation.ui.dashboard.HabitDashboardScreen
import com.androidforge.streakhappit.presentation.ui.details.HabitDetailsScreen
import com.androidforge.streakhappit.presentation.ui.settings.SettingsScreen

object AppDestinations {
    const val DASHBOARD_ROUTE = "home"
    const val ADD_EDIT_HABIT_ROUTE = "add_edit_habit"
    const val HABIT_DETAILS_ROUTE = "habit_details"
    const val SETTINGS_ROUTE = "settings"

    const val HABIT_ID_KEY = "habitId"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.DASHBOARD_ROUTE,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(Constants.ANIMATION_DURATION, easing = FastOutSlowInEasing)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(Constants.ANIMATION_DURATION, easing = FastOutSlowInEasing)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(Constants.ANIMATION_DURATION, easing = FastOutSlowInEasing) // Reverse direction for pop
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(Constants.ANIMATION_DURATION, easing = FastOutSlowInEasing) // Reverse direction for pop
            )
        }
    ) {
        composable(route = AppDestinations.DASHBOARD_ROUTE) {
            HabitDashboardScreen(
                onNavigateToAddEditHabit = { habitId ->
                    if (habitId != null) {
                        navController.navigate("${AppDestinations.ADD_EDIT_HABIT_ROUTE}?${AppDestinations.HABIT_ID_KEY}="
                                + habitId)
                    } else {
                        navController.navigate(AppDestinations.ADD_EDIT_HABIT_ROUTE)
                    }
                },
                onNavigateToHabitDetails = { habitId ->
                    navController.navigate("${AppDestinations.HABIT_DETAILS_ROUTE}/$habitId")
                },
                onNavigateToSettings = { navController.navigate(AppDestinations.SETTINGS_ROUTE) }
            )
        }

        composable(
            route = "${AppDestinations.ADD_EDIT_HABIT_ROUTE}?${AppDestinations.HABIT_ID_KEY}={${AppDestinations.HABIT_ID_KEY}}",
            arguments = listOf(navArgument(AppDestinations.HABIT_ID_KEY) { defaultValue = -1L })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getLong(AppDestinations.HABIT_ID_KEY)
            AddEditHabitScreen(
                habitId = habitId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${AppDestinations.HABIT_DETAILS_ROUTE}/{${AppDestinations.HABIT_ID_KEY}}",
            arguments = listOf(navArgument(AppDestinations.HABIT_ID_KEY) { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getLong(AppDestinations.HABIT_ID_KEY) ?: -1L
            HabitDetailsScreen(
                habitId = habitId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditHabit = { idToEdit ->
                    navController.navigate("${AppDestinations.ADD_EDIT_HABIT_ROUTE}?${AppDestinations.HABIT_ID_KEY}="
                            + idToEdit)
                }
            )
        }

        composable(route = AppDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}