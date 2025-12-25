package com.jeremylakeyjr.lanbulab.service

import com.jeremylakeyjr.lanbulab.data.model.MakerWorldModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class MakerWorldService {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()
    
    // Mock data for demonstration - in production this would call actual MakerWorld API
    suspend fun searchModels(query: String): List<MakerWorldModel> = withContext(Dispatchers.IO) {
        listOf(
            MakerWorldModel(
                id = "1",
                name = "Sample Model 1",
                author = "MakerWorld Creator",
                thumbnailUrl = "https://example.com/thumb1.jpg",
                downloadUrl = "https://example.com/model1.3mf",
                description = "A sample 3D model from MakerWorld",
                tags = listOf("utility", "home"),
                likes = 150,
                downloads = 500
            ),
            MakerWorldModel(
                id = "2",
                name = "Sample Model 2",
                author = "Another Creator",
                thumbnailUrl = "https://example.com/thumb2.jpg",
                downloadUrl = "https://example.com/model2.3mf",
                description = "Another great model",
                tags = listOf("toy", "fun"),
                likes = 200,
                downloads = 800
            )
        )
    }
    
    suspend fun getPopularModels(): List<MakerWorldModel> = withContext(Dispatchers.IO) {
        searchModels("")
    }
    
    suspend fun downloadModel(model: MakerWorldModel, destinationDir: File): File? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(model.downloadUrl)
                .build()
            
            val response = client.newCall(request).execute()
            
            if (response.isSuccessful) {
                val fileName = "${model.id}_${model.name}.3mf"
                val file = File(destinationDir, fileName)
                
                response.body?.byteStream()?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                
                file
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun getModelDetails(modelId: String): MakerWorldModel? = withContext(Dispatchers.IO) {
        // In production, fetch from actual API
        searchModels("").firstOrNull { it.id == modelId }
    }
}
