package bes.max.playlistmaker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bes.max.playlistmaker.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val ysDisplayFamily = FontFamily(
    Font(R.font.ys_display_heavy, FontWeight.ExtraBold),
    Font(R.font.ys_display_bold, FontWeight.Bold),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_light, FontWeight.Light),
    Font(R.font.ys_display_thin, FontWeight.ExtraLight),
)
val ysTextFamily = FontFamily(
    Font(R.font.ys_text_bold, FontWeight.Bold),
    Font(R.font.ys_text_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.ys_text_regular, FontWeight.Normal),
    Font(R.font.ys_text_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.ys_text_medium, FontWeight.Medium),
    Font(R.font.ys_text_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.ys_text_light, FontWeight.Light),
    Font(R.font.ys_text_light_italic, FontWeight.Light, FontStyle.Italic),
)