package com.example.chatapp.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.Screens.HomeScreen
import com.example.chatapp.Screens.WelcomeScreen
import com.example.chatapp.data.WelcomeViewmodel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    welcomeViewmodel: WelcomeViewmodel,
    context: Context
) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route ){
        composable(route=Screen.Welcome.route){
            WelcomeScreen(navController,welcomeViewmodel)
        }
        composable(route=Screen.Home.route){
            HomeScreen(context,navController)
        }
    }

}