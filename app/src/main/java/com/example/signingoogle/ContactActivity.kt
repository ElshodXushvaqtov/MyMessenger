package com.example.signingoogle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.signingoogle.data.UserData
import com.example.signingoogle.ui.theme.SignInGoogleTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContactActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInGoogleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uid = intent.getStringExtra("uid")

                    val userName = intent.getStringExtra("userName")
                    val userEmail = intent.getStringExtra("userEmail")
                    val userPhoto = intent.getStringExtra("userPhoto")

                    val userList = remember {
                        mutableStateListOf(UserData())
                    }

                    val reference = Firebase.database.reference.child("users")
                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val u = snapshot.children
                            userList.clear()
                            u.forEach {
                                val userData = it.getValue(UserData::class.java)
                                if (userData != null && uid != userData.uid) {
                                    userList.add(userData)
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("TAG", "onCancelled: ${error.message}")
                        }

                    })
                    Column(modifier = Modifier.fillMaxSize()) {

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {


                            IconButton(onClick = {
                                val i = Intent(
                                    this@ContactActivity,
                                    ProfileActivity::class.java
                                )
                                i.putExtra("user_photo", userPhoto)
                                i.putExtra("user_name", userName)
                                i.putExtra("user_email", userEmail)
                                startActivity(i)
                            }) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(userPhoto)
                                        .crossfade(true)
                                        .build(),
                                    placeholder = painterResource(R.drawable.user),
                                    contentDescription = ("no image"),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.clip(CircleShape),
                                    alignment = Alignment.TopEnd
                                )
                            }

                            Text(
                                text = "Welcome, $userName",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        LazyColumn() {
                            items(userList) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable {
                                            val i = Intent(
                                                this@ContactActivity,
                                                MessageActivity::class.java
                                            )
                                            i.putExtra("uid", uid)
                                            i.putExtra("useruid", it.uid)
                                            startActivity(i)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(it.photo)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(R.drawable.user),
                                        contentDescription = ("no image"),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.clip(CircleShape)
                                    )
                                    Text(
                                        text = it.name ?: "",
                                        Modifier.padding(start = 12.dp),
                                        fontSize = 22.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SignInGoogleTheme {


    }
}