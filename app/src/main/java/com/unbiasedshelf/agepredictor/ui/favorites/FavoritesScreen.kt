package com.unbiasedshelf.agepredictor.ui.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.unbiasedshelf.agepredictor.R
import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import com.unbiasedshelf.agepredictor.data.repository.Status
import com.unbiasedshelf.agepredictor.ui.common.AgifyButton
import com.unbiasedshelf.agepredictor.ui.common.showToast
import com.unbiasedshelf.agepredictor.ui.theme.Gray
import com.unbiasedshelf.agepredictor.ui.theme.Gray2


@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBackPressed: () -> Unit,
    onBlurredChange: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    val context = LocalContext.current

    LaunchedEffect(viewModel.favorites) {
        if (viewModel.favorites is Status.Error) {
            viewModel.favorites.showToast(context)
        }
    }

    val namesToRemove = remember { mutableStateListOf<String>() }

    var isSelectionMode by remember {
        mutableStateOf(false)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(showDeleteDialog) {
        onBlurredChange(showDeleteDialog)
    }

    val onSelectionModeChanged: (Boolean) -> Unit = remember {
        {
            if (!it) {
                namesToRemove.clear()
            }
            isSelectionMode = it
        }
    }

    BackHandler {
        when {
            showDeleteDialog -> showDeleteDialog = false
            isSelectionMode -> onSelectionModeChanged(false)
            else -> onBackPressed()
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.favorites),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(top = 36.dp, bottom = 25.dp)
        )

        if (viewModel.favorites is Status.Success) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                FavoritesList(
                    agePredictions = (viewModel.favorites as Status.Success).value,
                    selectedNames = namesToRemove,
                    onItemSelected = { name ->
                        if (namesToRemove.contains(name))
                            namesToRemove.remove(name)
                        else
                            namesToRemove.add(name)
                    },
                    isSelectionMode = isSelectionMode,
                    onSelectionModeChanged = onSelectionModeChanged
                )
            }
        }

        AnimatedVisibility(
            visible = namesToRemove.isNotEmpty(),
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            AgifyButton(
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier
                    .padding(bottom = 50.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.delete))
            }
        }
    }

    if (showDeleteDialog) {
        DeleteDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmClick = {
                val result = viewModel.removeFromFavorites(namesToRemove)
                onSelectionModeChanged(false)
                viewModel.getFavorites()
                showDeleteDialog = false
                result.showToast(context)
            }
        )
    }
}

@Composable
private fun FavoritesList(
    agePredictions: List<AgePrediction>,
    selectedNames: List<String>,
    onItemSelected: (String) -> Unit,
    isSelectionMode: Boolean,
    onSelectionModeChanged: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        agePredictions.forEach {
            FavoriteItem(
                name = it.name,
                isSelectionMode = isSelectionMode,
                isSelected = selectedNames.contains(it.name),
                onClick = onItemSelected,
                onLongClick = {
                    if (!isSelectionMode) {
                        onSelectionModeChanged(true)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteItem(
    name: String,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .height(50.dp)
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = { if (isSelectionMode) onClick(name) },
                onLongClick = { if (!isSelectionMode) onLongClick(name) }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            if (isSelectionMode) {
                val icon = painterResource(
                    id = if (isSelected) R.drawable.ic_checkbox_on else R.drawable.ic_checkbox_off
                )
                Icon(
                    painter = icon,
                    contentDescription = "checkbox",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Popup(
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            dismissOnBackPress = true
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                )
        ) {
            DeleteDialogCard(
                onDismissRequest = onDismissRequest,
                onConfirmClick = onConfirmClick
            )
        }
    }
}

@Composable
private fun DeleteDialogCard(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Card(
        elevation = 0.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.large)
            .clickable(enabled = false) { }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.dialog_title),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 10.dp)
            )
            Text(
                text = stringResource(R.string.dialog_message),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colors.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
            )

            Divider(
                color = Gray2,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(onClick = onDismissRequest)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_decline),
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            lineHeight = 21.sp
                        ),
                        color = Gray
                    )
                }
                Divider(
                    color = Gray2,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(onClick = onConfirmClick)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_confirm),
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 18.sp,
                            lineHeight = 21.sp
                        ),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    }
}