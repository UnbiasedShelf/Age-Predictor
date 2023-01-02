package com.unbiasedshelf.agepredictor.ui.composable.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.unbiasedshelf.agepredictor.R
import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import com.unbiasedshelf.agepredictor.ui.composable.common.AgifyButton


@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBackPressed: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        val namesToRemove = remember { mutableStateListOf<String>() }
        val isSelectionMode by remember {
            derivedStateOf { namesToRemove.isNotEmpty() }
        }

        Text(
            text = stringResource(id = R.string.favorites),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(top = 36.dp, bottom = 25.dp)
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            FavoritesList(
                agePredictions = viewModel.favorites,
                selectedNames = namesToRemove,
                onItemSelected = { name ->
                    if (namesToRemove.contains(name))
                        namesToRemove.remove(name)
                    else
                        namesToRemove.add(name)
                },
                onBackPressed = onBackPressed,
                isSelectionMode = isSelectionMode,
                onSelectionModeChanged = {
                    if (!it)
                        namesToRemove.clear()
                }
            )
        }
        //todo fix
        if (isSelectionMode) {
            AgifyButton(
                onClick = {
                    val isSuccessful = viewModel.removeFromFavorites(namesToRemove)
                    if (isSuccessful) {
                        namesToRemove.clear()
                        viewModel.getFavorites()
                    }
                },
                modifier = Modifier
//                    .height(55.dp)
//                    .padding(bottom = 50.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.delete))
            }
        }
    }
}

@Composable
private fun FavoritesList(
    agePredictions: List<AgePrediction>,
    selectedNames: List<String>,
    onItemSelected: (String) -> Unit,
    onBackPressed: () -> Unit,
    isSelectionMode: Boolean,
    onSelectionModeChanged: (Boolean) -> Unit
) {
    BackHandler {
        if (isSelectionMode)
            onSelectionModeChanged(false)
        else
            onBackPressed()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        agePredictions.forEach {
            FavoriteItem(
                name = it.name,
                isSelectionMode = isSelectionMode,
                isSelected = selectedNames.contains(it.name),
                onClick = onItemSelected,
                onLongClick = { name ->
                    if (!isSelectionMode) {
                        onSelectionModeChanged(true)
                        onItemSelected(name)
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
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}