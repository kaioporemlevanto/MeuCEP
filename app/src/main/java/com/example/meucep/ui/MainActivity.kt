package com.example.meucep.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import com.example.meucep.databinding.ActivityMainBinding
import java.io.Serializable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meucep.data.CepRepository
import com.example.meucep.data.ViaCepService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.meucep.data.CodanteService
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CepViewModel by viewModels {
        // Retrofit 1: ViaCEP
        val viaCepRetrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val viaCepService = viaCepRetrofit.create(ViaCepService::class.java)

        // Retrofit 2: Codante (Bandeiras)
        val codanteRetrofit = Retrofit.Builder()
            .baseUrl("https://apis.codante.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val codanteService = codanteRetrofit.create(CodanteService::class.java)

        // Repositório recebe os dois serviços
        val repository = CepRepository(viaCepService, codanteService)

        CepViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            val cep = binding.edtCep.text.toString().trim()
            if (cep.length >= 8) {
                viewModel.searchCep(cep)
            } else {
                Toast.makeText(this, "Por favor, digite um CEP válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.cepData.observe(this) { cepResponse ->
            cepResponse?.let {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("EXTRA_CEP_DATA", it as Serializable)
                    putExtra("EXTRA_FLAG_URL", viewModel.flagUrl.value)
                }
                startActivity(intent)
            }
        }
        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
    class CepViewModelFactory(private val repository: CepRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CepViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CepViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
