package com.example.xpen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.xpen.ui.entry.EntryScreen
import com.example.xpen.ui.history.HistoryScreen
import com.example.xpen.util.Screen
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XpenApp() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Screen.Entry.route

    val historyScreens = listOf(
        Screen.Today, Screen.ThisWeek, Screen.ThisMonth, Screen.LastMonth, Screen.Last3Months
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text("Xpen", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Entry") },
                    selected = currentRoute == Screen.Entry.route,
                    onClick = {
                        navController.navigate(Screen.Entry.route)
                        scope.launch { drawerState.close() }
                    }
                )
                HorizontalDivider()
                historyScreens.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route)
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        val title = (historyScreens + Screen.Entry).firstOrNull { it.route == currentRoute }?.title ?: "Xpen"
                        Text(title)
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Entry.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Entry.route) { EntryScreen() }
                composable(Screen.Today.route) { HistoryScreen(screen = Screen.Today) }
                composable(Screen.ThisWeek.route) { HistoryScreen(screen = Screen.ThisWeek) }
                composable(Screen.ThisMonth.route) { HistoryScreen(screen = Screen.ThisMonth) }
                composable(Screen.LastMonth.route) { HistoryScreen(screen = Screen.LastMonth) }
                composable(Screen.Last3Months.route) { HistoryScreen(screen = Screen.Last3Months) }
            }
        }
    }
}