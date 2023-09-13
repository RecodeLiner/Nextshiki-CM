
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.elements.CalendarCard
import com.rcl.nextshiki.theme.Theme.AppTheme

@Composable
@Preview
fun CalendarCardPrevew(){
    AppTheme{
        CalendarCard("temp", "/system/animes/original/56579.jpg?1694161076", "00")
    }
}