package com.dicoding.picodiploma.loginwithanimation.view.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
//            showLoading(true)

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, R.string.email_password_required, Toast.LENGTH_SHORT).show()
//                showLoading(false)
                return@setOnClickListener
            }

            viewModel.login(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    showLoading(false)

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && !loginResponse.error && loginResponse.loginResult != null) {
                            val loginResult = loginResponse.loginResult
                            val user = UserModel(loginResult.userId, loginResult.name, loginResult.token)
                            viewModel.saveSession(user)
                            navigateToMainActivity()
                        } else {
                            showErrorDialog(getString(R.string.invalid_login_result))
                        }
                    } else {
                        showErrorDialog(getString(R.string.username_password_salah))
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    showLoading(false)
                    showErrorDialog(getString(R.string.network_error, t.message))
                }
            })
        }
    }

//    private fun showLoading(state: Boolean) {
//        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
//    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this@LoginActivity).apply {
            setTitle(R.string.login_failed)
            setMessage(message)
            setPositiveButton(R.string.oke) { _, _ -> }
            create()
            show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
