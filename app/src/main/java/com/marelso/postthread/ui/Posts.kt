package com.marelso.postthread.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.marelso.postthread.data.Post

@Composable
fun PostList(posts: LazyPagingItems<Post>, onClick: ((Int) -> Unit)) {
    LazyColumn {
        items(posts.itemCount) { index ->
            posts[index]?.let {
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
        .padding(all = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
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
