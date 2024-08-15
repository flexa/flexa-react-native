package com.flexa.reactnative.theme

import androidx.compose.ui.graphics.Color
import org.json.JSONObject
import com.flexa.reactnative.theme.*
import org.json.JSONException

class ThemeAdapter {

    @Throws(JSONException::class)
    fun stringToThemeData(themeData: String?): ThemeData? {
        if (null == themeData) return null

        val jsonObject = JSONObject(themeData)

        val iOSObject = jsonObject.getJSONObject("iOS")
        val androidObject = jsonObject.getJSONObject("android")

        val iOSLight = iOSObject.getJSONObject("light")
        val iOSDark = iOSObject.getJSONObject("dark")

        val androidLight = androidObject.getJSONObject("light")
        val androidDark = androidObject.getJSONObject("dark")

        val iOSLightTheme = Theme(
            backgroundColor = try {
                iOSLight.getString("backgroundColor")
            } catch (e: JSONException) {
                null
            },
            sortTextColor = try {
                iOSLight.getString("sortTextColor")
            } catch (e: JSONException) {
                null
            },
            titleColor = try {
                iOSLight.getString("titleColor")
            } catch (e: JSONException) {
                null
            },
            cardColor = try {
                iOSLight.getString("cardColor")
            } catch (e: JSONException) {
                null
            },
            borderRadius = try {
                iOSLight.getString("borderRadius")
            } catch (e: JSONException) {
                null
            },
        )

        val iOSDarkTheme = Theme(
            backgroundColor = try {
                iOSDark.getString("backgroundColor")
            } catch (e: JSONException) {
                null
            },
            sortTextColor = try {
                iOSDark.getString("sortTextColor")
            } catch (e: JSONException) {
                null
            },
            titleColor = try {
                iOSDark.getString("titleColor")
            } catch (e: JSONException) {
                null
            },
            cardColor = try {
                iOSDark.getString("cardColor")
            } catch (e: JSONException) {
                null
            },
            borderRadius = try {
                iOSDark.getString("borderRadius")
            } catch (e: JSONException) {
                null
            },
        )

        val androidLightTheme = Theme(
            backgroundColor = try {
                androidLight.getString("backgroundColor")
            } catch (e: JSONException) {
                null
            },
            sortTextColor = try {
                androidLight.getString("sortTextColor")
            } catch (e: JSONException) {
                null
            },
            titleColor = try {
                androidLight.getString("titleColor")
            } catch (e: JSONException) {
                null
            },
            cardColor = try {
                androidLight.getString("cardColor")
            } catch (e: JSONException) {
                null
            },
            borderRadius = try {
                androidLight.getString("borderRadius")
            } catch (e: JSONException) {
                null
            },
        )

        val androidDarkTheme = Theme(
            backgroundColor = try {
                androidDark.getString("backgroundColor")
            } catch (e: JSONException) {
                null
            },
            sortTextColor = try {
                androidDark.getString("sortTextColor")
            } catch (e: JSONException) {
                null
            },
            titleColor = try {
                androidDark.getString("titleColor")
            } catch (e: JSONException) {
                null
            },
            cardColor = try {
                androidDark.getString("cardColor")
            } catch (e: JSONException) {
                null
            },
            borderRadius = try {
                androidDark.getString("borderRadius")
            } catch (e: JSONException) {
                null
            },
        )

        val iOSPlatform = Platform(iOSLightTheme, iOSDarkTheme)
        val androidPlatform = Platform(androidLightTheme, androidDarkTheme)

        val res = ThemeData(iOSPlatform, androidPlatform)
        return res
    }

    fun getColor(color: String?): Color? {
        if (null == color) return null

        val rgbaCSSRegex = Regex("rgba\\((\\d+), (\\d+), (\\d+), ([+-]?([0-9]*[.])?[0-9]+)\\)")
        val cssResult = rgbaCSSRegex.matchEntire(color)
        if (cssResult != null) {
            val (r, g, b, a) = cssResult.destructured
            val alpha = (a.toFloat() * 255).toInt()
            val argb = String.format("#%02X%02X%02X%02X", alpha, r.toInt(), g.toInt(), b.toInt())
            println(argb)  // Outputs: #80FF0000
            return Color(red = r.toInt(), green = g.toInt(), blue = b.toInt(), alpha = alpha)
        }
        val rgbRegex = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$")
        if (color.startsWith("#")) {
            return when (color.length) {
                4, 5 -> {
                    val r = color[1].toString().repeat(2)
                    val g = color[2].toString().repeat(2)
                    val b = color[3].toString().repeat(2)
                    val a = if (color.length == 5) color[4].toString().repeat(2) else "FF"
                    println("#$a$r$g$b <<<")
                    return Color(android.graphics.Color.parseColor("#$a$r$g$b"))
                }

                else -> Color(android.graphics.Color.parseColor(color))
            }
        }
        if (color.all { it.isLetter() }) {
            return colorFromKeyword(color)
        }

        return null
    }

    private fun colorFromKeyword(color: String?): Color? {
        return when (color) {
            "aliceblue" -> Color(0xf0f8ff)
            "antiquewhite" -> Color(0xfaebd7)
            "aqua" -> Color(0x00ffff)
            "aquamarine" -> Color(0x7fffd4)
            "azure" -> Color(0xf0ffff)
            "beige" -> Color(0xf5f5dc)
            "bisque" -> Color(0xffe4c4)
            "black" -> Color(0x000000)
            "blanchedalmond" -> Color(0xffebcd)
            "blue" -> Color(0x0000ff)
            "blueviolet" -> Color(0x8a2be2)
            "brown" -> Color(0xa52a2a)
            "burlywood" -> Color(0xdeb887)
            "cadetblue" -> Color(0x5f9ea0)
            "chartreuse" -> Color(0x7fff00)
            "chocolate" -> Color(0xd2691e)
            "coral" -> Color(0xff7f50)
            "cornflowerblue" -> Color(0x6495ed)
            "cornsilk" -> Color(0xfff8dc)
            "crimson" -> Color(0xdc143c)
            "cyan" -> Color(0x00ffff)
            "darkblue" -> Color(0x00008b)
            "darkcyan" -> Color(0x008b8b)
            "darkgoldenrod" -> Color(0xb8860b)
            "darkgray" -> Color(0xa9a9a9)
            "darkgreen" -> Color(0x006400)
            "darkgrey" -> Color(0xa9a9a9)
            "darkkhaki" -> Color(0xbdb76b)
            "darkmagenta" -> Color(0x8b008b)
            "darkolivegreen" -> Color(0x556b2f)
            "darkorange" -> Color(0xff8c00)
            "darkorchid" -> Color(0x9932cc)
            "darkred" -> Color(0x8b0000)
            "darksalmon" -> Color(0xe9967a)
            "darkseagreen" -> Color(0x8fbc8f)
            "darkslateblue" -> Color(0x483d8b)
            "darkslategrey" -> Color(0x2f4f4f)
            "darkturquoise" -> Color(0x00ced1)
            "darkviolet" -> Color(0x9400d3)
            "deeppink" -> Color(0xff1493)
            "deepskyblue" -> Color(0x00bfff)
            "dimgray" -> Color(0x696969)
            "dimgrey" -> Color(0x696969)
            "dodgerblue" -> Color(0x1e90ff)
            "firebrick" -> Color(0xb22222)
            "floralwhite" -> Color(0xfffaf0)
            "forestgreen" -> Color(0x228b22)
            "fuchsia" -> Color(0xff00ff)
            "gainsboro" -> Color(0xdcdcdc)
            "ghostwhite" -> Color(0xf8f8ff)
            "gold" -> Color(0xffd700)
            "goldenrod" -> Color(0xdaa520)
            "gray" -> Color(0x808080)
            "green" -> Color(0x008000)
            "greenyellow" -> Color(0xadff2f)
            "grey" -> Color(0x808080)
            "honeydew" -> Color(0xf0fff0)
            "hotpink" -> Color(0xff69b4)
            "indianred" -> Color(0xcd5c5c)
            "indigo" -> Color(0x4b0082)
            "ivory" -> Color(0xfffff0)
            "khaki" -> Color(0xf0e68c)
            "lavender" -> Color(0xe6e6fa)
            "lavenderblush" -> Color(0xfff0f5)
            "lawngreen" -> Color(0x7cfc00)
            "lemonchiffon" -> Color(0xfffacd)
            "lightblue" -> Color(0xadd8e6)
            "lightcoral" -> Color(0xf08080)
            "lightcyan" -> Color(0xe0ffff)
            "lightgoldenrodyellow" -> Color(0xfafad2)
            "lightgray" -> Color(0xd3d3d3)
            "lightgreen" -> Color(0x90ee90)
            "lightgrey" -> Color(0xd3d3d3)
            "lightpink" -> Color(0xffb6c1)
            "lightsalmon" -> Color(0xffa07a)
            "lightseagreen" -> Color(0x20b2aa)
            "lightskyblue" -> Color(0x87cefa)
            "lightslategrey" -> Color(0x778899)
            "lightsteelblue" -> Color(0xb0c4de)
            "lightyellow" -> Color(0xffffe0)
            "lime" -> Color(0x00ff00)
            "limegreen" -> Color(0x32cd32)
            "linen" -> Color(0xfaf0e6)
            "magenta" -> Color(0xff00ff)
            "maroon" -> Color(0x800000)
            "mediumaquamarine" -> Color(0x66cdaa)
            "mediumblue" -> Color(0x0000cd)
            "mediumorchid" -> Color(0xba55d3)
            "mediumpurple" -> Color(0x9370db)
            "mediumseagreen" -> Color(0x3cb371)
            "mediumslateblue" -> Color(0x7b68ee)
            "mediumspringgreen" -> Color(0x00fa9a)
            "mediumturquoise" -> Color(0x48d1cc)
            "mediumvioletred" -> Color(0xc71585)
            "midnightblue" -> Color(0x191970)
            "mintcream" -> Color(0xf5fffa)
            "mistyrose" -> Color(0xffe4e1)
            "moccasin" -> Color(0xffe4b5)
            "navajowhite" -> Color(0xffdead)
            "navy" -> Color(0x000080)
            "oldlace" -> Color(0xfdf5e6)
            "olive" -> Color(0x808000)
            "olivedrab" -> Color(0x6b8e23)
            "orange" -> Color(0xffa500)
            "orangered" -> Color(0xff4500)
            "orchid" -> Color(0xda70d6)
            "palegoldenrod" -> Color(0xeee8aa)
            "palegreen" -> Color(0x98fb98)
            "paleturquoise" -> Color(0xafeeee)
            "palevioletred" -> Color(0xdb7093)
            "papayawhip" -> Color(0xffefd5)
            "peachpuff" -> Color(0xffdab9)
            "peru" -> Color(0xcd853f)
            "pink" -> Color(0xffc0cb)
            "plum" -> Color(0xdda0dd)
            "powderblue" -> Color(0xb0e0e6)
            "purple" -> Color(0x800080)
            "rebeccapurple" -> Color(0x663399)
            "red" -> Color(0xff0000)
            "rosybrown" -> Color(0xbc8f8f)
            "royalblue" -> Color(0x4169e1)
            "saddlebrown" -> Color(0x8b4513)
            "salmon" -> Color(0xfa8072)
            "sandybrown" -> Color(0xf4a460)
            "seagreen" -> Color(0x2e8b57)
            "seashell" -> Color(0xfff5ee)
            "sienna" -> Color(0xa0522d)
            "silver" -> Color(0xc0c0c0)
            "skyblue" -> Color(0x87ceeb)
            "slateblue" -> Color(0x6a5acd)
            "slategray" -> Color(0x708090)
            "snow" -> Color(0xfffafa)
            "springgreen" -> Color(0x00ff7f)
            "steelblue" -> Color(0x4682b4)
            "tan" -> Color(0xd2b48c)
            "teal" -> Color(0x008080)
            "thistle" -> Color(0xd8bfd8)
            "tomato" -> Color(0xff6347)
            "turquoise" -> Color(0x40e0d0)
            "violet" -> Color(0xee82ee)
            "wheat" -> Color(0xf5deb3)
            "white" -> Color(0xffffff)
            "whitesmoke" -> Color(0xf5f5f5)
            "yellow" -> Color(0xffff00)
            "yellowgreen" -> Color(0x9acd32)
            else -> null
        }
    }
}
