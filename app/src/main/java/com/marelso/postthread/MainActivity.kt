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
fun HomeScreen(viewModel: PostListViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        PostList(posts = viewModel.pagingData.collectAsLazyPagingItems())
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

@Composable
fun PostDetailScreen(viewModel: PostListViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        PostList(posts = viewModel.pagingData.collectAsLazyPagingItems())
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