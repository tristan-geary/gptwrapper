package ca.bishops.cs330.gptwrapper.s002344234

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

///triggers Hilt's code generation and allows DI in app
@HiltAndroidApp
///entry point to app
class MyApp : Application()
