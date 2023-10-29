@file:OptIn(ExperimentalMaterial3Api::class)

package com.marelso.postthread.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import com.marelso.postthread.PopError
import com.marelso.postthread.R
import com.marelso.postthread.data.Post

@Composable
fun PostList(posts: LazyPagingItems<Post>, onClick: ((Int) -> Unit)) {
    LazyColumn {
        items(posts.itemCount) { index ->
            posts[index]?.let {
                Spacer(modifier = Modifier.size(16.dp))
                PostCard(it, onClick)
            }
        }
    }
}

@Composable
fun PostCard(post: Post, onClick: ((Int) -> Unit)) {
    Card(modifier = Modifier
        .clickable { post.reference?.let { onClick.invoke(it) } }
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberImagePainter(post.previewImage)

            Image(
                painter = painter,
                contentDescription = "Post's image preview",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = post.headline, style = typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.description,
                    style = typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PostDetail(
    post: Post, updateStatus: (Boolean) -> Unit,
    goBack: (Unit) -> Unit,
    delete: (Unit) -> Unit
) {
    var status by remember { mutableStateOf(post.status) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        TopAppBar(modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1,
                    text = post.headline,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier.clickable { goBack.invoke(Unit) },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.warning),
                )
            },
            actions = {
                Switch(checked = status, onCheckedChange = {
                    status = it
                    updateStatus.invoke(status)
                })
                Icon(
                    modifier = Modifier
                        .graphicsLayer(scaleY = 1.5f, scaleX = 1.5f)
                        .padding(start = 8.dp)
                        .clickable { delete.invoke(Unit) },
                    imageVector = Icons.Default.Delete,
                    tint = Color(android.graphics.Color.parseColor("#FF5252")),
                    contentDescription = stringResource(id = R.string.warning),
                )
            })
        SubcomposeAsyncImage(
            model = post.bannerImage,
            loading = {
                LinearProgressIndicator()
            },
            contentDescription = "Post's image preview",
            modifier = Modifier
                .padding(end = 8.dp)
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(25.dp))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = post.previewImage,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = "Post's image preview",
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .padding(end = 8.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(25.dp))
            )

            Text(
                text = post.headline, style = typography.headlineSmall
            )
        }

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = post.description, style = typography.displayLarge.copy(
                fontWeight = FontWeight.Bold, fontSize = 16.sp
            )
        )
        Text(
            text = post.content, style = typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal, fontSize = 20.sp
            )
        )
    }
}

@Composable
fun PostDetailLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CreatePostForm(viewModel: CreatePostViewModel, goBack: (Unit) -> Unit) {
    var step by remember { mutableStateOf<Step>(Step.Images) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        if (step != Step.Complete && step != Step.Loading) TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = { Text("Create new post") },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { goBack.invoke(Unit) },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.warning),
                )
            })

        when (step) {
            is Step.Images -> ImagesStep(viewModel = viewModel) {
                step = Step.BasicInfo
            }

            is Step.BasicInfo -> BasicInfoStep(viewModel = viewModel) {
                step = Step.Content
            }

            is Step.Content -> ContentStep(viewModel = viewModel) {
                viewModel.create()
                step = Step.Loading
            }
            is Step.Complete -> ResourceCreated {
                goBack.invoke(Unit)
            }
            is Step.Loading -> WaitingPostResponse(viewModel = viewModel) {
                step = it
            }
            is Step.Error -> PopError(
                headline = "Oops something went wrong while creating new post",
                subtitle = "Try again later",
                onClick = { goBack.invoke(Unit) })
        }
    }
}

@Composable
fun ResourceCreated(onClick: (Unit) -> Unit) {
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
            Text(
                modifier = Modifier.padding(vertical = 30.dp),
                text = "Your post has been successfuly created.",
                style = typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .align(alignment = Alignment.BottomCenter), onClick = { onClick.invoke(Unit) }) {
            Text("Go to home")
        }
    }
}

@Composable
fun WaitingPostResponse(viewModel: CreatePostViewModel, onClick: (Step) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(90.dp))
    }
    val state = viewModel.state.collectAsState()

    when(state.value) {
        is CreateUiState.Success -> onClick.invoke(Step.Complete)
        is CreateUiState.Error -> onClick.invoke(Step.Error)
        is CreateUiState.Loading -> {
            onClick.invoke(Step.Loading)
        }
    }
}

@Composable
fun BasicInfoStep(viewModel: CreatePostViewModel, onClick: (Unit) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(vertical = 30.dp)
                    .fillMaxWidth(),
                text = "Share post info with us.",
                style = typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TextField(
                modifier = Modifier.padding(vertical = 16.dp)
                    .fillMaxWidth(),
                value = viewModel.headline.value,
                label = { Text(text = "Post headline") },
                placeholder = { Text(text = "Your best headline") },
                onValueChange = {
                    viewModel.headline.value = it
                }
            )

            TextField(
                modifier = Modifier.padding(vertical = 16.dp)
                    .fillMaxWidth(),
                value = viewModel.description.value,
                label = { Text(text = "Post description") },
                placeholder = { Text(text = "Awesome description") },
                onValueChange = {
                    viewModel.description.value = it
                }
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .align(alignment = Alignment.BottomCenter),
            onClick = { onClick.invoke(Unit) },
            enabled = viewModel.headline.value.isNotBlank() && viewModel.description.value.isNotBlank()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun ImagesStep(viewModel: CreatePostViewModel, onClick: (Unit) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(vertical = 30.dp)
                    .fillMaxWidth(),
                text = "Share post images with us.",
                style = typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TextField(
                modifier = Modifier.padding(vertical = 16.dp)
                    .fillMaxWidth(),
                value = viewModel.bannerImage.value,
                label = { Text(text = "Banner image") },
                placeholder = { Text(text = "Awesome image") },
                onValueChange = {
                    viewModel.bannerImage.value = it
                }
            )

            TextField(
                modifier = Modifier.padding(vertical = 16.dp)
                    .fillMaxWidth(),
                value = viewModel.previewImage.value,
                label = { Text(text = "Preview image") },
                placeholder = { Text(text = "Awesome image") },
                onValueChange = {
                    viewModel.previewImage.value = it
                }
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .align(alignment = Alignment.BottomCenter),
            onClick = { onClick.invoke(Unit) },
            enabled = viewModel.previewImage.value.isNotBlank() && viewModel.bannerImage.value.isNotBlank()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun ContentStep(viewModel: CreatePostViewModel, onClick: (Unit) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(vertical = 30.dp)
                    .fillMaxWidth(),
                text = "Share what are you thinking with us.",
                style = typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TextField(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(380.dp),
                value = viewModel.content.value,
                label = { Text(text = "The content goes here") },
                placeholder = { Text(text = "...") },
                onValueChange = {
                    viewModel.content.value = it
                }
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .align(alignment = Alignment.BottomCenter),
            onClick = { onClick.invoke(Unit) },
            enabled = viewModel.content.value.isNotBlank()
        ) {
            Text("Continue")
        }
    }
}

sealed class Step {
    object Images : Step()
    object BasicInfo : Step()
    object Content : Step()
    object Complete : Step()
    object Error : Step()
    object Loading : Step()
}