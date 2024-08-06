package com.example.submissionjetpackcompose.data

import com.example.submissionjetpackcompose.model.Pokemon
import com.example.submissionjetpackcompose.model.PokemonsData

class PokemonRepository {
    fun getHeroes(): List<Pokemon> {
        return PokemonsData.pokemons
    }

    fun searchPokemons(query: String): List<Pokemon> {
        return PokemonsData.pokemons.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    fun getPokemonById(PokemonnID: String): Pokemon {
         val data = PokemonsData.pokemons.filter {
            it.id.contains(PokemonnID, ignoreCase = true)
        }
        return data[0]
    }

    companion object {
        @Volatile
        private var instance: PokemonRepository? = null

        fun getInstance(): PokemonRepository =
            instance ?: synchronized(this) {
                PokemonRepository().apply {
                    instance = this
                }
            }
    }

}