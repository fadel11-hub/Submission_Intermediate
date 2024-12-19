package com.dicoding.picodiploma.loginwithanimation.view.detailStory

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.main.ItemStory
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import kotlinx.coroutines.launch

class detailStory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: detailStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup UI dan ViewModel
        setupView()
        setupViewModel()

        // Mengambil token dan ID dari intent
        val token = intent.getStringExtra(EXTRA_TOKEN)
        val id = intent.getStringExtra(EXTRA_ID)

        // Validasi token dan ID, lalu ambil data detail
        if (token != null && id != null) {
            fetchStoryDetails(id, token)
        } else {
            showErrorDialog(getString(R.string.gagal_memuat_data))
            navigateToMainActivity()
        }
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.detail_story)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(detailStoryViewModel::class.java)

        // Observasi data cerita
        viewModel.getDetailStory().observe(this, { story ->
            if (story != null) {
                displayStoryDetails(story)
            } else {
                showErrorDialog(getString(R.string.gagal_memuat_data))
            }
        })

        // Observasi pesan error
        viewModel.getErrorMessage().observe(this, { errorMessage ->
            errorMessage?.let {
                showErrorDialog(it)
                showLoading(false)
            }
        })
    }

    private fun fetchStoryDetails(id: String, token: String) {
        showLoading(true)
        lifecycleScope.launch {
            viewModel.setDetailStory(id, "Bearer $token")
        }
    }

    private fun displayStoryDetails(story: ItemStory) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            Glide.with(this@detailStory)
                .load(story.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivDetailPhoto)
            showLoading(false)
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this@detailStory).apply {
            setTitle(R.string.gagal_memuat)
            setMessage(message)
            setPositiveButton(R.string.oke) { _, _ -> }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TOKEN = "extra_token"
    }
}
