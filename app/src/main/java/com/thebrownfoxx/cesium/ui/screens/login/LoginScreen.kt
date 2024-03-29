package com.thebrownfoxx.cesium.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.cesium.R
import com.thebrownfoxx.cesium.ui.components.snackbar.SnackbarHost
import com.thebrownfoxx.cesium.ui.components.snackbar.rememberSnackbarHostState
import com.thebrownfoxx.cesium.ui.theme.AppTheme
import com.thebrownfoxx.components.FilledButton
import com.thebrownfoxx.components.HorizontalSpacer
import com.thebrownfoxx.components.VerticalSpacer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    port: Int?,
    onPortChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    loading: Boolean,
    errors: Flow<String>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(state = rememberSnackbarHostState(errors))
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(16.dp)
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            VerticalSpacer(height = 32.dp)
            Row {
                TextField(
                    label = { Text(text = "IP Address") },
                    value = ipAddress,
                    onValueChange = onIpAddressChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.weight(2f),
                )
                HorizontalSpacer(width = 16.dp)
                TextField(
                    label = { Text(text = "Port") },
                    value = port?.toString() ?: "",
                    onValueChange = onPortChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.weight(1f),
                )
            }
            VerticalSpacer(height = 16.dp)
            TextField(
                label = { Text(text = "Password") },
                value = password,
                onValueChange = onPasswordChange,
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = { onLogin() },
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        AnimatedVisibility(visible = loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )
        }
            VerticalSpacer(height = 16.dp)
            FilledButton(
                text = "Login",
                onClick = onLogin,
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            ipAddress = "",
            onIpAddressChange = {},
            port = null,
            onPortChange = {},
            password = "",
            onPasswordChange = {},
            onLogin = {},
            loading = true,
            errors = flow {},
        )
    }
}

@Preview
@Composable
fun LoginScreenWithErrorPreview() {
    AppTheme {
        LoginScreen(
            ipAddress = "",
            onIpAddressChange = {},
            port = null,
            onPortChange = {},
            password = "",
            onPasswordChange = {},
            onLogin = {},
            loading = true,
            errors = flow {},
        )
    }
}