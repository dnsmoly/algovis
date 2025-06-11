package org.github.dnsmoly

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import algovis.composeapp.generated.resources.*
import org.github.dnsmoly.theme.AppTheme
import org.github.dnsmoly.theme.LocalThemeIsDark
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs

@Preview
@Composable
internal fun App() = AppTheme {
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.cyclone),
            fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
            style = MaterialTheme.typography.displayLarge
        )

        var isRotating by remember { mutableStateOf(false) }
        val moveRight by remember { mutableStateOf(true) }

        val rotate = remember { Animatable(0f) }
        val move = remember { Animatable(0f) }
        val moveTarget = 100f
        val target = -360f
        if (isRotating) {
            LaunchedEffect(Unit) {
                while (isActive) {
                    val remaining = (target - rotate.value) / target
                    println(rotate.value)
                    rotate.animateTo(target, animationSpec = tween((1_000 * remaining).toInt(), easing = LinearEasing))
                    rotate.snapTo(0f)
                    val remainingMove = (moveTarget - abs(move.value)) / moveTarget
                    move.animateTo(
                        moveTarget,
                        animationSpec = tween((1_000 * remainingMove).toInt(), easing = RepeatMode.Reverse)
                    )
                    move.snapTo(moveTarget)
                }
            }
        }

        Image(
            modifier = Modifier.size(250.dp).padding(16.dp).run {
                rotate(rotate.value)
//                offset(y = move.value.dp)
            },
            imageVector = vectorResource(Res.drawable.ic_cyclone),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = null
        )

        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { isRotating = !isRotating },
            content = {
                Icon(vectorResource(Res.drawable.ic_rotate_right), contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    stringResource(if (isRotating) Res.string.stop else Res.string.run)
                )
            })

        var isDark by LocalThemeIsDark.current
        val icon = remember(isDark) {
            if (isDark) Res.drawable.ic_light_mode
            else Res.drawable.ic_dark_mode
        }

        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { isDark = !isDark },
            content = {
                Icon(vectorResource(icon), contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(Res.string.theme))
            })

        val uriHandler = LocalUriHandler.current
        TextButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { uriHandler.openUri("https://github.com/terrakok") },
        ) {
            Text(stringResource(Res.string.open_github))
        }
    }
}
