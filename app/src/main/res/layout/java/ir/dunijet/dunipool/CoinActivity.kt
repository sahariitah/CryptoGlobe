package ir.dunijet.dunipool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.dunijet.dunipool.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding :ActivityCoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView( binding.root )
    }
}