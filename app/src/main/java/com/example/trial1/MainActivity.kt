package com.example.trial1

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.trial1.ui.theme.Trial1Theme


data class Wallpaper(
    val id: Int,
    val resourceId: Int,
    val title: String
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Trial1Theme {
                WallpaperApp()
                }
            }
        }
    }



@Composable
fun WallpaperApp() {
    val context = LocalContext.current
    val wallpapers = listOf(
        Wallpaper(1, R.drawable.wallpaper1, "Mountain Sunset"),
        // Other wallpapers
    )

    var selectedWallpaper by remember { mutableStateOf<Wallpaper?>(null) }

    MaterialTheme {
        Column {
            if (selectedWallpaper == null) {
                WallpaperGrid(wallpapers) { wallpaper ->
                    selectedWallpaper = wallpaper
                }
            } else {
                WallpaperDetailScreen(
                    wallpaper = selectedWallpaper!!,
                    onBackClick = { selectedWallpaper = null }
                )
            }
        }
    }
}


@Composable
fun WallpaperGrid(
    wallpapers: List<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(wallpapers) { wallpaper ->
            WallpaperGridItem(wallpaper, onWallpaperClick)
        }
    }
}

@Composable
fun WallpaperGridItem(
    wallpaper: Wallpaper,
    onWallpaperClick: (Wallpaper) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable { onWallpaperClick(wallpaper) }
    ) {
        Box {
            Image(
                painter = painterResource(id = wallpaper.resourceId),
                contentDescription = wallpaper.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = wallpaper.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.run {
                    align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                        .padding(4.dp)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperDetailScreen(
    wallpaper: Wallpaper,
    onBackClick: () -> Unit,

) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(wallpaper.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = wallpaper.resourceId),
                contentDescription = wallpaper.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
               Button(onClick = {
                   SetWallpaper(
                       context = context,
                       resourceId = wallpaper.resourceId
                   )
               }) { Text("Set Wallpaper")}
            }
        }
    }
}


 fun SetWallpaper(context: Context, resourceId: Int) : Unit {
    try {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        wallpaperManager.setBitmap(bitmap)
        Toast.makeText(context, "Wallpaper set successfully", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
    }
}
