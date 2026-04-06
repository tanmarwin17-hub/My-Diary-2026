package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ph.edu.cksc.college.appdev.mydiary.MAIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.RegisterComponent
import ph.edu.cksc.college.appdev.mydiary.components.RegisterViewModel
import ph.edu.cksc.college.appdev.mydiary.diary.Registration
import ph.edu.cksc.college.appdev.mydiary.supabase

suspend fun registerUser(name: String, email: String, password: String): String {
    try {
        val reponse = supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject {
                put("full_name", name)
                put("email", email)
            }
        }
        println("User registered successfully: ${reponse}")
        val session: UserSession? = supabase.auth.currentSessionOrNull()
        userSession = session
        println("User auto signed in successfully: ${userSession}")
        return "Success"
    } catch (e: Exception) {
        val error = e.message ?: "Sign up error"
        Log.e("Sign up", error, e)
        println("Sign-up failed: ${error}")
        return error
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("App Dev Demo")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        RegistrationScrollContent(innerPadding, navController, snackbarHostState)
    }
}

@Composable
fun RegistrationScrollContent(
    innerPadding: PaddingValues,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        RegisterComponent(
            viewModel = object : RegisterViewModel {
                @SuppressLint("UnrememberedMutableState")
                override var account = mutableStateOf(Registration())

                override var modified: Boolean = true

                override fun onNameChange(newValue: String) {
                    account.value = account.value.copy(name = newValue)
                }

                override fun onEmailChange(newValue: String) {
                    account.value = account.value.copy(email = newValue)
                }

                override fun onPasswordChange(newValue: String) {
                    account.value = account.value.copy(password = newValue)
                }

                override fun onRetypePasswordChange(newValue: String) {
                    account.value = account.value.copy(retypePassword = newValue)
                }

                override suspend fun register(): String {
                    Log.d("Sign up", "Registering ${account.value.email}")
                    val result = registerUser(account.value.name, account.value.email, account.value.password)
                    if (result == "Success") {
                        scope.launch {
                            val user = userSession?.user?.email
                            Log.d("Login", user ?: "")
                            snackbarHostState.showSnackbar(
                                message = "Welcome $user"
                            )
                            // or still need to login?
                            navController.navigate(MAIN_SCREEN)
                        }
                    } else {
                        Log.d("Sign up", result)
                    }
                    return result
                }
            },
            test = false,
            onCancel = {
                navController.popBackStack()
            }
        )
    }
}
