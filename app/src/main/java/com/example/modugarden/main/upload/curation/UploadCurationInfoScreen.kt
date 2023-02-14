package com.example.modugarden.main.upload.curation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.data.Category
import com.example.modugarden.data.UploadCuration
import com.example.modugarden.main.upload.EditTextLikeButton
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_CURATION
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadCurationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadCurationInfoScreen(
    navController: NavHostController,
    uploadCurationViewModel: UploadCurationViewModel,
    data: UploadCuration
) {
    val charactersLen = 40

    val titleData = remember { mutableStateOf(data.title) }
    val titleFocused = remember { mutableStateOf(false) }
    val titleDescription = "글자 수 ${titleData.value.length}/${charactersLen}"
    val titleError = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val categoryData = remember { mutableStateOf(data.category) }
    val categoryFocused = remember { mutableStateOf(false) }

    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val showModalSheet = rememberSaveable{ mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val mContext = LocalContext.current


    titleError.value = titleData.value.length > charactersLen

    ModalBottomSheet(
        title = "카테고리",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            ModalBottomSheetItem(text = "식물 가꾸기", icon = R.drawable.ic_house_with_garden, trailing = true, modifier = Modifier.bounceClick {
                uploadCurationViewModel.saveCategory(Category.GARDENING)
                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "플랜테리어", icon = R.drawable.ic_potted_plant, trailing = true, modifier = Modifier.bounceClick {
                uploadCurationViewModel.saveCategory(Category.PLANTERIOR)
                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "여행/나들이", icon = R.drawable.ic_tent, trailing = true, modifier = Modifier.bounceClick {
                uploadCurationViewModel.saveCategory(Category.TRIP)
                scope.launch {
                    bottomSheetState.hide()
                }
            })
        },
        uiScreen = {
            BackHandler(enabled = bottomSheetState.isVisible) {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .addFocusCleaner(focusManager)
            ) {
                //상단 조작 바
                TopBar(
                    title = "",
                    titleIcon = R.drawable.ic_arrow_left_bold,
                    titleIconSize = 20.dp,
                    titleIconOnClick = {
                        (mContext as Activity).finish() //UploadCurationActivity 창 끄기.
                    },
                    main = false,
                    bottomLine = false
                )
                Box(modifier = Modifier.padding(18.dp)) {
                    Column {
                        Text("어떤 큐레이션인가요?", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = moduBlack)
                        Spacer(modifier = Modifier.height(18.dp))
                        //제목 textField
                        EditText(
                            title = "제목",
                            data = titleData,
                            isTextFieldFocused = titleFocused,
                            description = titleDescription,
                            errorListener = titleError,
                            singleLine = false,
                            textStyle = TextStyle(fontSize = 16.sp, color = moduBlack)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        //카테고리 설정 Button
                        Column() {
                            Text("카테고리", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = moduGray_strong)
                            Spacer(modifier = Modifier.height(5.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bounceClick {
                                        focusManager.clearFocus()
                                        showModalSheet.value = !showModalSheet.value
                                        scope.launch {
                                            bottomSheetState.show()
                                        }
                                    },
                                elevation = 0.dp,
                                backgroundColor = moduBackground,
                                shape = RoundedCornerShape(10.dp),
                            ) {
                                Row(
                                    modifier = Modifier.padding(15.dp)
                                ) {
                                    Text(
                                        text = data.category.category,
                                        color = moduBlack,
                                        fontSize = 16.sp,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_chevron_right),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(10.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                //다음 버튼
                BottomButton(
                    title = "다음",
                    dpScale = dpScale.value,
                    alpha = if(titleData.value.length in (1..charactersLen)) 1f else 0.4f,
                    shapeScale = shapeScale.value,
                    onClick = {
                        if (titleData.value.length in (1..charactersLen)) { //제목 글자 수가 1~25자라면
                            uploadCurationViewModel.saveTitle(titleData.value)
                            navController.navigate(NAV_ROUTE_UPLOAD_CURATION.IMAGEINFO.routeName)
                        }
                    }
                )
            }
        }
    )
}