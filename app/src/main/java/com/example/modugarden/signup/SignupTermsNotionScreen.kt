package com.example.modugarden.signup

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.ui.theme.TopBar

@Composable
fun SignupTermsNotionScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()

    Box(
        Modifier.fillMaxSize()
    ) {
        Column() {
            TopBar(
                title = "개인정보 처리방침",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconOnClick = {
                    navController.popBackStack()
                },
                titleIconSize = 20.dp,
                bottomLine = true
            )
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Text(text = "제 1 장 총칙\n" +
                        "제 1 조 (목적)\n" +
                        "이 약관은「모두의 정원」(이하 “사이트”라 칭함)에서 제공하는 인터넷관련서비스(이하 \"서비스\"라 칭함)를 이용함에 있어 「모두의 정원」와 이용자의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                        "제 2 조 (용어의 정의)\n" +
                        "이 약관에서 사용하는 주요한 용어의 정의는 다음과 같습니다.\n" +
                        "① ‘회원’이라 함은 이 약관에 동의하고, 「모두의 정원」와 서비스 이용 계약을 체결하여 이용자 아이디(ID)를 부여 받은 개인 및 기관을 말합니다.\n" +
                        "② ‘회원 아이디’(이하 ‘ID’라 칭함)라 함은 회원의 식별과 회원의 서비스 이용을 위하여 회원이 선정하고 신청함에 따라 「모두의 정원」에서 승인한 문자나 숫자 혹은 그 조합을 말합니다. 기관의 정보로 가입한 ID는 관련 법규가 인정하는 범위에서 개인정보와 같은 권리와 의무를 가지며 이하 개인과 개인정보로 언급되는 모든 약관은 기관과 기관정보를 포함합니다.\n" +
                        "③ ‘비밀번호’라 함은 회원ID로 식별되는 회원의 본인 여부를 검증하고, 회원 자신의 비밀을 보호하기 위하여 회원이 선정하여 사이트에 등록한 고유의 문자와 숫자의 조합을 말합니다.\n" +
                        "④ ‘이용중지’라 함은 사이트가 본 약관에 의거하여 회원의 서비스 이용을 제한하는 것을 말합니다.\n" +
                        "⑤ ‘해지’라 함은 사이트 또는 회원이 일방적으로 이용계약을 장래에 소멸시키는 것을 말합니다.\n" +
                        "⑥ “저작권”이란 인간의 사상 또는 감정을 표현한 창작물인 저작물에 대한 배타적·독점적 권리이며, 이러한 저작물에는 소설·시·논문·강연·연술(演述)·각본·음악·연극·무용·회화·서예·도안(圖案)·조각·공예·건축물·사진·영상(映像)·도형(圖形)·컴퓨터프로그램 등이 있다.\n" +
                        "제 3 조 (약관의 효력 및 변경)\n" +
                        "① 이 약관은 서비스를 통하여 온라인으로 공시하고 회원의 동의와 서비스의 승낙으로 효력이 발생됩니다.\n" +
                        "② 서비스는 사정상 중요한 사유가 발생할 경우 사전 고지 없이 이 약관의 내용을 변경할 수 있으며, 변경된 약관은 정당한 절차에 따라 서비스를 통하여 공지함으로써 효력을 발휘합니다.\n" +
                        "③ 회원은 변경된 약관에 동의하지 않을 경우 회원 가입에 제약이 따르거나, 회원 탈퇴를 요청할 수 있으며, 변경된 약관의 효력 발생일 이후 10일 이내에 해지 요청을 하지 않을 경우 약관의 변경 사항에 동의한 것으로 간주됩니다.\n" +
                        "④ 회원은 정기적으로 서비스에 방문하여 약관의 변경사항을 확인하여야 합니다. 변경된 약관에 대한 정보를 알지 못해 발생하는 회원의 피해는 서비스에서 책임지지 않습니다.\n" +
                        "제 4 조 (약관 외 준칙)\n" +
                        "본 약관에 명시되지 아니한 사항에 대해서는 전기통신기본법, 전기통신사업법, 정보통신망 이용촉진 및 정보보호 등에 관한 법률 및 기타 관련 법령의 규정에 따릅니다. 제 2장 서비스 이용 계약제 5 조 (서비스 이용 신청)회원으로 가입하여 서비스를 이용하기를 희망하는 자는 서비스에서 정한 가입 양식에 따라 회원정보를 기입하고, 요청하는 제반 정보(개인인적사항이나 기관정보 등)를 제공하여 이용신청을 합니다.\n" +
                        "제 2 장 서비스 이용\n" +
                        "계약제 5 조 (서비스 이용 신청)\n" +
                        "회원으로 가입하여 서비스를 이용하기를 희망하는 자는 서비스에서 정한 가입 양식에 따라회원정보를 기입하고, 요청하는 제반 정보(개인인적사항이나 기관정보 등)를 제공하여 이용신청을 합니다.\n" +
                        "제 6 조 (이용 계약의 성립)\n" +
                        "① 이용계약은 이용자의 본 약관에 대한 동의와 이용신청에 대하여 서비스가 이용 승낙함으로써 성립합니다.\n" +
                        "② 약관에 대한 동의는 이용신청 당시 신청서 상의 ‘동의합니다’ 버튼을 누름으로써 의사표시를 합니다.\n" +
                        "제 7 조 (이용 신청의 승낙과 제한)\n" +
                        "① 서비스는 제 5 조에 따른 이용신청에 대하여 업무 수행상 또는 기술상 지장이 없는 경우에 원칙적으로 접수 순서대로 이용 신청을 승낙합니다.\n" +
                        "② 서비스는 다음 각 호에 해당하는 경우 이용신청에 대한 승낙을 제한할 수 있고, 그 사유가 해소될 때까지 승낙을 유보할 수 있습니다.\n" +
                        "\n" +
                        "1. 서비스 관련 설비에 여유가 없는 경우\n" +
                        "2. 기술상 지장이 있는 경우\n" +
                        "3. 기타 서비스의 사정상 필요하다고 인정되는 경우\n" +
                        "③ 서비스는 다음 각 호에 해당하는 경우 호의 승낙을 거절할 수 있습니다.\n" +
                        "4. 본인의 진정한 정보를 제공하지 아니한 이용신청의 경우\n" +
                        "5. 다른 사람의 명의를 사용하여 신청한 경우\n" +
                        "6. 이용 신청 시 필요 사항을 허위로 기재하여 신청한 경우\n" +
                        "7. 사회의 안녕과 질서 혹은 미풍양속을 저해할 목적으로 신청한 경우\n" +
                        "8. 기타 서비스가 정한 이용 신청 요건이 미비 된 경우\n" +
                        "④ 서비스는 회원이 이용 신청 시 제공한 정보는 회원 가입 시점부터 해지 완료 후 회원관리 처리 목적에 따라 2년까지 보관할 수 있습니다.\n" +
                        "⑤ 개인정보보호와 관련된 보다 자세한 사항은 개인정보 보호법을 규정합니다.\n" +
                        "제 8 조 (개인정보의 보호)\n" +
                        "① 서비스는 관계 법령이 정하는 바에 따라 회원의 개인정보를 보호하고 존중하기 위해 노력합니다. 회원의 개인정보보호에 관해서는 관련법령 및 서비스에서 정하는 “개인정보보호정책”에 정한 바에 의합니다.\n" +
                        "② 서비스는 이용 신청 시 또는 커뮤니티 활동, 이벤트 참가를 위하여 회원이 제공한 정보 및 기타 서비스 이용 과정에서 수집되는 정보 등을 수집할 수 있고, 이렇게 수집되어진 회원의 개인정보는 회원과의 이용계약 이행과 본 서비스상의 서비스 제공을 위한 목적으로 사용됩니다.\n" +
                        "③ 서비스는 수집한 회원의 신상정보를 본인의 승낙 없이 제3자에게 누설 또는 배포할 수 없으며 상업적 목적으로 사용할 수 없습니다. 다만, 다음의 각 호에 해당하는 경우에는 그러하지 아니합니다.\n" +
                        "9. 정보통신서비스의 제공에 따른 요금 정산을 위하여 필요한 경우\n" +
                        "10. 다통계작성, 학술연구 또는 시장조사를 위하여 필요한 경우로서 특정 개인을 알아볼 수 없는 형태로 가공하여 제공하는 경우\n" +
                        "11. 관계 법령에 의하여 수사상 목적으로 정해진 절차와 방법에 따라 관계기관의 요구가 있는 경우\n" +
                        "12. 다른 법률에 특별한 규정이 있는 경우\n" +
                        "13. 방송통신심의위원회가 관계법령에 의하여 요청하는 경우\n" +
                        "④ 서비스는 회원이 이용 신청시 제공한 정보를 회원 가입 시점부터 해지 완료 후 회원관리 처리 목적에 따라 2년까지 보관할 수 있습니다.\n" +
                        "⑤ 개인정보보호와 관련된 보다 자세한 사항은 개인정보처리방침을 규정합니다.\n" +
                        "제 9 조 (개인정보의 이용)\n" +
                        "① 서비스는 제 8조 제 3 항을 위반하지 않는 한도 내에서 업무와 관련하여 회원 전체 또는 일부의 개인정보에 관한 집합적인 통계 자료를 작성하여 이를 사용할 수 있고, 서비스를 통하여 회원의 컴퓨터에 쿠키를 전송할 수 있습니다. 이 경우 회원은 쿠키의 수신을 거부하거나 쿠키의 수신에 대하여 경고하도록 사용하는 컴퓨터의 브라우저의 설정을 변경할 수 있습니다.\n" +
                        "② 서비스가 수집하는 개인정보는 서비스의 제공에 필요한 최소한으로 하되, 필요한 경우 보다 더 자세한 정보가 요구될 수 있습니다.\n" +
                        "③ 회원은 언제든지 원하는 경우에 서비스에 제공한 개인정보의 수집과 이용에 대한 동의를 철회할 수 있으며, 동의의 철회는 해지 신청을 함으로써 이루어지게 됩니다.\n" +
                        "제 10 조 (계약 사항의 변경)\n" +
                        "① 회원은 고객지원 메뉴를 통해 언제든지 본인의 개인정보를 열람하고 수정할 수 있습니다.\n" +
                        "② 회원은 이용신청 시 기재한 사항이 변경되었을 경우 온라인으로 수정을 해야 하며 회원정보를 변경하지 아니하여 발생되는 일련의 모든 문제의 책임은 회원에게 있습니다.\n" +
                        "제 3 장 계약 당사자의 의무\n" +
                        "제 11 조 (서비스의 의무)\n" +
                        "① 서비스는 특별한 사정이 없는 한 이용자가 서비스 이용을 신청한 날부터 서비스를 이용할 수 있도록 합니다.\n" +
                        "② 서비스는 회원이 소정의 절차에 따라 제기한 의견이나 불만이 정당하다고 인정될 경우에는 적절한 절차를 거쳐 즉시 처리하여야 합니다. 즉시 처리가 곤란한 경우는 당 회원에게 그 사유와 처리 일정을 통보하여야 합니다.\n" +
                        "③ 서비스는 회원이 소정의 절차에 따라 제기한 의견이나 불만이 정당하다고 인정될 경우에는 적절한 절차를 거쳐 즉시 처리하여야 합니다. 즉시 처리가 곤란한 경우는 당 회원에게 그 사유와 처리 일정을 통보하여야 합니다.\n" +
                        "④ 서비스는 회원의 프라이버시 보호와 관련하여 제 8 조에 제시된 내용을 지킵니다.\n" +
                        "⑤ 서비스는 이용계약의 체결, 계약사항의 변경 및 해지 등 회원과의 계약 관련 절차 및 내용 등에 있어 회원에게 편의를 제공하도록 노력합니다.\n" +
                        "⑥ 서비스는 다음 각 호에 해당하는 경우 서비스의 전부 또는 일부를 제한하거나 제공을 중지할 수 있습니다.\n" +
                        "14. 서비스용 설비의 확장, 보수 등 공사로 인한 부득이한 경우\n" +
                        "15. 전기통신사업법에 규정된 기간통신사업자가 전기통신 서비스를 중지했을 경우\n" +
                        "16. 기타 불가항력적 사유가 있는 경우\n" +
                        "17. 서비스는 국가비상사태, 천재지변, 기타 정전 등 불가항력적 사유가 있는 경우 정상적인 서비스 이용에 지장이 있는 때에는 서비스의 전부 또는 일부를 제한하거나 중지할 수 있습니다.\n" +
                        "18. 서비스가 통제하기 곤란한 사정으로 불가피하거나, 서비스 설비의 장애 또는 서비스 이용의 폭주 등으로 정상적인 서비스 이용에 지장이 있는 때에는 서비스의 전부 또는 일부를 제한하거나 중지할 수 있습니다.\n" +
                        "⑦ 서비스는 제1항에 의한 서비스의 이용을 제한하거나 중지한 때에는 그 사유 및 제한기간 등을 지체 없이 회원에게 알려야 합니다.\n" +
                        "제 12 조 (회원의 의무)\n" +
                        "① 회원은 회원가입 신청 또는 회원정보 변경 시 모든 사항을 사실에 근거하여 본인의 진정한 정보로 작성하여야 하며, 허위 또는 타인의 정보로 작성한 경우 이와 관련된 모든 권리를 주장할 수 없습니다.\n" +
                        "② 회원은 약관에서 규정하는 사항과 기타 서비스가 정한 제반 규정의 공지사항 등 서비스가 공지하는 사항 및 관계 법령을 준수하여야 하며 기타 서비스의 업무에 방해가 되는 행위, 서비스의 명예를 손상시키는 행위, 타인에게 피해를 주는 행위를 해서는 안됩니다.\n" +
                        "③ 회원의 ID와 비밀번호에 관한 모든 관리책임은 회원에게 있습니다. 회원에게 부여된 ID와 비밀번호의 관리 소홀, 부정사용에 의하여 발생하는 모든 결과에 대한 책임은 회원에게 있습니다.\n" +
                        "④ 회원의 ID 및 비밀번호의 관리 책임은 전적으로 회원에게 있습니다. 이를 소홀이 관리하여 발생하는 서비스 이용상의 손해 또는 제3자에 의한 부정사용에 의한 책임은 회원에게 있으며 서비스는 그에 대한 책임을 지지 않습니다. 회원은 본인의 ID 및 비밀번호가 부정하게 사용되었다는 사실을 발견한 경우에는 즉시 서비스에 그 사실을 알려야 하며, 알리지 않아 발생하는 모든 결과에 대한 책임은 회원에게 있습니다.\n" +
                        "⑤ 회원은 서비스의 사전승낙 없이는 서비스에서 제공하는 서비스를 이용하여 영업활동을 할 수 없으며, 약관에 위반한 일련의 영업활동의 결과에 대하여 서비스는 책임을 지지 않습니다. 또한 회원의 이와 같은 영업활동으로 서비스가 손해를 입은 경우, 해당 회원은 서비스에 대하여 손해배상의무를 지며, 서비스는 해당 회원에 대해 서비스 이용제한 및 일련의 절차를 거쳐 손해배상 등을 청구할 수 있습니다.\n" +
                        "⑥ 서비스의 명시적인 동의가 없는 한 회원은 본인의 서비스 이용권한, 기타 이용 계약상 지위를 타인에게 양도, 증여하거나 이를 담보로 제공할 수 없습니다.\n" +
                        "⑦ 회원은 서비스 이용과 관련하여 다음 각 호에 해당되는 행위를 하여서는 안됩니다.\n" +
                        "19. 다른 회원의 ID와 비밀번호, 주민등록번호, 사업자등록번호 등을 도용하는 행위\n" +
                        "20. 본 서비스를 통하여 습득한 정보를 서비스의 사전승낙 없이 회원 본인의 이용 이외 다른 목적으로 복제하거나 이를 출판 및 방송 등에 사용하거나 제3자에게 제공하는 행위\n" +
                        "21. 타인의 특허, 상표, 영업비밀, 저작권 기타 저작재산권을 침해하는 내용을 전송, 게시하거나, 전자메일 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
                        "22. 공공질서 및 미풍양속에 위반되는 저속, 음란한 내용의 정보, 문장, 도형 등을 전송, 게시하거나, 전자메일 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
                        "23. 모욕적이거나 위협적이어서 타인의 프라이버시를 침해할 수 있는 내용을 전송, 게시하거나, 전자메일 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
                        "24. 범죄와 결부된다고 객관적으로 판단되는 행위\n" +
                        "25. 서비스의 승인을 받지 않고 다른 사용자의 개인정보를 수집 또는 저장하는 행위\n" +
                        "26. 기타 관계법령에 위배되는 행위\n" +
                        "제 4 장 서비스 이용\n" +
                        "제 13 조 (서비스 이용 범위)\n" +
                        "회원은 서비스 가입 시 발급된 ID 하나로 서비스 내의 모든 서비스를 이용할 수 있습니다.\n" +
                        "제 14 조 (서비스 이용시간)\n" +
                        "① 서비스 이용을 서비스의 업무상 또는 기술상 특별한 지장이 없는 한 연중무휴, 1일 24시간 운영을 원칙으로 합니다. 단, 서비스는 시스템 점검 및 교체를 위해 서비스가 정한 날이나 시간에 서비스를 일시 중단할 수 있으며, 예정되어 있는 작업으로 인한 서비스 일시 중단은 공지사항을 통해 공지합니다.\n" +
                        "② 서비스는 긴급한 시스템 점검, 증설 및 교체, 설비의 장애, 서비스 이용의 폭주, 국가비상사태, 정전 등 부득이한 사유가 발생한 경우 사전 공지 없이도 일시적으로 서비스의 전부 또는 일부를 중단할 수 있습니다.\n" +
                        "③ 서비스는 서비스 개편 등 서비스 운영 상 필요한 경우 회원에게 사전 예고 후 서비스의 전부 또는 일부의 제공을 중단할 수 있습니다.\n" +
                        "④ 서비스는 서비스를 일정범위로 분할하여 각 범위 별로 이용가능 시간을 별도로 정할 수 있습니다. 이 경우 사전에 공지를 통해 그 내용을 알립니다.\n" +
                        "제 15 조 (정보의 제공)\n" +
                        "서비스는 회원이 서비스 이용에 있어 필요가 있다고 인정되는 다양한 정보에 대해서 공지사항이나 전자우편, 우편, SMS, 전화 등의 방법으로 회원에게 제공할 수 있습니다. 단, 서비스는 회원이 서비스 혜택 정보 제공을 원치 않는다는 의사를 밝히는 경우 정보 제공 대상에서 해당 회원을 제외하여야 하며, 대상에서 제외되어 서비스 정보를 제공받지 못해 불이익이 발생하더라도 이에 대해서는 서비스가 책임지지 않습니다.\n" +
                        "제 16 조 (회원의 게시물 등)\n" +
                        "① 게시물이라 함은 회원이 서비스를 이용하면서 게시한 글, 사진, 각종파일 및 링크 등을 말합니다.\n" +
                        "② 서비스는 회원이 게시하거나 등록하는 서비스내의 내용물이 다음 각 호에 해당한다고 판단되는 경우에 사전통지 없이 삭제할 수 있습니다.\n" +
                        "27. 다른 회원 또는 제3자를 비방하거나 중상모략으로 명예를 손상시키는 내용인 경우\n" +
                        "28. 다른 회원 또는 제3자를 비방하거나 중상모략으로 명예를 손상시키는 내용인 경우\n" +
                        "29. 범죄적 행위에 결부된다고 인정되는 내용일 경우\n" +
                        "30. 서비스의 저작권, 제 3 자의 저작권 등 기타 권리를 침해하는 내용인 경우\n" +
                        "31. 서비스에서 규정한 게시기간이나 용량을 초과한 경우\n" +
                        "32. 회원이 음란물을 게재하거나 음란사이트를 링크하는 경우\n" +
                        "33. 게시판의 성격에 부합하지 않는 게시물의 경우\n" +
                        "34. 기타 관계법령에 위반된다고 판단되는 경우\n" +
                        "제 17 조 (게시물에 대한 저작권)\n" +
                        "서비스에 게재된 자료에 대한 권리는 다음 각 항과 같습니다.\n" +
                        "① 서비스가 작성한 게시물 또는 저작물에 대한 저작권 기타 저작재산권은 서비스에 귀속합니다.\n" +
                        "② 회원이 서비스 내에 게시한 게시물에 대한 권리와 책임은 게시자에게 있으며 서비스는 게시자의 동의 없이는 이를 서비스 내 게재 이외에 영리적 목적으로 사용할 수 없습니다. 단, 비영리적인 경우에는 그러하지 아니하며 또한 서비스는 서비스 내에서의 게재권을 갖습니다.\n" +
                        "③ 회원은 서비스를 이용하여 얻은 정보를 가공, 판매하는 행위 등 서비스에 게재된 자료를 서비스의 동의없이 상업적으로 사용할 수 없습니다.\n" +
                        "제 18 조 (서비스 이용 책임)\n" +
                        "회원은 서비스에서 권한 있는 사원이 서명한 명시적인 서면에 구체적으로 허용한 경우를 제외하고는 서비스를 이용하여 상품을 판매하는 등의 영업활동을 할 수 없으며 특히 해킹, 돈벌이 광고, 음란 사이트 등을 통한 상업행위, 상용S/W 불법배포 등을 할 수 없습니다. 이를 어기고 발생한 영업활동의 결과 및 손실, 관계기관에 의한 구속 등 법적 조치 등에 관해서 서비스는 책임이 없습니다.\n" +
                        "제 5 장 계약 해지 및 이용 제한\n" +
                        "제 19 조 (계약 해지 및 변경)\n" +
                        "① 회원이 이용 계약을 해지하고자 하는 경우에는 회원 본인이 서비스 내의 회원탈퇴 메뉴를 통하여 가입해지를 해야 합니다.\n" +
                        "② 회원이 이용 계약을 해지하는 경우 서비스는 개인정보보호정책에 따라 회원 등록을 말소합니다. 이 경우 회원에게 이를 통지합니다.\n" +
                        "제 20 조 (서비스 이용 제한 등)\n" +
                        "① 서비스는 회원이 서비스 이용내용에 있어서 본 약관 제 12 조 내용을 위반하거나, 다음 각 호에 해당하는 행위를 하였을 경우 사전통지 없이 이용계약을 해지하거나 또는 기간을 정하여 서비스 이용을 제한할 수 있습니다.\n" +
                        "35. 타인의 개인정보, ID 및 비밀번호를 도용한 경우\n" +
                        "36. 가입한 이름이 실명이 아닌 경우\n" +
                        "37. 타인의 명예를 손상시키거나 불이익을 주는 행위를 한 경우\n" +
                        "38. 서비스, 다른 회원 또는 제 3자의 저작재산권을 침해하는 경우\n" +
                        "39. 공공질서 및 미풍양속에 저해되는 내용을 고의로 유포시킨 경우\n" +
                        "40. 회원이 국익 또는 사회적 공익을 저해할 목적으로 서비스 이용을 계획 또는 실행하는 경우\n" +
                        "41. 서비스 운영을 고의로 방해한 경우\n" +
                        "42. 서비스의 안정적 운영을 방해할 목적으로 다량의 정보를 전송하거나 광고성 정보를 전송하는 경우\n" +
                        "43. 정보통신설비의 오작동이나 정보의 파괴를 유발시키는 컴퓨터 바이러스 프로그램 등을 유포하는 경우\n" +
                        "44. 정보통신윤리위원회 등 외부기관의 시정요구가 있거나 불법선거운동과 관련하여 선거관리위원회의 유권해석을 받은 경우\n" +
                        "45. 서비스를 이용하여 얻은 정보를 서비스의 사전 승낙 없이 복제 또는 유통시키거나 상업적으로 이용하는 경우\n" +
                        "46. 회원이 음란물을 게재하거나 음란 사이트를 링크하는 경우\n" +
                        "47. 본 약관을 포함하여 기타 서비스가 정한 이용 조건에 위반한 경우\n" +
                        "제 6 장 손해배상 및 기타 사항\n" +
                        "제 21 조 (손해배상)\n" +
                        "① 회사와 이용자는 서비스 이용과 관련하여 고의 또는 과실로 상대방에게 손해를 끼친 경우에는 이를 배상하여야 한다.\n" +
                        "② 회사는 무료로 제공하는 서비스의 이용과 관련하여 개인정보보호정책에서 정하는 내용에 위반하지 않는 한 어떠한 손해도 책임을 지지 않습니다.\n" +
                        "제 22 조 (면책조항)\n" +
                        "① 서비스는 천재지변 또는 이에 준하는 불가항력으로 인하여 서비스를 제공할 수 없는 경우에는 서비스 제공에 관한 책임이 면제됩니다.\n" +
                        "② 서비스는 회원의 귀책사유로 인한 서비스 이용의 장애에 대하여 책임이 면제됩니다.\n" +
                        "③ 서비스는 회원이 서비스를 이용하여 기대하는 수익을 상실한 것이나 서비스를 통하여 얻은 자료로 인한 손해에 관하여 책임을 지지 않습니다.\n" +
                        "④ 서비스는 회원이 서비스에 저장, 게시 또는 전송한 정보, 자료, 사실의 신뢰도, 정확성 등 내용에 관하여는 책임을 지지 않습니다.\n" +
                        "⑤ 서비스는 서비스 이용과 관련하여 가입자에게 발생한 손해 가운데 가입자의 고의, 과실에 의한 손해에 대하여 책임을 지지 않습니다.\n" +
                        "⑥ 서비스에서 주최하는 이벤트 성 행사에 참여한 당첨자의 경품 발송 후 장기간(발송일로부터 60일 이후) 경품 미 수령 사실을 통보하지 않은 회원에 대해서는 책임이 없습니다.\n" +
                        "⑦ 서비스가 정한 기준 이외의 서비스 운영에 대한 모든 세부 업무를 회원에게 공지할 책임을 지지 않습니다.\n" +
                        "⑧ 이용자의 저작권 위반, 보관 부실, 불법복제, 소스변형 등과 관련하여 발생하는 모든 종류의 손해에 대해 책임을 지지 않습니다.\n" +
                        "제 24 조 (분쟁의 해결 및 관할)\n" +
                        "① 서비스 이용으로 발생한 분쟁에 대해 소송이 제기될 경우 서비스의 본사 소재지를 관할하는 법원을 전속 관할법원으로 합니다.\n" +
                        "② 서비스와 이용자 간에 제기된 소송에는 대한민국 국법을 적용합니다.\n" +
                        "제 25 조 (통지)\n" +
                        "① 서비스가 회원에 대하여 통지를 하는 경우 서비스가 회사에 등록한 전자우편 주소로 할 수 있습니다.\n" +
                        "② 서비스는 불특정다수 회원에게 통지를 해야 할 경우 공지 게시판을 통해 7일 이상 게시함으로써 개별 통지에 갈음할 수 있습니다.")
            }
        }
    }
}