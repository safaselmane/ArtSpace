package com.example.artspace
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ArtSpaceApp()
                }
            }
        }
    }
}
data class Artwork(
    val id: Int,
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val artist: Int,
    val year: Int
)

private val artworks = listOf(
    Artwork(1, R.drawable.art_1, R.string.title_1, R.string.artist_1, 2017),
    Artwork(2, R.drawable.art_2, R.string.title_2, R.string.artist_2, 2021),
    Artwork(3, R.drawable.art_3, R.string.title_3, R.string.artist_3, 2020)
)

@Composable
fun ArtSpaceApp(modifier: Modifier = Modifier) {
    var currentId by rememberSaveable { mutableIntStateOf(1) }
    val lastId = artworks.size

    val artwork = artworks.first { it.id == currentId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()) // évite la troncature en paysage
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ArtworkWall(
            image = artwork.image,
            description = stringResource(artwork.title),
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.height(16.dp))
        ArtworkDescriptor(artwork)
        Spacer(Modifier.height(16.dp))
        DisplayController(
            onPrevious = {
                currentId = when (currentId) {
                    1 -> lastId// première->dernière
                    else -> currentId - 1
                }
            },
            onNext = {
                currentId = when (currentId) {
                    lastId -> 1//dernière->première
                    else -> currentId + 1
                }
            }
        )
    }
}

@Composable
fun ArtworkWall(
    @DrawableRes image: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .widthIn(max = 480.dp) // limite la largeur sur tablette
            .shadow(elevation = 8.dp),
        color = Color.White
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(32.dp)
                .heightIn(max = 480.dp)
        )
    }
}

@Composable
fun ArtworkDescriptor(artwork: Artwork, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .widthIn(max = 480.dp)
            .fillMaxWidth()
            .background(Color(0xFFECEBF4))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(artwork.title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(artwork.artist))
                }
                append(" (${artwork.year})")
            },
            fontSize = 14.sp
        )
    }
}

@Composable
fun DisplayController(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onPrevious, modifier = Modifier.width(140.dp)) {
            Text("Previous")
        }
        Button(onClick = onNext, modifier = Modifier.width(140.dp)) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    MaterialTheme { ArtSpaceApp() }
}