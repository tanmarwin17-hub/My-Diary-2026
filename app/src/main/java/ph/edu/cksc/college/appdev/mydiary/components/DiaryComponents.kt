package ph.edu.cksc.college.appdev.mydiary.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.diary.SampleDiaryEntries
import ph.edu.cksc.college.appdev.mydiary.diary.moodList
import ph.edu.cksc.college.appdev.mydiary.ui.theme.MyDiaryTheme
import java.time.LocalDateTime

@Composable
fun DiaryEntryCard(entry: DiaryEntry) {
    // Parent Row containing the entire entry
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(), // Fill width to allow right-alignment
        verticalAlignment = androidx.compose.ui.Alignment.Top
    ) {
        // 1. LEFTMOST: Profile Image
        Icon(
            imageVector = moodList[entry.mood].icon,
            tint = moodList[entry.mood].color,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "SurfaceColor"
        )

        // 2. MIDDLE: Author and Message Body
        Column(modifier = Modifier
                .weight(1f) // Takes up remaining space, pushing the stars to the end
                .clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = entry.title,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = entry.content,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 3. RIGHTMOST: Star Icons
        Text(
            // Repeat the ★ character based on the star count
            text = "★".repeat(entry.star.coerceIn(0, 5)),
            color = Color(0xFFFFD700), // Yellow/Gold
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun DiaryList(entries: List<DiaryEntry>) {
    LazyColumn {
        items(entries) { entry ->
            DiaryEntryCard(entry)
        }
    }
}

@Preview
@Composable
fun PreviewDiaryList() {
    MyDiaryTheme {
        DiaryList(SampleDiaryEntries.entries)
    }
}

@Preview(
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun DiaryEntryCardPreview() {
    MyDiaryTheme {
        DiaryEntryCard(
            entry = DiaryEntry("1", 4, 5, "Lexi", "Hey, take a look at Jetpack Compose, it's great!",
                LocalDateTime.now().toString())
        )
    }
}