package com.sam.quickkeys.ui.screens.splash

import android.graphics.drawable.Animatable

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }

    // Start animation and navigate after 2 seconds
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(2000)
        navController.navigate(ROUT_REGISTER) {
            popUpTo(0) // Removes SplashScreen from back stack
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_car_logo), // Replace with your car logo in res/drawable
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "QuickKeys",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Drive Your Way",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
        }
    }
}

annotation class Composable
