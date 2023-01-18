package com.example.modugarden.main.follow

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.modugarden.R
import com.example.modugarden.main.content.CurationContentActivity
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable //팔로우 피드에 표시되는 큐레이션 카드 item.
fun CurationCard(userID:String,
                 scope: CoroutineScope,
                 snackbarHostState: SnackbarHostState
) {
    val mContext = LocalContext.current
    Card(
        modifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 9.dp, bottom = 9.dp)
            .clip(RoundedCornerShape(20.dp)),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = "$userID",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), contentAlignment = Alignment.BottomCenter
                ) {
                // 큐레이션 썸네일
                Image(
                    painter = painterResource(id = R.drawable.plant2),
                    contentDescription = "썸네일",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f))

                // 외부 페이지 이동
                    // 이미지 하단 블러처리
                Image(
                    painter = painterResource(id = R.drawable.plant2),
                    contentDescription = "썸네일",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .drawWithContent { //ContentDrawScope
                            clipRect(top = size.height / 1.11f) {
                                this@drawWithContent.drawContent()
                                drawRect(
                                    moduBlack,
                                    alpha = 0.4f, blendMode = BlendMode.DstIn
                                )
                            }
                        }
                        .blur(10.dp)
                        .fillMaxWidth()

                )
                    // 안내 문구 및 아이콘
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clickable {
                        mContext.startActivity(
                            Intent(mContext, CurationContentActivity::class.java)
                        )
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween ) {
                    Text(modifier=Modifier.padding(start = 18.dp),
                        text = "외부 페이지로 이동해요.",
                        color = moduGray_light,
                        fontSize = 12.sp)
                    Icon(modifier=Modifier.padding(end=18.dp),
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "외부 페이지 이동",
                        tint = moduGray_light)
                }
            }
            Box(modifier = Modifier
                .background(Color.White)
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2

                    drawLine(
                        Color("#EBEEED".toColorInt()),
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp)
                ) {
                    Text("Title", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color("#17291F".toColorInt()))
                    Row() {
                        Text("category", fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                        Spacer(modifier = Modifier.weight(1f))
                        Text("upload time", fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                    }

                }

            }
            // 버튼들 ( 좋아요, 스크랩, 신고 )
            val isButtonClickedLike = remember { mutableStateOf(false) }
            val isButtonClickedSave = remember { mutableStateOf(false)}
            Row(
                Modifier.padding(22.dp)) {
                // 좋아요
                Icon(modifier = Modifier
                    .padding(end = 18.dp)
                    .bounceClick {
                                 if (isButtonClickedLike.value)
                                     isButtonClickedLike.value = false
                                else
                                     isButtonClickedLike.value = true
                    }
                    ,painter = painterResource
                        (id =
                            if (isButtonClickedLike.value)
                                R.drawable.ic_heart_solid
                            else
                                R.drawable.ic_heart_line
                        ),
                    contentDescription = "좋아요",
                tint = if (isButtonClickedLike.value)
                            Color(0xFFFF6767)
                        else
                            moduBlack
                )
                // 스크랩
                Icon(modifier = Modifier
                    .bounceClick {
                        isButtonClickedSave.value = !isButtonClickedSave.value

                        if (isButtonClickedSave.value){
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "게시물을 저장하였습니다.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                    ,painter = painterResource
                        (id =
                    if (isButtonClickedSave.value)
                        R.drawable.ic_star_solid
                    else
                        R.drawable.ic_star_line
                    ),
                    contentDescription = "스크랩",
                    tint = moduBlack
                )
                // 신고
                Box(Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd) {
                    Icon(modifier = Modifier.bounceClick {  },
                        painter = painterResource(id = R.drawable.ic_dot3_vertical),
                        contentDescription = "신고",
                        tint = moduBlack)
                }

            }

        }
    }
}

@Preview
@Composable
fun CurationPreview(){
    // 팔로우 스낵바 메세지 띄울 때 필요
    val scope = rememberCoroutineScope()
    // 팔로우 스낵바 메세지 상태 변수
    val snackbarHostState = remember { SnackbarHostState() }
    CurationCard("user1",scope,snackbarHostState)
}