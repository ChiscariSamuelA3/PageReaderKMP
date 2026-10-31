package com.demo.pagereader.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.pagereader.theme.PageReaderTheme
import com.demo.pagereader.R
import com.demo.pagereader.models.PageReaderError
import com.demo.pagereader.ui.components.SectionCard
import com.demo.pagereader.presentation.PageState
import com.demo.pagereader.presentation.PageReaderViewModel
import com.demo.pagereader.presentation.ReadStatus

@Composable
fun PageReaderScreen(
    viewModel: PageReaderViewModel = viewModel(factory = PageReaderViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PageReaderContent(
        uiState = uiState,
        onUrlChange = viewModel::onUrlChange,
        onSelectorChange = viewModel::onSelectorChange,
        onReadClick = viewModel::onReadClick
    )
}

@Composable
private fun PageReaderContent(
    modifier: Modifier = Modifier,
    uiState: PageState,
    onUrlChange: (String) -> Unit,
    onSelectorChange: (String) -> Unit,
    onReadClick: () -> Unit
) {
    val isLoading = uiState.status is ReadStatus.Loading

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.app_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            SectionCard {
                Text(
                    text = stringResource(R.string.url_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.url,
                    onValueChange = onUrlChange,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.ic_link),
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Next
                    )
                )

                Text(
                    text = stringResource(R.string.selector_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.selector,
                    onValueChange = onSelectorChange,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.ic_code),
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(Modifier.height(4.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    onClick = onReadClick,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = LocalContentColor.current,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(if (isLoading) R.string.reading_button else R.string.read_button),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            SectionCard {
                Text(
                    text = stringResource(R.string.result_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                OutlinedTextField(
                    value = when (val status = uiState.status) {
                        ReadStatus.Idle, ReadStatus.Loading -> ""
                        is ReadStatus.Success -> status.text
                        is ReadStatus.Failure -> errorMessageMapper(status.error)
                    },
                    onValueChange = {},
                    readOnly = true,
                    isError = uiState.status is ReadStatus.Failure,
                    placeholder = { Text(stringResource(R.string.result_placeholder)) },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        errorTextColor = MaterialTheme.colorScheme.error,
                    ),
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun errorMessageMapper(error: PageReaderError): String = when (error) {
    PageReaderError.InvalidUrl -> stringResource(R.string.error_invalid_url)
    PageReaderError.InsecureUrl -> stringResource(R.string.error_insecure_url)
    PageReaderError.InvalidSelector -> stringResource(R.string.error_invalid_selector)
    is PageReaderError.Network -> stringResource(R.string.error_network, error.reason)
    is PageReaderError.Http -> stringResource(R.string.error_http, error.code)
    PageReaderError.ResponseTooLarge -> stringResource(R.string.error_response_too_large)
    is PageReaderError.NothingFound -> stringResource(R.string.error_nothing_found, error.selector)
}

@Preview(showBackground = true)
@Composable
private fun PageReaderPreview() {
    PageReaderTheme {
        PageReaderContent(
            uiState = PageState(status = ReadStatus.Success("Example Domain")),
            onUrlChange = {},
            onSelectorChange = {},
            onReadClick = {},
        )
    }
}
