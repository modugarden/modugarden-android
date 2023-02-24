package com.example.modugarden.main.discover.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.data.RecentSearch
import com.example.modugarden.data.RecentSearchDatabase
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack


@Composable
fun DiscoverSearchBeforeCard(
    navController: NavHostController,
    recentSearch: RecentSearch,
    db: RecentSearchDatabase,
    ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .bounceClick {
                    db.recentSearchDao().delete(recentSearch)
                    db.recentSearchDao().insert(RecentSearch(recentSearch.searchText))
                    navController.navigate(NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHRESULT.routeName + "/" + recentSearch.searchText)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                backgroundColor = moduBackground
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search_small),
                        contentDescription = null,
                        modifier = Modifier
                            .size(height = 18.dp, width = 18.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = recentSearch.searchText,
                style = TextStyle(
                    color = moduBlack,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp
                )
            )

        }

        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.bounceClick {
                db.recentSearchDao().delete(recentSearch)
                navController.navigate(NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName) {
                    popUpTo(NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName) {
                        inclusive = true
                    }
                }
            },
            painter = painterResource(id = R.drawable.ic_cross_line_bold_gray),
            contentDescription = null,
        )
    }
    Spacer(modifier = Modifier.height(18.dp))

}