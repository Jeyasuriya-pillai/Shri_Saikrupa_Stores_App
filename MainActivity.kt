package com.shrisaikrupa.stores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.shrisaikrupa.stores.navigation.NavGraph
import com.shrisaikrupa.stores.ui.theme.ShriSaikrupaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShriSaikrupaTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}