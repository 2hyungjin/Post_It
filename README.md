# 배운 것들

## JWT

JSON Web Token의 약자로 안정성 있게 데이터를 교환하기 위해 사용

서버에서 발급받은 토큰을 사용하여 데이터를 교환함

#### 안드로이드에서의 jwt 로그인 과정

1. 아이디 비밀번호 등을 통해 사용자가 로그인을 함
2. 서버에서 토큰(access token)을 발급
3. 토큰을 저장소(preference)에 저장
4. 서버에 요청할 때 header에 토큰을 넣어 요청함

---

### Shared Preference

간단한 설정 값을 앱 내부의 DB에 저장하기 용이함 (앱 삭제시 데이터도 소거됨)

#### 사용방법

1. Preference Class 생성 후 preference 인스턴스 생성

```kotlin
class Prefs(context: Context) {
    private val prefNm="mPref"
    private val prefs=context.getSharedPreferences(prefNm,MODE_PRIVATE)
}
```

2. get set 메서드를 통해 관리

```kotlin
var token:String?
    get() = prefs.getString("token",null)
    set(value){
        prefs.edit().putString("token",value).apply()
    }
```

3. activity보다 먼저 시작하기 위해 application에서 실행

```kotlin
class App :Application(){
    companion object{
        lateinit var prefs:Prefs
    }
    override fun onCreate() {
        prefs=Prefs(applicationContext)
        super.onCreate()
    }
}
```



---

### OkHttp3 Interceptor 사용하기

일일히 retrofit 메서드에 header 어노테이션을 사용하여 토큰을 부여할 수 있지만

OkHttp3의 Interceptor를 사용하면 REST API를 요청할 때 요청을 가로채(intercept) 헤더를 붙인 뒤 다시 전송할 수 있게 해줌

#### 사용방법

1. Interceptor 생성

```kotlin
class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var req =
                chain.request().newBuilder().addHeader("Authorization", App.prefs.token).build()
            return chain.proceed(req)
        }
    }
```

Interceptor를 상속받아 intercept 메서드를 구현

chain에 "Authorization"이라는 key와 prefs의 value를 가진 헤더를 부여 후 다시 넘겨줌



2. Client 생성

```kotlin
val okHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
```

만들어 놓은 interceptor를 바탕으로 okHttpClient를 생성



3. Client와 retrofit의 인스턴스를 연결

```kotlin
val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
```

### 
