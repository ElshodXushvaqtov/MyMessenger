package com.example.signingoogle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
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
import kotlin.properties.Delegates

//private lateinit var sharedPreference: SharedPreferences
//private lateinit var editor: Editor
//private var is_logged by Delegates.notNull<Boolean>()

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

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
    context = LocalContext.current
    val iM = Intent(context, MainActivity::class.java)
    val iC = Intent(context, ContactActivity::class.java)
    val sharedPreference = context.getSharedPreferences("myShared", Context.MODE_PRIVATE)
    val isLogged =
        context.getSharedPreferences("myShared", Context.MODE_PRIVATE).getBoolean("isLogged", false)

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
            Log.d("ISLOG", isLogged.toString())
            delay(10000L)
            if (isLogged) {
                iC.putExtra("uid", sharedPreference.getString("userID", ""))
                iC.putExtra("userPhoto", sharedPreference.getString("uPhoto", ""))
                iC.putExtra("userName", sharedPreference.getString("uName", ""))
                iC.putExtra("userEmail", sharedPreference.getString("uEmail", ""))
                context.startActivity(iC)
            } else {

                context.startActivity(iM)
            }
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