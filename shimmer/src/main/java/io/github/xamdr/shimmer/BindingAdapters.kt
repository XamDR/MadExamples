package io.github.xamdr.shimmer

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.documentfile.provider.DocumentFile
import java.util.concurrent.Executors

private val executor = Executors.newSingleThreadExecutor()
private val handler = Handler(Looper.getMainLooper())

@BindingAdapter("source", "width", "height", requireAll = true)
fun bindImageSrc(imageView: ImageView, src: Uri?, width: Int, height: Int) {
	if (src != null) {
		val fileName = "${DocumentFile.fromSingleUri(imageView.context, src)?.simpleName}.jpg"
		executor.execute {
			val bitmap = ImageStoreManager.getImageFromInternalStorage(imageView.context, fileName, width, height)
			handler.post {
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap)
				}
			}
		}
	}
	else {
		imageView.setImageBitmap(null)
	}
}