package com.example.modugarden.main.profile

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.modugarden.R
import com.example.modugarden.main.profile.follow.ProfileFollowActivity
import com.example.modugarden.ui.theme.*

/*
서버에서 프로필에 표시될 유저의 정보를 받아옴
*/

const val userId = 1
const val myId = 1

@Preview(showBackground = true)
@OptIn(ExperimentalMaterialApi::class)
@Composable //프로필, 인수로 유저의 정보를 받아옴
fun MyProfileScreen() {
    Column (
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ){
        val context = LocalContext.current
        // 프로필, 설정(또는 메뉴), 팔로우 목록을 묶고 있는 박스레이아웃
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(24.dp),
            propagateMinConstraints = false
        ) {
            // 설정(내 프로필) 또는 메뉴
            Icon(
                painter =
                    if(userId != myId)
                        painterResource(id = R.drawable.cog_8_tooth)
                    else
                        painterResource(id = R.drawable.ellipsis_vertical),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        enabled = true
                    ) {
                        /*
                        설정 화면으로 이동
                        */
                    }
                    .align(Alignment.TopEnd),
                tint = moduGray_normal
            )

            // 프로필
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(50.dp)
                    .clickable(onClick = {
                        Log.d("Start Follow Activity", "OK")
                        val intent = Intent(context, ProfileFollowActivity::class.java)

                        context.startActivity(intent)
                    })
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cog_8_tooth),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Row(
                        modifier = Modifier
                            .height(30.dp)
                    ) {
                        Text(
                            text = user.name,
                            style = TextStyle(
                                color = moduBlack,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_check_solid),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 5.dp)
                                .size(18.dp)
                        )
                    }
                    Text(
                        modifier = Modifier
                            .height(20.dp)
                            .wrapContentWidth(),
                        text = "팔로워 ${user.follower} · 게시물 ${user.post.size}",
                        style = TextStyle(
                            color = moduGray_normal,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            enabled = true
                        ) {
                            /*
                            설정 화면으로 이동
                            */
                        }
                        .align(Alignment.CenterVertically)
                        .padding(vertical = 15.dp)
                        .size(20.dp),
                    tint = moduGray_normal
                )
            }
        }

        // 카테고리를 띄울 레이지로우와 카테고리 추가(내 프로필) 또는 팔로우 버튼을 넣을 로우레이아웃
        Row (
            Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .fillMaxWidth()
                .height(36.dp)
        ) {
            // 카테고리 리스트가 들어갈 레이지로우
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                items(user.category) { category ->
                    Card (
                        Modifier
                            .wrapContentSize()
                            .fillMaxHeight()
                            .padding(end = 10.dp),
                        backgroundColor = moduBackground,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = category,
                            style = TextStyle(
                                color = moduBlack,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            // 카테고리 추가(내 프로필) 또는 팔로우 버튼 역할을 할 카드
            if(userId == myId)
            {
                Card(
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = moduBackground,
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                enabled = true
                            ) {
                                // 카테고리 추가
                            }
                            .wrapContentSize()
                            .background(color = Color.Transparent)
                    )
                }
            }
            else
            {
                Card(
                    modifier = Modifier
                        .width(64.dp)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = moduPoint,
                    onClick = {}
                ) {
                    Text(
                        text = "팔로우",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // 탭 구현
        ProfileTab()
    }
}