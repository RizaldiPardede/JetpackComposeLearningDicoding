package com.example.submissionjetpackcompose.navigation

sealed class Screen(val route: String){
    object Home : Screen("home")
    object Profile : Screen("profile")
    object DetailPokemon : Screen("home/{pokemonId}") {
        fun createRoute(pokemonId: String) = "home/$pokemonId"
    }
}
