package ph.edu.cksc.college.appdev.mydiary.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Marwin - Orange Hex Codes
private val Orange500 = Color(0xFFFF9800)
private val Blue500 = Color(0xFF2196F3) // Complementary to Orange

private val DarkColorScheme = darkColorScheme(
    primary = Orange500,
    secondary = Blue500,
    tertiary = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = Blue500,      // Complementary color as requested
    secondary = Blue500,     // To be used as cursorColor
    onSecondary = Color.White
)

@Composable
fun MyDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Set to false to ensure your Orange shows
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

