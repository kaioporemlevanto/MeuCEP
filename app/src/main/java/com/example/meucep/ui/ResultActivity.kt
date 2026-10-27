package com.example.meucep.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meucep.R
import com.example.meucep.databinding.ActivityResultBinding
import com.example.meucep.model.CepResponse
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cepData = intent.getSerializableExtra("EXTRA_CEP_DATA") as? CepResponse
        val flagUrl = intent.getStringExtra("EXTRA_FLAG_URL")

        if (cepData != null) {
            populateFields(cepData, flagUrl)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getMapDrawableId(uf: String): Int {
        return when (uf.lowercase()) {
            "ac" -> R.raw.acre_in_brazil
            "al" -> R.raw.alagoas_in_brazil
            "am" -> R.raw.amazonas_in_brazil
            "ap" -> R.raw.amapa_in_brazil
            "ba" -> R.raw.bahia_in_brazil
            "ce" -> R.raw.ceara_in_brazil
            "df" -> R.raw.distrito_federal_in_brazil
            "es" -> R.raw.espirito_santo_in_brazil
            "go" -> R.raw.goias_in_brazil
            "ma" -> R.raw.maranhao_in_brazil
            "mg" -> R.raw.minas_gerais_in_brazil
            "ms" -> R.raw.mato_grosso_do_sul_in_brazil
            "mt" -> R.raw.mato_grosso_in_brazil
            "pa" -> R.raw.para_in_brazil
            "pb" -> R.raw.paraiba_in_brazil
            "pe" -> R.raw.pernambuco_in_brazil
            "pi" -> R.raw.piaui_in_brazil
            "pr" -> R.raw.parana_in_brazil
            "rj" -> R.raw.rio_de_janeiro_in_brazil
            "rn" -> R.raw.rio_grande_do_norte_in_brazil
            "ro" -> R.raw.rondonia_in_brazil
            "rr" -> R.raw.roraima_in_brazil
            "rs" -> R.raw.rio_grande_do_sul_in_brazil
            "sc" -> R.raw.santa_catarina_in_brazil
            "se" -> R.raw.sergipe_in_brazil
            "sp" -> R.raw.sao_paulo_in_brazil
            "to" -> R.raw.tocantins_in_brazil
            else -> 0 // Retorna 0 caso a UF não seja encontrada (não carregará imagem)
        }
    }

    private fun populateFields(cep: CepResponse, flagUrl: String?) {
        binding.txtResultCep.text = "${getString(R.string.lbl_cep)} ${cep.cep}"
        binding.txtResultStreet.text = "${getString(R.string.lbl_street)} ${cep.logradouro}"
        binding.txtResultNeighborhood.text = "${getString(R.string.lbl_neighborhood)} ${cep.bairro}"
        binding.txtResultCity.text = "${getString(R.string.lbl_city)} ${cep.localidade} - ${cep.uf}"
        binding.txtStateName.text = cep.uf
        binding.txtResultRegion.text = cep.regiao
        binding.txtResultDdd.text = cep.ddd
        binding.txtResultIbge.text = cep.ibge

        flagUrl?.let { url ->
            val imageLoader = ImageLoader.Builder(this)
                .components { add(SvgDecoder.Factory()) }
                .build()

            binding.imgStateFlag.load(url, imageLoader) {
                crossfade(true)
            }
        }
        val mapImageId = getMapDrawableId(cep.uf)

        if (mapImageId != 0) {
            // Construtor do Coil configurado para ler SVG
            val svgImageLoader = ImageLoader.Builder(this)
                .components { add(SvgDecoder.Factory()) }
                .build()

            // Carrega o ficheiro SVG bruto do R.raw diretamente no ImageView
            binding.imgMap.load(mapImageId, svgImageLoader) {
                crossfade(true)
            }
        }
    }
}
