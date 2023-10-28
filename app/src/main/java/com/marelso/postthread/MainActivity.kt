package com.marelso.postthread

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.marelso.postthread.ui.PostDetailViewModel
import com.marelso.postthread.ui.PostListViewModel
import com.marelso.postthread.ui.PostList
import com.marelso.postthread.ui.PostUiState
import com.marelso.postthread.ui.Screen
import com.marelso.postthread.ui.setupNavGraph
import com.marelso.postthread.ui.theme.PostThreadTheme
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostThreadTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    setupNavGraph(navController = navController)
                }
            }
        }
    }
}


@Composable
fun HomeScreen(
    viewModel: PostListViewModel = getViewModel(),
    navHostController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PostList(
            posts = viewModel.pagingData.collectAsLazyPagingItems(),
            onClick = {
                goToDetail(it, navHostController)
            }
        )
        FloatingActionButton(
            onClick = {
                // Handle FloatingActionButton click here
            },
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.BottomEnd)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

fun goToDetail(reference: Int, navHostController: NavHostController) {
    navHostController.navigate(route = Screen.Detail.setId(reference))
}

@Composable
fun PostDetailScreen(viewModel: PostDetailViewModel) {
    viewModel.fetchContent()

    val postUiState = viewModel.postUiState.collectAsState()

    when (postUiState.value) {
        is PostUiState.Loading -> Text(text = "Loading")

        is PostUiState.Error -> ErrorScreen("Something went wrong", "Resource not found")

        is PostUiState.Success -> {
            val post = (postUiState.value as PostUiState.Success).post
        }
    }
}

@Composable
fun ErrorScreen(headline: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.graphicsLayer(scaleY = 2f, scaleX = 2f),
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(id = R.string.warning)
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = headline,
            style = typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = subtitle,
            style = typography.displayLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        )
    }
}