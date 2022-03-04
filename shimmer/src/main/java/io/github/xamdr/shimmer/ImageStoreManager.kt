package io.github.xamdr.shimmer

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileNotFoundException

object ImageStoreManager {

	fun saveToInternalStorage(context: Context, uri: Uri, fileName: String): String {
		context.openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
			context.contentResolver.openInputStream(uri).use {
				val buffer = ByteArray(1024)
				var read = it?.read(buffer) ?: -1
				while (read != -1) {
					fos.write(buffer, 0, read)
					read = it?.read(buffer) ?: -1
				}
				fos.flush()
			}
		}
		return context.filesDir.absolutePath
	}

	fun getImageFromInternalStorage(context: Context, fileName: String, width: Int, height: Int): Bitmap? {
		val directory = context.filesDir
		val file = File(directory, fileName)
		return try {
			ImageHelper.getBitmapFromUri(context, file.toUri(), width, height)
		}
		catch (e: FileNotFoundException) {
			null
		}
	}

//	fun deleteImageFromInternalStorage(context: Context, fileName: String): Boolean {
//		val dir = context.filesDir
//		val file = File(dir, fileName)
//		return file.delete()
//	}
}