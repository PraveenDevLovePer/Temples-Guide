package com.techdevlp.templesguide.ui.views.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.UserDetails
import com.techdevlp.templesguide.navigations.ScreenNames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreenComposable(
    navController: NavController
) {
    val activity = LocalContext.current as Activity
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = remember { GoogleSignIn.getClient(activity, gso) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task, navController, activity)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.app_icon), contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Connect us using Google",
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(5.dp))

        Image(
            painter = painterResource(id = R.drawable.google_button),
            contentDescription = "Button",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable {
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)

                }
        )
    }
}

fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    navController: NavController,
    activity: Activity
) {
    try {
        CoroutineScope(Dispatchers.Main).launch {
            val account = task.getResult(ApiException::class.java)
            val dataStore = LocalStoredData(MyApplicationContext.getContext())
            val userDetails = UserDetails(
                userId = account.id.toString(),
                fullName = account.displayName,
                emailAddress = account.email,
                phoneNumber = null,
                profilePicUrl = account.photoUrl.toString()
            )
            dataStore.setUserDetails(userDetails)

//            activity.startActivity(Intent(activity, HomeScreenActivity::class.java))
//            activity.finish()
            navController.navigate(route = ScreenNames.HomeScreen.route) {
                popUpTo(route = ScreenNames.SplashScreen.route) {
                    inclusive = true
                }
            }
        }
    } catch (e: ApiException) {
        Log.v("GoogleSignInError", "signInResult:failed code=" + e.statusCode)
    }
}