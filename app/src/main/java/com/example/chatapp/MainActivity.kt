package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.Navigation.SetupNavGraph
import com.example.chatapp.Screens.PagerScreen
import com.example.chatapp.data.DataStoreRepository
import com.example.chatapp.data.WelcomeViewmodel
import com.example.chatapp.ui.theme.ChatAppTheme

class MainActivity : ComponentActivity() {
    private  val viewmodel by viewModels<SplashViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                !viewmodel.isReady.value
            }
        }
        setContent {
            val navController= rememberNavController()
            val repo=DataStoreRepository(this)
            SetupNavGraph(navController = navController, welcomeViewmodel = WelcomeViewmodel(repo),this)
        }
    }
}



