package ca.bishops.cs330.gptwrapper.s002344234

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ca.bishops.cs330.gptwrapper.s002344234.ui.ChatViewModel
import ca.bishops.cs330.gptwrapper.s002344234.ui.screens.ChatScreen
import dagger.hilt.android.AndroidEntryPoint


///allows hilt to perform DI in activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    ///lazily create view model scoped to this activity using hilt
    private val viewModel: ChatViewModel by viewModels()


    ///sets UI content using jetpack compose
    ///passes chatviewmodel to composable screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen(viewModel)
        }
    }
}
