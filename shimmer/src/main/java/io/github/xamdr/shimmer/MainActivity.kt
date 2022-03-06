package io.github.xamdr.shimmer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import io.github.xamdr.shimmer.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

	private val binding by lazy(LazyThreadSafetyMode.NONE) {
		ActivityMainBinding.inflate(layoutInflater)
	}
	private val imageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
		if (uri != null) {
			addImage(uri)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}

	override fun onStart() {
		super.onStart()
		binding.btnSelectImage.setOnClickListener { imageLauncher.launch(arrayOf("image/*")) }
	}

	private fun addImage(uri: Uri) {
		binding.shimmerContainer.isVisible = true
		copyImage(uri)
	}

	private fun copyImage(uri: Uri) {
		lifecycleScope.launch {
			val fileName = "${DocumentFile.fromSingleUri(this@MainActivity, uri)?.simpleName}.jpg"
			val result = ImageStoreManager.saveToInternalStorage(this@MainActivity, uri, fileName)

			if (result.isNotEmpty()) {
				binding.shimmerContainer.isVisible = false
				binding.uri = uri
				binding.image.isVisible = true
			}
		}
	}
}