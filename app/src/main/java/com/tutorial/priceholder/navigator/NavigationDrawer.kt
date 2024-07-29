package com.tutorial.priceholder.navigator

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tutorial.priceholder.R
import com.tutorial.priceholder.StateViewModel
import com.tutorial.priceholder.screens.CalculatingScreen
import com.tutorial.priceholder.screens.DataScreen
import com.tutorial.priceholder.screens.HomeScreen
import com.tutorial.priceholder.screens.SettingsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizedNavigationDrawer(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: StateViewModel
){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: AllDestinations.HOME
    val navObject = remember(navController){ NavigateInMyApp(navController) }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                route = currentRoute,
                goToHome = {
                    navObject.navigateToHome()
                },
                goToSettings = { navObject.navigateToSettings() },
                goToStorage = { navObject.navigateToStorage() },
                goToCalculating = { navObject.navigateToCalculating() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Yemek Listesi",
                            fontSize = 28.sp,
                            fontFamily = FontFamily(Font(R.font.opensans_semicondensed_bold)),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom=5.dp, start = 1.dp)
                        )
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF191C1E),
                        titleContentColor = Color(0xFFE6E0E9)
                    ),

                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                        ) {
                            Icon(Icons.Filled.Menu, null, tint = Color.White)
                        }
                    }
                )
            }
        ) {
            NavHost(navController = navController, startDestination = AllDestinations.HOME) {
                composable(AllDestinations.HOME) { HomeScreen(viewModel = viewModel) }
                composable(AllDestinations.SETTINGS) { SettingsScreen(viewModel = viewModel) }
                composable(AllDestinations.STORAGE) { DataScreen(viewModel) }
                composable(AllDestinations.CALCULATING) { CalculatingScreen(viewModel) }
            }
        }
    }
}