package com.thebrownfoxx.cesium.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.cesium.ui.theme.AppTheme
import com.thebrownfoxx.components.animation.sharedYAxisEnter
import com.thebrownfoxx.components.extension.bringIntoViewOnFocus
import com.thebrownfoxx.components.extension.indicationlessClickable
import com.thebrownfoxx.components.extension.plus

fun <T> List<T>?.getListState(emptyText: String, searching: Boolean) = when {
    this == null -> ListState.Loading
    isEmpty() && searching -> ListState.NoSearchResults
    isEmpty() -> ListState.Empty(text = emptyText)
    else -> ListState.Loaded
}

sealed class ListState {
    data object Loading : ListState()
    data class Empty(val text: String) : ListState()
    data object NoSearchResults : ListState()
    data object Loaded : ListState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableLazyColumnScreen(
    topBarExpandedContent: @Composable BoxScope.() -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    listState: ListState = ListState.Loaded,
    onNavigateUp: (() -> Unit)? = null,
    background: @Composable BoxScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentPadding: PaddingValues = PaddingValues(),
    content: LazyListScope.() -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .indicationlessClickable { focusManager.clearFocus() }
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            SearchTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onNavigateUp = onNavigateUp,
                background = background,
                scrollBehavior = scrollBehavior,
                content = topBarExpandedContent,
                modifier = Modifier.bringIntoViewOnFocus {
                    val topAppBarState = scrollBehavior.state
                    AnimationState(initialValue = topAppBarState.heightOffset).animateTo(
                        targetValue = topAppBarState.heightOffsetLimit,
                        animationSpec = scrollBehavior.snapAnimationSpec!!,
                    ) { topAppBarState.heightOffset = value }
                }
            )
        },
    ) { scaffoldContentPadding ->
        val indicatorModifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(scaffoldContentPadding)
        val density = LocalDensity.current

        AnimatedContent(
            targetState = listState,
            transitionSpec = {
                if (initialState == ListState.Loading && targetState == ListState.Loaded) {
                    with(density) {
                        sharedYAxisEnter() togetherWith
                                fadeOut(animationSpec = tween(90))
                    }
                } else {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(220, delayMillis = 90)
                            ))
                        .togetherWith(fadeOut(animationSpec = tween(90)))
                }
            },
            label = "",
        ) { listState ->
            when (listState) {
                ListState.Loading -> LoadingIndicator(
                    modifier = indicatorModifier,
                )

                is ListState.Empty -> EmptyList(
                    text = listState.text,
                    modifier = indicatorModifier,
                )

                ListState.NoSearchResults -> NoSearchResults(
                    modifier = indicatorModifier,
                )

                ListState.Loaded -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    content = content,
                    contentPadding = scaffoldContentPadding + contentPadding,
                    state = lazyListState,
                    verticalArrangement = verticalArrangement,
                )
            }
        }
    }

    LaunchedEffect(searchQuery) {
        lazyListState.animateScrollToItem(0)
    }
}

@Composable
fun SearchableLazyColumnScreen(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    listState: ListState = ListState.Loaded,
    onNavigateUp: (() -> Unit)? = null,
    background: @Composable BoxScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: LazyListScope.() -> Unit,
) {
    SearchableLazyColumnScreen(
        modifier = modifier,
        topBarExpandedContent = { Text(text = title) },
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        listState = listState,
        onNavigateUp = onNavigateUp,
        background = background,
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        verticalArrangement = verticalArrangement,
        contentPadding = contentPadding,
        content = content,
    )
}

@Preview
@Composable
fun SearchableLazyColumnScreenPreview() {
    AppTheme {
        SearchableLazyColumnScreen(
            title = "Enchantments",
            searchQuery = "",
            onSearchQueryChange = {},
            content = {},
        )
    }
}
