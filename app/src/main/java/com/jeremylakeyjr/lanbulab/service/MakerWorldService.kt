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
    
    // Better sample data for demonstration
    suspend fun searchModels(query: String): List<MakerWorldModel> = withContext(Dispatchers.IO) {
        val allModels = listOf(
            MakerWorldModel(
                id = "1",
                name = "Phone Stand",
                author = "MakerWorld",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/phone_stand.3mf",
                description = "Adjustable phone stand for desk",
                tags = listOf("utility", "office"),
                likes = 523,
                downloads = 1842
            ),
            MakerWorldModel(
                id = "2",
                name = "Cable Organizer",
                author = "TechPrints",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/cable_organizer.3mf",
                description = "Keep your cables organized",
                tags = listOf("utility", "organization"),
                likes = 412,
                downloads = 1234
            ),
            MakerWorldModel(
                id = "3",
                name = "Desk Organizer",
                author = "DesignHub",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/desk_organizer.3mf",
                description = "Multi-compartment desk organizer",
                tags = listOf("utility", "office"),
                likes = 678,
                downloads = 2341
            ),
            MakerWorldModel(
                id = "4",
                name = "Plant Pot",
                author = "GreenPrints",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/plant_pot.3mf",
                description = "Modern geometric plant pot",
                tags = listOf("home", "decor"),
                likes = 891,
                downloads = 3456
            ),
            MakerWorldModel(
                id = "5",
                name = "Pencil Holder",
                author = "OfficePrints",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/pencil_holder.3mf",
                description = "Stylish pencil and pen holder",
                tags = listOf("utility", "office"),
                likes = 234,
                downloads = 891
            ),
            MakerWorldModel(
                id = "6",
                name = "Coaster Set",
                author = "HomeMaker",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/coaster.3mf",
                description = "Hexagonal coaster design",
                tags = listOf("home", "utility"),
                likes = 567,
                downloads = 1567
            ),
            MakerWorldModel(
                id = "7",
                name = "Key Holder",
                author = "EntryOrganizer",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/key_holder.3mf",
                description = "Wall-mounted key holder",
                tags = listOf("home", "organization"),
                likes = 445,
                downloads = 1123
            ),
            MakerWorldModel(
                id = "8",
                name = "Headphone Stand",
                author = "AudioAccessories",
                thumbnailUrl = "",
                downloadUrl = "https://example.com/headphone_stand.3mf",
                description = "Elegant headphone stand",
                tags = listOf("utility", "tech"),
                likes = 789,
                downloads = 2234
            )
        )
        
        if (query.isEmpty()) {
            allModels
        } else {
            allModels.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true) ||
                it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }
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
