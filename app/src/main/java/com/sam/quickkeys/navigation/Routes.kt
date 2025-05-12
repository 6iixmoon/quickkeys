package com.sam.quickkeys.navigation

const val ROUT_HOME = "home"
const val ROUT_REGISTER = "register"
const val ROUT_LOGIN = "login"
const val ROUT_ADMIN = "admin"
const val ROUT_PROFILE = "profile/{userId}"
const val ROUT_CAR = "car/{carId}"
const val ROUT_BOOKING = "booking/{carId}/{userId}"
const val ROUT_ABOUT = "about"
const val ROUT_SPLASH = "splash"

// Helper function for dynamic route generation
fun getCarRoute(carId: Int): String = "car/$carId"
