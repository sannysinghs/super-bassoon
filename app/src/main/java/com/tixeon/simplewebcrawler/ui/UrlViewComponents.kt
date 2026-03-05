package com.tixeon.simplewebcrawler.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tixeon.simplewebcrawler.R
import com.tixeon.simplewebcrawler.ui.theme.SimpleWebCrawlerTheme

@Composable
fun UrlTile(
    name: String,
    totalLinks: Int = 10,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = Modifier
            .clickable { }
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Column(
            modifier = modifier.weight(2f)
        ) {
            Text(
                text = "$name!",
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = "Link count: $totalLinks",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Icon(
            painter = painterResource(R.drawable.baseline_arrow_forward_24),
            contentDescription = stringResource(id = R.string.app_name)
        )

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleWebCrawlerTheme {
        UrlTile("http://www.google.come", totalLinks = 10)
    }
}