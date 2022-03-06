package io.github.xamdr.shimmer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

object ImageHelper {

	fun getBitmapFromUri(context: Context, uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
		val bitmap: Bitmap?
		try {
			var inputStream = context.contentResolver.openInputStream(uri)
			val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
			BitmapFactory.decodeStream(inputStream, null, options)
			inputStream?.close()
			inputStream = context.contentResolver.openInputStream(uri)
			options.apply {
				inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
				inJustDecodeBounds = false
			}
			bitmap = BitmapFactory.decodeStream(inputStream, null, options)
			inputStream?.close()
			return bitmap
		}
		catch (e: FileNotFoundException) {
			printError("EXCEPTION", e.message)
			return null
		}
	}

	private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
		// Raw height and width of image
		val (height: Int, width: Int) = options.run { outHeight to outWidth }
		var inSampleSize = 1

		if (height > reqHeight || width > reqWidth) {
			val halfHeight: Int = height / 2
			val halfWidth: Int = width / 2

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
				inSampleSize *= 2
			}
		}
		return inSampleSize
	}
}