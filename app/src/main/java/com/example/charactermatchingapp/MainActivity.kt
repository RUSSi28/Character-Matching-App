package com.example.charactermatchingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.charactermatchingapp.ui.theme.CharacterMatchingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CharacterMatchingAppTheme {
                MainNavigation()
            }
        }
    }
}
