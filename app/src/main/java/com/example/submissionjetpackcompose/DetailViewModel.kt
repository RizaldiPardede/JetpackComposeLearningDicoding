package com.example.submissionjetpackcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.submissionjetpackcompose.data.PokemonRepository
import com.example.submissionjetpackcompose.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel (private val repository: PokemonRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Pokemon>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Pokemon>>
        get() = _uiState

    fun getPokemonById(PokemonId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getPokemonById(PokemonId))
        }
    }

    class ViewModelDetailFactory(private val repository: PokemonRepository) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }


}
