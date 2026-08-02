# OkHttp(Ktor OkHttp 엔진)가 선택적으로 참조하는 TLS 프로바이더들. 이 프로젝트는 사용하지 않으므로 경고만 억제한다.
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# 카카오 SDK 공식 R8 규칙(https://developers.kakao.com/docs/latest/ko/android/getting-started).
# 내부적으로 Retrofit을 리플렉션 기반으로 쓰는데, 이 부분이 축소/난독화되면
# "Call return type must be parameterized" 같은 런타임 크래시가 난다.
-keep class com.kakao.sdk.**.model.* { <fields>; }

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking class retrofit2.Response
