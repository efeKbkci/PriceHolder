package com.tutorial.priceholder.navigator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorial.priceholder.R
import com.tutorial.priceholder.navigator.AllDestinations.CALCULATING
import com.tutorial.priceholder.navigator.AllDestinations.HOME
import com.tutorial.priceholder.navigator.AllDestinations.SETTINGS
import com.tutorial.priceholder.navigator.AllDestinations.STORAGE

@Composable
fun DrawerContent(
    route:String,
    goToSettings: () -> Unit = {},
    goToHome:() -> Unit = {},
    goToStorage:() -> Unit = {},
    goToCalculating: () -> Unit,
    closeDrawer:() -> Unit = {}
){
    ModalDrawerSheet (modifier = Modifier.padding(end = 60.dp)){
        DrawerHeader()
        Divider(Modifier.padding(top=8.dp, bottom = 8.dp, end = 4.dp, start = 4.dp))
        CustomizedItem(currentRoute = route, destinationRoute = HOME, goToHome, closeDrawer)
        CustomizedItem(currentRoute = route, destinationRoute = STORAGE, goToStorage, closeDrawer)
        CustomizedItem(currentRoute = route, destinationRoute = SETTINGS, goToSettings, closeDrawer)
        CustomizedItem(currentRoute = route, destinationRoute = CALCULATING, goToCalculating, closeDrawer)
    }
}

@Composable
fun DrawerHeader(){

    Surface(
        modifier = Modifier.background(Color.Black)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.height(190.dp).fillMaxWidth()
        ){
            Image(
                painterResource(id = R.drawable.diet),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@Composable
fun CustomizedItem(
    currentRoute:String,
    destinationRoute:String,
    navigateFun:()->Unit={},
    closeDrawer:()->Unit={}
){
    val matchIcons = hashMapOf(
        SETTINGS to Icons.Filled.Settings,
        STORAGE to Icons.Filled.Storage,
        HOME to Icons.Filled.Home,
        CALCULATING to Icons.Filled.Calculate
    )

    val matchTexts = hashMapOf(
        SETTINGS to "Ayarlar",
        STORAGE to "Kaydedilenler",
        HOME to "Ana Sayfa",
        CALCULATING to "Ücret Hesaplama"
    )

    val icon: ImageVector = matchIcons[destinationRoute] ?: Icons.Filled.Error
    val text: String = matchTexts[destinationRoute] ?: "Hata"

    NavigationDrawerItem(
        label = {
            Text(
                text = text,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.opensans_semicondensed_italic, weight = FontWeight.Normal))
            )
        },
        selected = currentRoute == destinationRoute,
        onClick = {
            navigateFun()
            closeDrawer()
        },
        icon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier.padding(start = 4.dp,end=4.dp)
    )
}