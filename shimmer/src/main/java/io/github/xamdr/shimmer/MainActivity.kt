package io.github.xamdr.shimmer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import io.github.xamdr.shimmer.databinding.ActivityMainBinding

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
		binding.uri = uri
	}

	private fun copyImage(uri: Uri) {
		val fileName = "${DocumentFile.fromSingleUri(this, uri)?.simpleName}.jpg"
		ImageStoreManager.saveToInternalStorage(this, uri, fileName)
	}
}