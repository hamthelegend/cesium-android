package com.thebrownfoxx.cesium.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ContentCopy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.cesium.ui.theme.AppTheme
import com.thebrownfoxx.components.IconButton
import com.thebrownfoxx.components.extension.minus

@Composable
fun CopyField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val clipboardManager = LocalClipboardManager.current

    Surface(
        shape = shapes.small,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(PaddingValues(16.dp) - PaddingValues(end = 8.dp)),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = value,
                    fontFamily = FontFamily.Monospace,
                )
            }
            IconButton(
                imageVector = Icons.TwoTone.ContentCopy,
                contentDescription = null,
                onClick = { clipboardManager.setText(AnnotatedString(value)) },
            )
            if (actions != null) {
                actions()
            }
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@PreviewFontScale
@Preview
@Composable
fun CopyFieldPreview() {
    AppTheme {
        CopyField(
            label = "TOTP Secret",
            value = "KRUGKIDROVUWG2ZA",
            modifier = Modifier.padding(16.dp),
        )
    }
}