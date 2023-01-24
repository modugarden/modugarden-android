package com.example.modugarden.main.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.modugarden.R
import com.example.modugarden.main.profile.User
import com.example.modugarden.main.profile.categoryResponse
import com.example.modugarden.main.profile.curationResponse
import com.example.modugarden.main.profile.postResponse
import com.example.modugarden.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

val userList = listOf<User>(
    User("https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri()
        , "Mara", categoryResponse, 100, 50,
        true, postResponse, curationResponse
    ),
    User("https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri()
        ,"Logan", categoryResponse, 100, 50,
        true, postResponse, curationResponse
    ),
    User("https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri()
        , "Penguin", categoryResponse, 100, 50,
        true, postResponse, curationResponse
    )
)

@Composable
fun SettingsBlockScreen (
    // 차단한 유저 리스트
) {
    if(userList.isEmpty())
    {
        Box(modifier = Modifier.fillMaxSize())
        {
            Image(
                painter = painterResource(id = R.drawable.ic_setting_no_block),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    else
    {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            scaffoldState = scaffoldState,
            snackbarHost = {
                ScaffoldSnackBar (
                    snackbarHostState = it
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it).padding(18.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(userList) { blockedProfile ->
                    BlockedProfileCard(blockedProfile) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("${blockedProfile.name} 님의 차단을 해제했습니다.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlockedProfileCard (
    user: User,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .bounceClick {

            }
    ) {
        GlideImage(
            imageModel = user.image,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier
                .padding(vertical = 5.dp)
        )  {
            Text(
                text = user.name,
                style = TextStyle(
                    color = moduBlack,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = user.category.toString(),
                style = TextStyle(
                    color = moduGray_normal,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize()
                .bounceClick {
                    // 차단해제 api
                    onClick()
                },
            shape = RoundedCornerShape(5.dp),
            backgroundColor = moduBackground,
            elevation = 0.dp
        ) {
            Text(
                text = "차단 해제",
                style = TextStyle(
                    color = moduBlack,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}