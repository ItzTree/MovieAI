package com.movieai.app.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font as GoogleFontFont
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.movieai.app.R

private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs,
)

private val interGf       = GoogleFont("Inter")
private val jetBrainsGf   = GoogleFont("JetBrains Mono")

val Pretendard = FontFamily(
    Font(R.font.pretendard_regular,  FontWeight.Normal),
    Font(R.font.pretendard_medium,   FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold,     FontWeight.Bold),
)

val Inter = FontFamily(
    GoogleFontFont(interGf, googleFontProvider, FontWeight.Normal),
    GoogleFontFont(interGf, googleFontProvider, FontWeight.SemiBold),
    GoogleFontFont(interGf, googleFontProvider, FontWeight.Bold),
)

val JetBrainsMono = FontFamily(
    GoogleFontFont(jetBrainsGf, googleFontProvider, FontWeight.Medium),
    GoogleFontFont(jetBrainsGf, googleFontProvider, FontWeight.SemiBold),
)

val MovieAiTypography = Typography(
    displayLarge   = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.Bold,     fontSize = 32.sp, letterSpacing = (-0.02).em),
    displayMedium  = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.Bold,     fontSize = 28.sp, letterSpacing = (-0.02).em),
    headlineLarge  = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.Bold,     fontSize = 24.sp),
    headlineMedium = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleLarge     = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyLarge      = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 22.sp),
    bodyMedium     = TextStyle(fontFamily = Pretendard,    fontWeight = FontWeight.Normal,   fontSize = 13.sp),
    labelMedium    = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,   fontSize = 11.sp, letterSpacing = 0.1.em),
    labelSmall     = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,   fontSize = 10.sp, letterSpacing = 0.15.em),
)
