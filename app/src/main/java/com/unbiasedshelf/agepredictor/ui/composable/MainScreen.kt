package com.unbiasedshelf.agepredictor.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unbiasedshelf.agepredictor.R
import com.unbiasedshelf.agepredictor.ui.composable.favorites.FavoritesScreen
import com.unbiasedshelf.agepredictor.ui.composable.home.HomeScreen
import com.unbiasedshelf.agepredictor.ui.theme.BottomNavActiveGray
import com.unbiasedshelf.agepredictor.ui.theme.BottomNavInactiveGray

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            AgifyBottomAppBar(
                currentDestination = navBackStackEntry?.destination?.route,
                onItemClick = { destination ->
                    navController.navigate(destination) {
                        if (destination == Route.Home) {
                            popUpTo(Route.Home) { inclusive = true }
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.padding(it)
        ) {
            composable(Route.Home) { HomeScreen() }
            composable(Route.Favorites) { FavoritesScreen() }
        }
    }
}

@Composable
private fun AgifyBottomAppBar(currentDestination: String?, onItemClick: (String) -> Unit) {
    BottomAppBar(
        contentColor = MaterialTheme.colors.primary,
        modifier = Modifier.height(95.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            AppBarItem(
                painter = painterResource(id = R.drawable.ic_home),
                label = stringResource(R.string.home),
                currentDestination = currentDestination,
                route = Route.Home,
                onClick = onItemClick
            )

            AppBarItem(
                painter = painterResource(id = R.drawable.ic_favorites),
                label = stringResource(R.string.favorites),
                currentDestination = currentDestination,
                route = Route.Favorites,
                onClick = onItemClick
            )
        }
    }
}

@Composable
private fun AppBarItem(
    painter: Painter,
    label: String,
    route: String,
    currentDestination: String?,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick(route) }
    ) {
        val color by remember(currentDestination) {
            val isActive = route == currentDestination
            mutableStateOf(if (isActive) BottomNavActiveGray else BottomNavInactiveGray)
        }

        Icon(
            painter = painter,
            contentDescription = label,
            tint = color,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = color
        )
    }
}

private object Route {
    const val Home = "home"
    const val Favorites = "favorites"
}
