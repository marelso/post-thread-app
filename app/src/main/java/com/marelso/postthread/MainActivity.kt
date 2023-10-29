@file:OptIn(ExperimentalMaterial3Api::class)

package com.marelso.postthread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.marelso.postthread.ui.CreatePostForm
import com.marelso.postthread.ui.CreatePostViewModel
import com.marelso.postthread.ui.PostDetail
import com.marelso.postthread.ui.PostDetailLoading
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
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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
    viewModel: PostListViewModel = getViewModel(), navHostController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PostList(posts = viewModel.pagingData.collectAsLazyPagingItems(), onClick = {
            goToDetail(it, navHostController)
        })
        FloatingActionButton(
            onClick = {
                navHostController.navigate(Screen.Create.route)
            }, modifier = Modifier
                .padding(vertical = 16.dp)
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
fun PostDetailScreen(
    viewModel: PostDetailViewModel, navHostController: NavHostController
) {
    val postUiState = viewModel.postUiState.collectAsState()

    when (postUiState.value) {
        is PostUiState.Loading -> PostDetailLoading()

        is PostUiState.Error -> {
            val error = (postUiState.value as PostUiState.Error)
            PopError(
                headline = error.headline,
                subtitle = error.subtitle,
                onClick = {
                    refresh(viewModel)
                })
        }

        is PostUiState.Success -> {
            val post = (postUiState.value as PostUiState.Success).post

            PostDetail(
                post = post,
                goBack = { goBack(navHostController, Screen.Home) },
                updateStatus = {
                    viewModel.changePostStatus(it)
                },
                delete = {
                    viewModel.deletePost()
                }
            )
        }

        is PostUiState.Delete -> {
            DeleteCompleted(
                onClick = {
                    goBack(navHostController, Screen.Home)
                })
        }
    }
}

@Composable
fun DeleteCompleted(onClick: ((Unit) -> Unit)) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.graphicsLayer(scaleY = 2f, scaleX = 2f),
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(id = R.string.warning),
                tint = Color(android.graphics.Color.parseColor("#FF5252"))
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Resource successfully deleted",
                style = typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "You can now go to the home",
                style = typography.displayLarge.copy(
                    fontWeight = FontWeight.Normal, fontSize = 16.sp
                )
            )
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .align(alignment = Alignment.BottomCenter), onClick = { onClick.invoke(Unit) }) {
            Text("Take me to home")
        }
    }
}

fun goBack(navHostController: NavHostController, screen: Screen) {
    navHostController.navigate(screen.route) {
        popUpTo(screen.route) {
            inclusive = true
        }
    }
}

fun refresh(viewModel: PostDetailViewModel) {
    viewModel.refresh()
}

@Composable
fun CreatePostScreen(
    viewModel: CreatePostViewModel = getViewModel(),
    navHostController: NavHostController
) {
    CreatePostForm(
        viewModel = viewModel,
        goBack = {
            goBack(
                navHostController = navHostController,
                screen = Screen.Home
            )
        }
    )
}

@Composable
fun PopError(headline: String, subtitle: String, onClick: ((Unit) -> Unit)) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.graphicsLayer(scaleY = 2f, scaleX = 2f),
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(id = R.string.warning),
                tint = Color(android.graphics.Color.parseColor("#FF5252"))
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = headline,
                style = typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = subtitle,
                style = typography.displayLarge.copy(
                    fontWeight = FontWeight.Normal, fontSize = 16.sp
                )
            )
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .align(alignment = Alignment.BottomCenter), onClick = { onClick.invoke(Unit) }) {
            Text("Refresh")
        }
    }
}