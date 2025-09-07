package com.example.charactermatchingapp.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.ui.theme.ButtonTextColor
import com.example.charactermatchingapp.ui.theme.ErrorColor
import com.example.charactermatchingapp.ui.theme.MainColor
import com.example.charactermatchingapp.ui.theme.TextMainColor
import com.example.charactermatchingapp.ui.theme.TextSubColor

@Composable
fun SignUpScreen(
    uiState: AuthUiState,
    onSignUp: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.signup_title), style = MaterialTheme.typography.headlineMedium,
            color = TextMainColor
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("アカウント名") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.signUpError != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors().copy(
                unfocusedTextColor = TextSubColor,
                focusedTextColor = TextMainColor,
                focusedLabelColor = MainColor
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.signUpError != null,
            colors = OutlinedTextFieldDefaults.colors().copy(
                unfocusedTextColor = TextSubColor,
                focusedTextColor = TextMainColor,
                focusedLabelColor = MainColor
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.signUpError != null,
            colors = OutlinedTextFieldDefaults.colors().copy(
                unfocusedTextColor = TextSubColor,
                focusedTextColor = TextMainColor,
                focusedLabelColor = MainColor
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onSignUp(email, password, displayName) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MainColor,
                    contentColor = ButtonTextColor
                )
            ) {
                Text(
                    stringResource(id = R.string.signup_button_text),
                    color = ButtonTextColor,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateToLogin, enabled = !uiState.isLoading) {
            Text(
                text = stringResource(id = R.string.signup_navigate_to_login),
                color = MainColor,
            )
        }
        uiState.signUpError?.let { error ->
            Text(
                text = error,
                color = ErrorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = AuthUiState(),
        onSignUp = { _, _, _ -> },
        onNavigateToLogin = {}
    )
}