package com.example.modugarden.main.discover.search

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.curationAPI
import com.example.modugarden.api.dto.CurationLikeResponse
import com.example.modugarden.api.dto.GetSearchCurationContent
import com.example.modugarden.main.content.CurationContentActivity
import com.example.modugarden.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//포스트, 큐레이션에 표시되는 카드들로 데이터 형식 알려주면 그때 넣겠삼삼
@Composable
fun DiscoverSearchCurationCard(curationData: GetSearchCurationContent) {
    val mContext = LocalContext.current
    val likeCnt = rememberSaveable{ mutableStateOf(curationData.likeNum) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) {
        curationAPI.getCurationLikeNum(curationData.id)
            .enqueue(  object : Callback<CurationLikeResponse> {
                override fun onResponse(
                    call: Call<CurationLikeResponse>,
                    response: Response<CurationLikeResponse>
                ) {
                    likeCnt.value = response.body()?.result?.like_num ?: 0
                }

                override fun onFailure(
                    call: Call<CurationLikeResponse>,
                    t: Throwable
                ) {

                }

            })
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick {

                val intent = Intent(mContext, CurationContentActivity::class.java)
                val bundle = Bundle()

                bundle.putInt("curation_id", curationData.id)

                intent.putExtras(bundle)

                val pendIntent: PendingIntent
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    pendIntent = PendingIntent
                        .getActivity(
                            mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                        )

                } else {
                    pendIntent = PendingIntent
                        .getActivity(
                            mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT
                        )
                }

                launcher.launch(
                    IntentSenderRequest
                        .Builder(pendIntent)
                        .build()
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
        ) {
            //큐레이션 카드 이미지

            GlideImage( // CoilImage, FrescoImage
                imageModel = curationData.preview_image,
                modifier = Modifier
                    .size(width = 110.dp, height = 110.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
                // shows an indicator while loading an image.
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

        }
        Spacer(modifier = Modifier.width(18.dp))

        Column {
            //타이틀
            Text(
                modifier = Modifier.width(230.dp),
                text = curationData.title,
                style = TextStyle(color = moduBlack,
                    fontWeight = FontWeight(500),
                    fontSize = 15.sp),
                //넘치면 ....으로 표시해주는놈
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(7.dp))

            //시간
            val timeLine = curationData.created_Date.split("T")[0].split("-")

            Text(text = "${timeLine[0]}년 ${timeLine[1]}월 ${timeLine[2]}일",
                style = TextStyle(color = moduGray_strong,
                    fontWeight = FontWeight(400),fontSize = 11.sp)
            )

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    imageModel =
                    if(curationData.user_profile_image == null)
                        R.drawable.ic_default_profile
                    else
                        curationData.user_profile_image,
                    modifier = Modifier
                        .size(width = 20.dp, height = 20.dp)
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
                            .override(128,128)
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))
                //작성자
                Text(
                    modifier = Modifier
                        .padding(bottom = 3.dp),
                    text = curationData.user_nickname,
                    style = TextStyle(color = Color(0xFF252525).copy(alpha = 0.8f),
                        fontWeight = FontWeight(400),fontSize = 11.sp)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.ic_heart_mini),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 15.dp, height = 15.dp)
                )

                Text(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    text = "${likeCnt.value}",
                    style = TextStyle(color = moduGray_normal,
                        fontWeight = FontWeight(400),fontSize = 12.sp)
                )

            }

        }

    }

    Spacer(modifier = Modifier.height(18.dp))

}