package com.example.signingoogle

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.signingoogle.data.UserData
import com.example.signingoogle.ui.theme.SignInGoogleTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import org.jetbrains.annotations.Async

private lateinit var auth: FirebaseAuth
private var mAuth = FirebaseAuth.getInstance()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInGoogleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    auth = FirebaseAuth.getInstance()
                    mAuth = FirebaseAuth.getInstance()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("158053140209-0qbat8b2r9rop9rvvorlhqbd313vujb5.apps.googleusercontent.com")
                        .requestEmail()
                        .build()

                    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {

                            val signInIntent = mGoogleSignInClient.signInIntent
                            startActivityForResult(signInIntent, 1)

                        }) {

                            Text(text = "Sign In Google")

                        }

                        Button(onClick = {

                            Firebase.auth.signOut()

                        }) {

                            Text(text = "Sign Out")

                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {


                        if (getUser() != null) {
                            Text(text = "Name: ${getUser()?.name}")
                            AsyncImage(
                                model = "Image: ${getUser()?.photo}",
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, /* p1 = */ null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    if (user != null) {

                        val userData = UserData(
                            user.displayName,
                            user.email,
                            user.uid,
                            user.photoUrl.toString()
                        )
                        setUser(userData)

                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@MainActivity, "Authentication Failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun setUser(userData: UserData) {

        val user = FirebaseAuth.getInstance().currentUser
        user?.run {
            val userIdReference = Firebase.database.reference
                .child("users").child(user.uid)
            userIdReference.setValue(userData)
        }

    }

    private fun getUser(): UserData? {
        var userData: UserData? = null
        val user = FirebaseAuth.getInstance().currentUser
        user?.run {
            val userIdReference = Firebase.database.reference
                .child("users").child(uid)

            userIdReference.get().addOnSuccessListener { dataSnapShot ->
                userData = dataSnapShot.getValue<UserData>()
                //successfully read UserData from the database
            }
        }
        return userData


    }

}

@Composable
fun Greeting() {}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {}