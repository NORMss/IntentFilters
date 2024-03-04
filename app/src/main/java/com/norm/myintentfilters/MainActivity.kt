package com.norm.myintentfilters

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.norm.myintentfilters.ui.theme.MyIntentFiltersTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyIntentFiltersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Open the image with this app via share"
                        )
                        viewModel.uri?.let {
                            AsyncImage(
                                model = viewModel.uri,
                                contentDescription = null,
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("test@test.ru"))
                                    putExtra(Intent.EXTRA_SUBJECT, "This my subject")
                                    putExtra(Intent.EXTRA_TEXT, "This is the content my email")
                                }
                                if (intent.resolveActivity(packageManager) != null) {
                                    startActivity(intent)
                                }
                            }
                        ) {
                            Text(
                                text = "Open Email"
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                        Button(
                            onClick = {
                                Intent(Intent.ACTION_MAIN).also {
                                    it.`package` = "com.google.android.youtube"
                                    try {
                                        startActivity(it)
                                    } catch (e: ActivityNotFoundException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Open YouTube"
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                        Button(
                            onClick = {
                                Intent(applicationContext, SecondActivity::class.java).also {
                                    startActivity(it)
                                }

                            }
                        ) {
                            Text(
                                text = "Open SecondActivity"
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        viewModel.updateUri(uri)
    }
}