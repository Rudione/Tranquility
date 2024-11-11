package my.rudione.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import my.rudione.designsystem.R

val Lexend = FontFamily(
    Font(R.font.lexend_medium, FontWeight.Medium),
    Font(R.font.lexend_semi_bold, FontWeight.SemiBold),
    Font(R.font.lexend_bold, FontWeight.Bold)
)
val OpenSans = FontFamily(
    Font(R.font.open_sans_light, FontWeight.Light),
    Font(R.font.open_sans_regular, FontWeight.Normal)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp
    ),
    labelLarge = TextStyle(
        fontFamily = OpenSans
    ),
    labelMedium = TextStyle(
        fontFamily = OpenSans
    )
)