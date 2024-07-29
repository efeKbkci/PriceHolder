package com.tutorial.priceholder.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.tutorial.priceholder.R

@Composable
fun CustomizedTextField(
    modifier: Modifier = Modifier,
    label:String="",
    supportingText:String="",
    value:String,
    leadingIcon:ImageVector?=null,
    trailingIcon:ImageVector?=null,
    onTrailingIcon:()->Unit={},
    justNumbers:Boolean=false,
    isError:Boolean=false,
    enabled:Boolean=true,
    onValueChange:(String)->Unit
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(text = label, Modifier.background(Color.Transparent), fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium))) },
        supportingText = { Text(text = supportingText, modifier = Modifier.background(Color.Transparent), fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium))) },
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = leadingIcon, contentDescription = "") }
        },
        trailingIcon = trailingIcon?.let{
            { Icon(imageVector = trailingIcon, contentDescription = "", modifier = Modifier.clickable { if (!isError) onTrailingIcon() }) }
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium))
        ),
        isError = isError,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = if (justNumbers) KeyboardType.Number else KeyboardType.Unspecified)
    )
}