package com.example.meucep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meucep.data.CepRepository
import com.example.meucep.model.CepResponse
import kotlinx.coroutines.launch

class CepViewModel(private val repository: CepRepository) : ViewModel() {
    private val _cepData = MutableLiveData<CepResponse?>()
    val cepData: LiveData<CepResponse?> = _cepData

    private val _flagUrl = MutableLiveData<String>()
    val flagUrl: LiveData<String> = _flagUrl

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun searchCep(cep: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchCep(cep)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    _cepData.postValue(data)

                    // API de Bandeiras: Concatena a UF (em letras minúsculas) para obter a imagem
                    // Exemplo usando o repositório público de bandeiras do Brasil:
                    val ufLower = data!!.uf.lowercase()
                    _flagUrl.postValue("https://githubusercontent.com{ufLower}.png")
                } else {
                    _error.postValue("CEP não encontrado.")
                }
            } catch (e: Exception) {
                _error.postValue("Erro de conexão.")
            }
        }
    }
}
