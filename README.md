# Post IT 프로젝트를 하며 얻은 것

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

일일 retrofit 메서드에 header 어노테이션을 사용하여 토큰을 부여할 수 있지만

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

---

# Infinite Scroll (무한 스크롤)

한번에 모든 게시글을 불러오는 행위는 자원 낭비가 너무 심하다.

서버에서 가져오는 데이터를 나누기 위해서는 페이지를 만드는 방법(페이지네이션)도 있지만 모바일의 경우 페이지네이션 방식보다 무한 스크롤을 구현하는 것이 사용자가 더 좋은 경험을 할 수 있게 한다,

무한 스크롤의 기본적인 원리는

1. 서버 측에서 분리된 데이터를 받는다
2. 사용자의 RecyclerView 스크롤의 위치를 확인한다.
3. 스크롤의 위치가 끝에 도달하면 더 많은 데이터를 가져온다.

#### recyclerview.addOnScrollListener

onScrollListener안의 onScrolled 메서드는 recyclerview의 스크롤 변화를 감지한다. 

무한 스크롤을 구현할 때에는 onScrolled 메서드에서 스크롤의 움직임을 감지하여 리스트의 마지막에 도달했는지 확인한다.

**Activity**

```kotlin
override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)
    //리스트에서 마지막으로 보이는 position이 리스트의 마지막 인덱스와 같은지 검사한다 (나의 경우에는 리스트에 기본값이 있어서 -1을 하였다)
    if (layoutManager != null && 
        layoutManager.findLastCompletelyVisibleItemPosition() == boardIdxList.lastIndex - 1) {
        if (MORE_LOADING && !LOADING) // 추가로 로딩할 게시판이 있는지, 현재 로딩 중인지을 체크하는 변수
        {
            LOADING = true // 현재 로딩 중
            rv_board.post {
                boardAdapter.showProgressBar() // recyclerView에 ProgressBar를 띄움
                loadBoard() //추가 데이터 로드
            }
        }
    }
}
```

#### Adapter.getItemViewType

무한 스크롤 뿐 아니라 recyclerview에서 여러 Layout을 inflate하려면 ViewType에 따라 ViewHolder를 바꾸어주는 작업이 필요하다.

View에 맞는 ViewHolder를 만든 뒤 Adapter에서 getItemViewType을 override한다.

**getItemViewType**

```kotlin
override fun getItemViewType(position: Int): Int {
    val board = boardList[position]
    if (null != board?.images) {
        return 0
    } else if (null != board) {
        return 1
    } else {
        return -1
    }
}
```

전달받은 데이터에 따라 ViewType을 설정한다.

(나의 경우 이미지가 포함된 View라면 0, 이미지가 포함되지 않는 경우 1, 그 외 (progressbar)는 -1로 반환하였다.)

**onCreateViewHolder**

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    context = parent.context
    when (viewType) {
        0 -> {
            return BoardViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_board_item, parent, false)
            )
        }
        1 -> {
            return BoardViewHolderNoImages(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_board_item_no_image, parent, false)
            )
        }
        else -> {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_loading, parent, false)
            )
        }
    }
}
```

onCreateViewHolder에서 viewType에 대해 각자 다른 ViewHolder에게 return 해준다.

**onBindViewHolder**

```kotlin
override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is BoardViewHolder) {
        holder.bind(boardList[position]!!)
    } else if (holder is BoardViewHolderNoImages) {
        holder.bind(boardList[position]!!)
    }
}
```

onBindViewHolder에서 holder에 따라 bind를 시켜주면 된다.
