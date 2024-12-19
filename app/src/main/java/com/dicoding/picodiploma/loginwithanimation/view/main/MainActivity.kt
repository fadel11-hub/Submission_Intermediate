package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R

import com.dicoding.picodiploma.loginwithanimation.data.response.main.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.addStory.AddStory
import com.dicoding.picodiploma.loginwithanimation.view.auth.setting.Setting
import com.dicoding.picodiploma.loginwithanimation.view.detailStory.detailStory
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
        setupFabAction()
    }

    private fun setupRecyclerView() {
        adapter = MainAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickcallBack {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, detailStory::class.java).also {
                    it.putExtra(detailStory.EXTRA_ID, data.id)
                    startActivity(it)
                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                // Arahkan pengguna ke halaman login
                Intent(this, WelcomeActivity::class.java).also {
                    startActivity(it)
                }
                finish() // Tutup MainActivity
            } else {
                val token = "Bearer ${user.token}"
                viewModel.getAllStories(token)
            }
        }


        viewModel.listStories.observe(this) { stories ->
            adapter.setList(ArrayList(stories))
            showLoading(false)
        }

        viewModel.errorMessage.observe(this) { message ->
            showErrorDialog(message)
            showLoading(false)
        }

        showLoading(true)
    }

    private fun setupFabAction() {
        binding.fabNewStory.setOnClickListener {
            Intent(this, AddStory::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(this, Setting::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
