package com.unbiasedshelf.agepredictor.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.unbiasedshelf.agepredictor.R

val Rubik = FontFamily(
    Font(R.font.rubik_wght)
)

val AbhayaLibre = FontFamily(
    Font(R.font.abhayalibre_regular, FontWeight.Normal),
    Font(R.font.abhayalibre_bold, FontWeight.Bold)
)

val ABeeZee = FontFamily(
    Font(R.font.abeezee_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),
    body2 = TextStyle(
        fontFamily = ABeeZee,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    h1 = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Light,
        fontSize = 50.sp,
        lineHeight = 30.sp
    ),
    h5 = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 30.sp
    ),
    h6 = TextStyle(
        fontFamily = AbhayaLibre,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    caption = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 17.sp
    )
    /* Todo dialog fonts */
)

