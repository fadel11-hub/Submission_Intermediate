package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.auth.setting.Setting
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        adapter = MainAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin || user.token.isEmpty()) {
                navigateToLogin()
            } else {
                val token = "Bearer ${user.token}"
                viewModel.getAllStories(token)
            }
        }

        viewModel.listStories.observe(this) { stories ->
            adapter.setList(ArrayList(stories))
        }

        viewModel.errorMessage.observe(this) { message ->
            showErrorDialog(message)
        }
    }

    private fun navigateToLogin() {
        Intent(this, WelcomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.gagal_memuat_data))
            setMessage(message)
            setPositiveButton(R.string.oke, null)
            create()
            show()
        }
    }
}
