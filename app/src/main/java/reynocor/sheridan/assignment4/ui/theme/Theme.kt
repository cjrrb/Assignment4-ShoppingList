package reynocor.sheridan.assignment4.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MintPrimary,
    onPrimary = OnDarkPrimary,

    secondary = MintPrimary,
    onSecondary = OnDarkPrimary,

    tertiary = MintPrimaryLight,
    onTertiary = OnDarkPrimary,

    background = DarkBackground,
    onBackground = OnDark,

    surface = DarkSurface,
    onSurface = OnDark,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSecondary,

    outline = DarkOutline,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun Assignment4ShoppingListTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}