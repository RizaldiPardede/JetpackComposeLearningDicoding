package com.example.submissionjetpackcompose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionjetpackcompose.data.PokemonRepository
import com.example.submissionjetpackcompose.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PokemonsViewModel (private val repository: PokemonRepository) : ViewModel(){
    private val _groupedPokemons = MutableStateFlow(
        repository.getHeroes()
            .sortedBy { it.name }
            .groupBy { it.name[0] }
    )
    val groupedPokemons: StateFlow<Map<Char,List<Pokemon>>>get() = _groupedPokemons

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(Query :String){
        _query.value = Query
        _groupedPokemons.value = repository.searchPokemons(_query.value).sortedBy { it.name }.groupBy { it.name[0] }
    }



}



class ViewModelFactory(private val repository: PokemonRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonsViewModel::class.java)) {
            return PokemonsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}