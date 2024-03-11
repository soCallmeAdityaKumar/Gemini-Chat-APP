package com.example.chatapp.Screens

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chatapp.Navigation.Screen
import com.example.chatapp.R
import com.example.chatapp.data.Message
import com.example.chatapp.data.Network
import com.example.chatapp.gemini.GeminiRepo
import com.example.chatapp.ui.theme.BlueHeading
import com.example.chatapp.ui.theme.endlineColor
import com.example.chatapp.ui.theme.fontFamily
import com.example.chatapp.ui.theme.onlineColor
import com.example.chatapp.ui.theme.textGray
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(context: Context, navController: NavHostController) {
    val network=Network(context)

    var isAvailable by  remember {
        mutableStateOf(false)
    }
    var onlineText by remember {
        mutableStateOf("Online")
    }
    val handler= Handler()
    handler.postDelayed(object :Runnable{
        override fun run() {
            isAvailable=network.checkNetwork()
            handler.postDelayed(this,3*1000)
            Log.d("network",isAvailable.toString())

        }
    },3*1000)
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Tool_Bar(isAvailable, modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
            navController,
            onlineText)
        TextBox(isAvailable){
            onlineText=it
        }
    }
}

@Composable
fun MySnackbar(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(context) {
        snackbarHostState.showSnackbar(
            message = "Please Check Your Network!",
            actionLabel = "Dismiss",
            duration = SnackbarDuration.Short
        )
    }
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .fillMaxWidth()
            .background(endlineColor),
        snackbar = {
            Snackbar(
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text("Please check your network!!", color = Color.White)
                    }
                },
                action = {
                    TextButton(onClick = {
                        onDismiss()
                    }) {
                        Text("Dismiss",color=Color.White, fontFamily = fontFamily)
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBox(
    isAvailable: Boolean,
    lazyListState: LazyListState,
    newSize:Int,
    text: (String)->Unit,
) {
    var text by remember {
        mutableStateOf("")
    }
    val focusRequester= remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    var snackbarVisible by remember { mutableStateOf(false) }


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .padding(start = 10.dp, bottom = 10.dp, top = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                placeholder={Text("Write your message", fontFamily = fontFamily, fontSize = 12.sp)},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 8.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontFamily = fontFamily,
                ),
                singleLine = true,
            )

            IconButton(
                onClick = {
                    if(isAvailable){
                        if(text.isNotEmpty()) text(text)
                        focusManager.clearFocus()
                        text=""
                    }else{
                        snackbarVisible=true
                    }

                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(color = BlueHeading)

            ) {
                Icon(painter = painterResource(id = R.drawable.send), contentDescription = null, tint = Color.White)
            }
        }
        if(snackbarVisible){
            MySnackbar(onDismiss = {snackbarVisible=false})
        }
    }
}

@Composable
fun TextBox(
    isAvailable:Boolean,
    setOnline:(String)->Unit
) {
    var newtext by remember {
        mutableStateOf("")
    }
    var messages by remember {
        mutableStateOf(emptyList<Message>())
    }

    var newSize by remember {
        mutableStateOf(0)
    }


    var lazyState = rememberLazyListState()
    LaunchedEffect(newtext){
        val gemini=GeminiRepo()

        if (newtext.isNotEmpty()){ gemini.sendInput(newtext)
        Log.d("gemini->newtext",newtext)
            if(!isAvailable){
                return@LaunchedEffect
            }
        val resp=gemini.getResponse()

        messages=messages.toMutableList().apply {
            val time=System.currentTimeMillis()
            if(resp!=null) add(Message(false,time,resp))}
            setOnline("Online")

        }
        newSize=messages.size
        lazyState.scrollToItem(newSize-1)

    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White), verticalArrangement = Arrangement.SpaceBetween) {
            Message(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                    messages = messages

            ){
                lazyState=it
            }

            MessageBox(isAvailable, lazyListState = lazyState,newSize){
                newtext=it
                messages=messages.toMutableList().apply {
                    setOnline("Typing...")
                    val time=System.currentTimeMillis()
                    add(Message(true,time,it))
                }
                newSize=messages.size
            }

        }
    }
}

@Composable
fun Message(modifier: Modifier,messages:List<Message>,setState:(LazyListState)->Unit) {
    Log.d("message",messages.size.toString())
    val listState = rememberLazyListState()

    LazyColumn(
        state=listState,
            modifier= modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)){
            items(messages){mess->
                Log.d("message->LazyColumn->",mess.message)
                MessageItem(mess.sender,mess)
                setState(listState)
            }
    }
}

@Composable
fun MessageItem(sender:Boolean,message: Message) {
    val time= formatMilliseconds(message.time)
    if(sender){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 10.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.End
            ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(BlueHeading)
                .padding(vertical = 10.dp, horizontal = 20.dp)){
                SelectionContainer {
                    Text(text = message.message,color=Color.White, fontFamily = fontFamily)
                }

            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = time, fontSize = 10.sp, color = textGray, fontFamily = fontFamily)

        }
    }else{
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 40.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.Start
            ) {

            Box(modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(endlineColor)
                .padding(vertical = 10.dp, horizontal = 20.dp)){
                SelectionContainer {
                Text(text = message.message,color=Color.Black, fontFamily = fontFamily)
                }

            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = time, fontSize = 10.sp, color = textGray, fontFamily = fontFamily)

        }

    }

}


@Composable
fun Tool_Bar(
    online: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onlineText: String
) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.elevatedCardElevation(6.dp), shape = RoundedCornerShape(0f)) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            navController.navigate(Screen.Welcome.route)
        }) {
            Image(painter = painterResource(id = R.drawable.arrow_left), contentDescription ="arrow_left")
        }
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text("Gemini", fontFamily = fontFamily, fontSize = 20.sp, color = BlueHeading)
                if(online){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(
                            modifier = Modifier.size(7.dp)
                        ) {
                            drawCircle(
                                color = onlineColor, // Set your desired fill color
                                radius = size.minDimension / 2,
                                center = center
                            )
                        }
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(text = onlineText,fontFamily = fontFamily, color = onlineColor )

                    }

                }
            }
            Image(painter = painterResource(id = R.drawable.volume_high), contentDescription = "volume_high", modifier = Modifier.padding(start=148.dp))
            Image(painter = painterResource(id = R.drawable.export), contentDescription = "export")


        }
    }

    
}

fun formatMilliseconds(milliseconds: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateStr = dateFormat.format(calendar.time)

    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timeStr = timeFormat.format(calendar.time)

    return "$dateStr $timeStr"
}