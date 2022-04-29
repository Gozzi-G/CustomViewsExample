package com.prud.customviews

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prud.customviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.bottomButtons.setListener {
            if (it == BottomButtonAction.POSITIVE) {
                binding.bottomButtons.isProgressMode = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    handler.postDelayed({
                        binding.bottomButtons.isProgressMode = false
                        binding.bottomButtons.setPositiveButtonText("Update Ok")
                        Toast.makeText(this, "POSITIVE", Toast.LENGTH_SHORT).show()
                    }, token, 2000)
                }

            } else if (it == BottomButtonAction.NEGATIVE) {
                binding.bottomButtons.setNegativeButtonText("Update Cancel")
                Toast.makeText(this, "NEGATIVE", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(token)
    }
}