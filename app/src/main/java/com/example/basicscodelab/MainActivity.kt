package com.example.basicscodelab

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    // TODO: This state should be hoisted
    // by : delegate(대리자)
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    var shouldShowOnboardingSaveable by rememberSaveable { mutableStateOf(true) }              // 구성이 변경 되어도 유지 (화면 회전, 다크모드 등)

    if(shouldShowOnboarding) {
        OnboardingScreen(onContinueClicked = {shouldShowOnboarding = false})                        // 콜백 등록
    } else {
        Greetings()
    }
}

@Composable
fun Greetings(names: List<String> = List(100) { "$it" }) {    // it : index 값
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            LazyColumn {                                                              // 현재 화면 안에 있는 컴포저블만 랜더링
                item { Text("Header") }
                items(names) {name ->
                    Greeting(name = name)
                }
                item { Text("Bottom") }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    // 상태 값이 변경 되면 그 값에 따라 컴포저블 재 구성, 첫 구성 시 mutableStateOf 생성, 초기화 / 재 구성 시 생성,초기화 x
    // val 키워드 사용 : 내부의 값이 변경 되기 때문
    val expanded = rememberSaveable { mutableStateOf(false) }       // open 값 기억

//    val extraPadding = if(expanded.value) 48.dp else 0.dp

    // animateDpAsState : state = 애니메이션이 끝날 때까지 값이 지속적으로 갱신되는 State 객체를 반환 (expanded 값을 입력한 것과 같음)
    val extraPadding by animateDpAsState(
        targetValue = if(expanded.value) 48.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 2000
        )
    )

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {     // 머터리얼 사용 시 컬러에 따라 컨텐츠 색이 자동으로 변경
        Row(
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello")
                Text(text = "$name!")
            }

            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Show Less" else "Show more")
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick =  onContinueClicked
        ) {
            Text("Continue")
        }
    }
}
@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    BasicsCodelabTheme {
        Greetings()
    }
}
