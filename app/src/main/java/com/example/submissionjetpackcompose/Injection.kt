package com.example.submissionjetpackcompose

import com.example.submissionjetpackcompose.data.PokemonRepository

object Injection {
    fun provideRepository(): PokemonRepository {
        return PokemonRepository.getInstance()
    }
}