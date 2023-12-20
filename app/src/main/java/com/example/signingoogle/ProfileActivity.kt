package com.example.signingoogle

import android.content.Intent
import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.signingoogle.ui.theme.SignInGoogleTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInGoogleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userName = intent.getStringExtra("user_name")
                    val userEmail = intent.getStringExtra("user_email")
                    val userPhoto = intent.getStringExtra("user_photo")
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("158053140209-0qbat8b2r9rop9rvvorlhqbd313vujb5.apps.googleusercontent.com")
                        .requestEmail()
                        .build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                    val context = LocalContext.current
                    val i = Intent(context, MainActivity::class.java)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(userPhoto)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.user),
                            contentDescription = ("no image"),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (userName != null && userEmail != null) {
                            Text(text = userName)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = userEmail)
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = {

                            mGoogleSignInClient.signOut()

                            Toast.makeText(context,"Successfully signed out!", Toast.LENGTH_SHORT).show()

                            context.startActivity(i)


                        }) {

                            Text(text = "Sign Out")

                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    SignInGoogleTheme {
        Greeting("Android")
    }
}