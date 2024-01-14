
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rcl.nextshiki.elements.CalendarCard
import com.rcl.nextshiki.theme.Theme.AppTheme

@Composable
@Preview
fun CalendarCardPreview(){
    AppTheme(seedColor = Color.Blue){
        CalendarCard("temp", "/system/animes/original/56579.jpg?1694161076", "00")
    }
}