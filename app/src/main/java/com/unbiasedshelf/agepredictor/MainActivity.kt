package com.unbiasedshelf.agepredictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.unbiasedshelf.agepredictor.ui.MainScreen
import com.unbiasedshelf.agepredictor.ui.theme.AgePredictorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgePredictorTheme {
                MainScreen()
            }
        }
    }
}
