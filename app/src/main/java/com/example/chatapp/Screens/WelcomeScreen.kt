package com.example.chatapp.Screens

import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chatapp.Navigation.Screen
import com.example.chatapp.R
import com.example.chatapp.data.WelcomeViewmodel
import com.example.chatapp.ui.theme.BlueHeading
import com.example.chatapp.ui.theme.continueText
import com.example.chatapp.ui.theme.fontFamily
import com.example.chatapp.ui.theme.greyTitle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(navHostController: NavHostController,welcomeViewmodel: WelcomeViewmodel) {
    val pagerState= rememberPagerState()

    Column(Modifier.fillMaxSize()){
        HorizontalPager(count = 1, state = pagerState) {
            PagerScreen(navHostController,welcomeViewmodel)
        }
    }

}

@Composable
fun PagerScreen(navHostController: NavHostController,welcomeViewmodel: WelcomeViewmodel) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Your AI Assistant", fontFamily = fontFamily, color= BlueHeading, fontSize =23.sp )
                Text(modifier = Modifier.padding(start = 30.dp, top = 15.dp, end = 30.dp),text = "Using This Software you can ask questions and receive articles using artifical intelligence assistant ",
                    color= greyTitle, fontSize = 15.sp, fontFamily= fontFamily, textAlign = TextAlign.Center)
        }

        Image(painterResource(id = R.drawable.welcome_image), contentDescription = "welcome_image")
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(BlueHeading),
            onClick = {
                      navHostController.popBackStack()
                navHostController.navigate(Screen.Home.route)
                welcomeViewmodel.saveOnBoardState(completed = true)
            }, elevation = ButtonDefaults.buttonElevation(20.dp)) {
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Continue",modifier=Modifier.padding(vertical = 10.dp),
                        color= continueText,
                        fontSize = 15.sp,
                        fontFamily= fontFamily,
                        textAlign = TextAlign.Center)
                Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "arrow_welcome")
            }
            
        }

    }
}