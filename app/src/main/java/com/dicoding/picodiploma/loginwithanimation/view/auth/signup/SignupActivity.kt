package com.dicoding.picodiploma.loginwithanimation.view.auth.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.SignupResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.auth.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            showLoading(true)
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.register(name, email, password).enqueue(object : Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                ) {
                    if (response.isSuccessful) {
                        val signupResponse = response.body()
                        if (signupResponse != null && !signupResponse.error!!) {
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("Yeah!")
                                setMessage("Akun dengan $email sudah jadi. Silahkan login terlebih dahulu")
                                setPositiveButton(R.string.oke) { _, _ ->
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                                showLoading(false)
                            }
                        } else {
                            showLoading(false)
                            Toast.makeText(
                                this@SignupActivity,
                                R.string.signup_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        AlertDialog.Builder(this@SignupActivity).apply {
                            setTitle(R.string.signup_failed)
                            setMessage(R.string.recheck_the_data)
                            setPositiveButton(R.string.oke) { _, _ ->
                            }
                            create()
                            show()
                        }
                        showLoading(false)
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    showLoading(false)
                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle(R.string.signup_failed)
                        setMessage(R.string.gagal_memuat_data)
                        setPositiveButton(R.string.oke) { _, _ ->
                        }
                        create()
                        show()
                        showLoading(false)
                    }
                }
            })
        }
    }

        private fun playAnimation() {
            ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title =
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
            val nameTextView =
                ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
            val nameEditTextLayout =
                ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
            val emailTextView =
                ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
            val emailEditTextLayout =
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
            val passwordTextView =
                ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
            val passwordEditTextLayout =
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f)
                    .setDuration(100)
            val signup =
                ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

            AnimatorSet().apply {
                playSequentially(
                    title,
                    nameTextView,
                    nameEditTextLayout,
                    emailTextView,
                    emailEditTextLayout,
                    passwordTextView,
                )
                startDelay = 100
            }.start()
        }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
