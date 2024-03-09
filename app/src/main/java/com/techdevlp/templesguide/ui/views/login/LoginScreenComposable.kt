package com.techdevlp.templesguide.ui.views.login

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.isInternetAvailable
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.UserDetails
import com.techdevlp.templesguide.navigations.ScreenNames
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
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
        handleSignInResult(task, navController)
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var showSnack by remember { mutableStateOf(false) }

    /**
     * Login screen ui
     * @Version V1.0
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome back !",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.dp30),
                    start = dimensionResource(id = R.dimen.dp16),
                    end = dimensionResource(id = R.dimen.dp16)
                ),
            style = Typography.headlineSmall,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.login_hand_icon), contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.dp250))
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Connect us securely with Google",
            fontSize = spTextSizeResource(id = R.dimen.sp16),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp7)))

        Image(
            painter = painterResource(id = R.drawable.google_button),
            contentDescription = "Button",
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = dimensionResource(id = R.dimen.dp70))
                .clickable {
                    if (isInternetAvailable(context = activity)) {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    } else {
                        showSnack = true
                    }
                }
        )

        /**
         * Show Snack bar
         * @Version V1.0
         */
        if (showSnack){
            scope.launch {
                snackBarHostState.showSnackbar(message = activity.getString(R.string.no_internet))
                showSnack = false
            }
        }
    }

    SnackbarHost(hostState = snackBarHostState)
}

/**
 * Checking the given google login details and navigate to home screen.
 * @Version V1.0
 */
fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    navController: NavController
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