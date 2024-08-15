package com.flexa.reactnative.theme

 data class Theme(
  val backgroundColor: String?,
  val sortTextColor: String?,
  val titleColor: String?,
  val cardColor: String?,
  val borderRadius: String?
)

data class Platform(
  val light: Theme,
  val dark: Theme
)

data class ThemeData(
  val iOS: Platform,
  val android: Platform
)
