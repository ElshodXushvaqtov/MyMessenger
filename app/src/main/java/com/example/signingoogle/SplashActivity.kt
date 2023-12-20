package com.example.signingoogle

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.signingoogle.ui.theme.SignInGoogleTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInGoogleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Splash()
                }
            }
        }
    }
}

@Composable
fun Splash() {
    val animation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    val context = LocalContext.current
    val iM = Intent(context, MainActivity::class.java)
    val iC = Intent(context, ContactActivity::class.java)
//    val sharedPreference =
//    var editor = sharedPreference.edit()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 290.dp, bottom = 80.dp),
            horizontalArrangement = Arrangement.Center
        ) {}
        LottieAnimation(composition = animation)
        LaunchedEffect(key1 = true) {

            delay(10000L)

            context.startActivity(iM)

        }


    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SignInGoogleTheme {
        Splash()
    }
}