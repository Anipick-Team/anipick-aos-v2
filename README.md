# AniPick (애니픽)

애니메이션 정보 탐색, 랭킹, 추천, 리뷰까지 한 곳에서 즐기는 안드로이드 애니메이션 커뮤니티 앱입니다.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-28-blue)
![Version](https://img.shields.io/badge/version-1.0.0-informational)

## 소개

AniPick은 애니메이션 정보를 탐색하고, 랭킹과 추천을 통해 새로운 작품을 발견하며, 직접 리뷰를 남기고 다른 사용자와 소통할 수 있는 서비스입니다. 기존 프로젝트를 기반으로 아키텍처와 기술 스택을 새로 정비해 재구축하고 있는 버전입니다.

### 주요 기능
- 애니메이션/캐릭터/시리즈/성우/제작사 정보 탐색 및 검색
- 랭킹, 추천 기반 콘텐츠 발견
- 리뷰 작성 및 마이페이지를 통한 개인 활동 관리
- 이메일 회원가입/로그인, 카카오·구글 소셜 로그인 지원

## 기술 스택

**Language & UI**
- Kotlin
- Jetpack Compose, Material 3
- Navigation 3
- Splash Screen API

**Architecture & DI**
- Multi-module 기반 Clean Architecture (Presentation - Domain - Data)
- Koin, KSP

**비동기 처리**
- Kotlin Coroutines, Flow

**네트워크 & 데이터**
- Ktor Client
- Kotlinx Serialization
- Room, DataStore (Preferences)
- Coil3 (이미지 로딩)

**인증**
- Google Identity Services (Credential Manager)
- Kakao Login SDK

**배포**
- Play In-App Update
- OSS Licenses
