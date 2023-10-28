@file:OptIn(ExperimentalMaterial3Api::class)

package com.marelso.postthread.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
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
        .clickable { onClick.invoke(post.reference) }
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
    post: Post, updateStatus: (Boolean) -> Unit, goBack: (Unit) -> Unit
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
                )},
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