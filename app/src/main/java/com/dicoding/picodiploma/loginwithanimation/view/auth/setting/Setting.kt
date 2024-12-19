package com.dicoding.picodiploma.loginwithanimation.view.auth.setting

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySettingBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class Setting : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val viewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupView()


        viewModel.logoutSuccess.observe(this) { success ->
            if (success) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        playAnimation()
    }

    private fun setupAction() {
        binding.cardViewChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.cardViewLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun playAnimation() {

        val changeLanguage = ObjectAnimator.ofFloat(binding.cardViewChangeLanguage, View.ALPHA, 1f).setDuration(200)
        val logout = ObjectAnimator.ofFloat(binding.cardViewLogout, View.ALPHA, 1f).setDuration(200)


        AnimatorSet().apply {
            playSequentially(changeLanguage, logout)
            start()
        }
    }
}