package com.unbiasedshelf.agepredictor.ui.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unbiasedshelf.agepredictor.R
import com.unbiasedshelf.agepredictor.data.repository.Status
import com.unbiasedshelf.agepredictor.ui.common.AgifyButton
import com.unbiasedshelf.agepredictor.ui.common.showToast
import com.unbiasedshelf.agepredictor.ui.theme.Gray1
import com.unbiasedshelf.agepredictor.ui.theme.Gray3
import com.unbiasedshelf.agepredictor.ui.theme.SearchLabelColor
import com.unbiasedshelf.agepredictor.ui.theme.SearchTextColor

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 18.dp)) {
        SearchBar(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            onSearchClick = { viewModel.getAge() }
        )

        val context = LocalContext.current
        LaunchedEffect(viewModel.ageStatus) {
            if (viewModel.ageStatus is Status.Error) {
                viewModel.ageStatus?.showToast(context)
            }
        }

        if (viewModel.ageStatus is Status.Success) {
            (viewModel.ageStatus as? Status.Success<Int>)?.value?.let { age ->
                AgeContent(
                    age = age,
                    onAddToFavoritesClick = {
                        val result = viewModel.addToFavorites()
                        result.showToast(context)
                    },
                    onShareClick = { context.share(viewModel.name, age) }
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
            Placeholder(text = stringResource(R.string.placeholder_text))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = stringResource(R.string.search),
            tint = Gray1,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(20.dp)
        )
        SearchTextField(
            value = value,
            placeholder = stringResource(R.string.search),
            onValueChange = onValueChange,
            onSearchClick = onSearchClick
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Words
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick()
                keyboardController?.hide()
            }
        ),
        singleLine = true,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.body2
                    )
                },
                label = null,
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = SearchTextColor,
                    cursorColor = SearchTextColor,
                    placeholderColor = SearchLabelColor
                ),
                contentPadding = PaddingValues(0.dp)
            )
        },
        textStyle = MaterialTheme.typography.body2,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Placeholder(text: String) {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun AgeContent(
    age: Int,
    onAddToFavoritesClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 54.dp, bottom = 50.dp)
    ) {
        AgeCircle(age = age)

        Row {
            AgifyButton(
                onClick = onAddToFavoritesClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(text = stringResource(R.string.add_to_fav))
            }
            AgifyButton(onClick = onShareClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = stringResource(R.string.share),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun AgeCircle(age: Int) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.secondary)
    ) {
        Text(
            text = age.toString(),
            style = MaterialTheme.typography.h1,
            color = Gray3,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun Context.share(name: String, age: Int) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message, name, age))
    }
    startActivity(intent)
}