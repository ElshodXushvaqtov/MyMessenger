package com.example.signingoogle

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signingoogle.data.Message
import com.example.signingoogle.ui.theme.SignInGoogleTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date

class MessageActivity : ComponentActivity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInGoogleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val messageList = remember {
                        mutableStateListOf(Message())
                    }
                    val uid = intent.getStringExtra("uid")
                    val useruid = intent.getStringExtra("useruid")

                    val context = LocalContext.current

                    var text = remember {
                        mutableStateOf(TextFieldValue(""))
                    }

                    LazyColumn() {
                        items(messageList) {
                            val backgroundColor = if (it.from == uid) Color(0xFF9CFFE3) else Color.Gray
                            val specificAlign =
                                if (it.from == uid) Alignment.End else Alignment.Start

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = specificAlign
                            ) {
                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = backgroundColor
                                    ),
                                    content = {
                                        Column(
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = it.text ?: "",
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                text = it.date ?: "",
                                                color = Color.Black,
                                                fontSize = 8.sp,
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                    }
                                )

                            }

                        }
                    }
                    val ref = Firebase.database.reference.child("users")
                        .child(uid!!)
                        .child("message")
                        .child(useruid!!)
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val children = snapshot.children
                            messageList.clear()
                            children.forEach {
                                val message = it.getValue(Message::class.java)
                                messageList.add(message ?: Message())
                                Log.d("MESS", "onCreate: ${message?.text}")
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("ERR", "onCancelled: ${error.message}")
                        }

                    })
                    val sdf = SimpleDateFormat("HH:mm")
                    val currentDateAndTime = sdf.format(Date())
                    val m = Message(useruid, uid, text.value.text, currentDateAndTime)

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TextField(
                            text.value,
                            onValueChange = {
                                text.value = it
                            },
                            placeholder = { Text(text = "New message") },
                            label = { Text("Enter text") },
                            modifier = Modifier
                                .padding(8.dp),

                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT)
                                        .show()
                                    val reference = Firebase.database.reference.child("users")
                                    val key = reference.push().key.toString()
                                    reference.child(uid)
                                        .child("message")
                                        .child(useruid)
                                        .child(key)
                                        .setValue(m)
                                    reference.child(useruid)
                                        .child("message")
                                        .child(uid)
                                        .child(key)
                                        .setValue(m)

                                    text.value = TextFieldValue("")
                                }
                            ))


                        Button(onClick = {
                            val reference = Firebase.database.reference.child("users")
                            val key = reference.push().key.toString()
                            reference.child(uid)
                                .child("message")
                                .child(useruid)
                                .child(key)
                                .setValue(m)
                            reference.child(useruid)
                                .child("message")
                                .child(uid)
                                .child(key)
                                .setValue(m)

                            text.value = TextFieldValue("")
                        })
                        {
                            Text(text = "send")
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SignInGoogleTheme {

    }
}