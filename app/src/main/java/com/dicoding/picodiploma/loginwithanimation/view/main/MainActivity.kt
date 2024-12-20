package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.response.main.ItemStory
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.addStory.AddStory
import com.dicoding.picodiploma.loginwithanimation.view.auth.setting.Setting
import com.dicoding.picodiploma.loginwithanimation.view.detailStory.detailStory
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                lifecycleScope.launch {
                    val token = UserPreference.getInstance(dataStore).getToken()
                    viewModel.getAllStories("Bearer $token")
                    showLoading(true)
                }
            }
        }
        adapter = MainAdapter()
        adapter.notifyDataSetChanged()
        viewModel.getListStories().observe(this, {
            if (it != null) {
                adapter.setList(it)
            } else {
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(R.string.gagal_memuat_data)
                    setMessage(R.string.gagal_memuat_data)
                    setPositiveButton(R.string.oke) { _, _ ->
                    }
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    showLoading(false)
                }
            }

            showLoading(false)
        })

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickcallBack {
            override fun onItemClicked(data: ItemStory) {
                lifecycleScope.launch {
                    val token = UserPreference.getInstance(dataStore).getToken()
                    Intent(this@MainActivity, detailStory::class.java).also {
                        it.putExtra(detailStory.EXTRA_ID, data.id)
                        it.putExtra(detailStory.EXTRA_TOKEN, token)
                        startActivity(it)
                    }
                }
            }
        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        binding.fabNewStory.setOnClickListener {
            lifecycleScope.launch {
                val token = UserPreference.getInstance(dataStore).getToken()
                Intent(this@MainActivity, AddStory::class.java).also {
                    it.putExtra(AddStory.EXTRA_TOKEN, token)
                    startActivity(it)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this, Setting::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }





    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
