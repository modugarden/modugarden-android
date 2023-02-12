package com.example.modugarden.main.discover.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.api.dto.FindByNicknameResContent
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//포스트, 큐레이션에 표시되는 카드들로 데이터 형식 알려주면 그때 넣겠삼삼
@Composable
fun DiscoverSearchUserCard(
    userData : FindByNicknameResContent,
    coroutineScope : CoroutineScope,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    userViewModel: UserViewModel
) {

    val followState = remember{ mutableStateOf(userData.follow) }
    val blockState = remember { mutableStateOf(userData.block) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .bounceClick {
                    userViewModel.setUserId(userData.userId)
                    navController.navigate(NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHUSERPROFILE.routeName)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_background),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(width = 50.dp, height = 50.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
            GlideImage(
                imageModel =
                if(userData.profileImage == null)
                    R.drawable.ic_default_profile
                else
                    userData.profileImage,
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                loading = {
                    ShowProgressBar()
                },
                // shows an error text if fail to load an image.
                failure = {
                    Text(text = "image request failed.")
                },
                requestOptions = {
                    RequestOptions()
                        .override(256,256)
                }
            )
            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    modifier = Modifier.width(200.dp),
                    text = userData.nickname,
                    style = TextStyle(
                        color = moduBlack,
                        fontWeight = FontWeight(700),
                        fontSize = 15.sp
                    ),
                    //넘치면 ....으로 표시해주는놈
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(5.dp))


                Text(
                    text =  userData.categories.joinToString(", ","",""),
                    style = TextStyle(
                        color = Color(0xFF959DA7),
                        fontWeight = FontWeight(400), fontSize = 13.sp
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        FollowCard(
            id = userData.userId,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentHeight()
                .wrapContentWidth(),
            snackBarAction = {
                coroutineScope.launch {
                    if(followState.value) snackbarHostState.showSnackbar("${userData.nickname} 님을 팔로우 했어요.")
                    else snackbarHostState.showSnackbar("${userData.nickname} 님을 언팔로우 했어요.")

                }
            },
            blockState = blockState,
            followState = followState,
            contentModifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(vertical = 6.dp, horizontal = 10.dp),
            unBlockSnackBarAction = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("${userData.nickname} 님을 차단해제했어요.")
                }
            }
        )

//        //팔로우 버튼
//        Card(
//            modifier = Modifier
//                .width(51.dp)
//                .height(24.dp)
//                .bounceClick {
//                    followState.value = followState.value.not()
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar(
//                            "$userData 님을 팔로우 하였습니다.",
//                            duration = SnackbarDuration.Short
//                        )
//                    }
//                },
//            backgroundColor =
//            if(followState.value) moduBackground else Color(0xFF6CD09A),
//            shape = RoundedCornerShape(5.dp)
//        ){
//            Box(contentAlignment = Alignment.Center) {
//                Text(
//                    text =  if(followState.value) "팔로잉" else "팔로우",
//                    style = TextStyle(
//                        color =  if(followState.value) moduBlack else Color.White,
//                        fontWeight = FontWeight(700), fontSize = 11.sp
//                    ),
//
//                )
//            }
//        }


    }

    Spacer(modifier = Modifier.height(18.dp))
}