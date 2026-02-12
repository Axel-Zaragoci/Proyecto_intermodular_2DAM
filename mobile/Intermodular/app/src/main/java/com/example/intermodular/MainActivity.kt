package com.example.intermodular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.intermodular.data.remote.RetrofitProvider
import com.example.intermodular.data.repository.LoginRepository
import com.example.intermodular.ui.theme.AppTheme
import com.example.intermodular.viewmodels.LoginViewModel
import com.example.intermodular.viewmodels.LoginViewModelFactory
import com.example.intermodular.views.scaffold.MyApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val repo = LoginRepository(RetrofitProvider.api)
            val factory =  LoginViewModelFactory(repo)
            val vm: LoginViewModel = viewModel(factory = factory)
            vm.login("cliente@prueba.test", "Password123!")

            val darkMode = rememberSaveable { mutableStateOf(false) }
            AppTheme (darkTheme = darkMode.value) {
                MyApp(
                    darkMode = darkMode.value,
                    onToggleDarkMode = {
                        darkMode.value = !darkMode.value
                    }
                )
            }
        }
    }
}