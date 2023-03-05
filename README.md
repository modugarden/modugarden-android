# 모두의 정원

## 참가 인원
### 프론트엔드
|다나 (서주원)|마라 (이은섭)|양털 (김태윤)|펭귄 (오진영)|
|:------:|:---:|:------:|:---:|
| <img width="160px" src="https://avatars.githubusercontent.com/u/85955988?v=4"/> | <img width="160px" src="https://avatars.githubusercontent.com/u/107783650?v=4"/> | <img width="160px" src="https://avatars.githubusercontent.com/u/19889463?v=4"/> | <img width="160px" src="https://avatars.githubusercontent.com/u/99639919?v=4"/> |
|[joowojr](https://github.com/joowojr)|[MinchoGreenT](https://github.com/MinchoGreenT)|[kimtaeyoon20031220](https://github.com/kimtaeyoon20031220)|[OJOJIN](https://github.com/OJOJIN)|


## 프로젝트 소개
1. 페이지 분류, 기능 분류, (기능 상세) - [PascalCase] ex) PostLike(포스트 상세보기 페이지에서 좋아요 수 보기 창), SettingsProfileEdit(설정 페이지에서 프로필을 편집하는 창)
<br/>

## 사용한 기술


**layout 파일명 규칙**
1. activity/fragment_기능  ex) activity_login, fragment_home
<br/>

**layout id 규칙**
1. xml이름_세부기능_view이름  ex) home_story_rv, shop_brandid_tv
2. 세부 기능에는 _ 더 사용해도 괜찮아요

***view 이름***
1. recyclerView : rv
2. textView : tv
3. editText : et
4. button : btn
<br/>

**더미데이터 이름 규칙**
1. recyclerview 이름_숫자  ex)post_1, post_2
<br/>

**commit 규칙**
1. type: [파일명] 수정한 내용
2. type 
    - add : 새로운 기능을 추가할 때
    - modify : 기존 기능을 수정할 때
    - test : 테스트 코드를 올릴 때
3. ex) add: [LoginActivity] 로그인 레이아웃 추가 <br/>
       add: [MainActivity] 바텀 네비게이션 구현 <br/>
       modify : [LoginActivity] 로그인 레이아웃 수정
       
<br/>

**코드 주석 규칙 (kt 파일에 사용)**
1. 코드 위에 어떤 기능인지 설명  ex) 파이어베이스 연결, 좋아요 기능
2. 공통적으로 사용하는 변수를 제외한 애들은 선언 옆에  // 이 주석을 사용해서 설명해주기
3. 화면 전환 시 어느 화면에서 어느 화면으로 넘어가는지 설명
4. 자세하게 써주기
<br/>
