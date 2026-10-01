package com.mato.studio.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mato.studio.R
import com.mato.studio.core.common.ResultState
import com.mato.studio.presentation.history.HistorySection
import com.mato.studio.presentation.numpad.NumberPadSheet
import com.mato.studio.ui.theme.DarkBlue
import com.mato.studio.ui.theme.DarkGrey
import com.mato.studio.ui.theme.GreyBlue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val balance by viewModel.balance.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val activeSheet by viewModel.activeSheet.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val amountInput by viewModel.amountInput.collectAsState()
    val operationState by viewModel.operationState.collectAsState()
    val isHistoryExpanded by viewModel.isHistoryExpanded.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val successMessage = stringResource(R.string.transaction_successful)

    LaunchedEffect(operationState) {
        when (operationState) {
            is ResultState.Success -> {
                snackbarHostState.showSnackbar(successMessage)
            }
            is ResultState.Error -> {
                snackbarHostState.showSnackbar((operationState as ResultState.Error).message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Blue
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val totalHeight = maxHeight
            val minimizedHeight = totalHeight * 0.2f
            val maxExpandedHeight = totalHeight * 0.64f

            val estimatedContentHeight = if (transactions.isEmpty()) {
                210.dp
            } else {
                72.dp + (transactions.size * 86).dp
            }
            val expandedTargetHeight = estimatedContentHeight.coerceIn(minimizedHeight, maxExpandedHeight)

            val targetHeight = if (isHistoryExpanded) expandedTargetHeight else minimizedHeight
            val animatedHeight by animateDpAsState(
                targetValue = targetHeight,
                animationSpec = tween(durationMillis = 350),
                label = "historySheetHeight"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = minimizedHeight)
                    .verticalScroll(rememberScrollState())
            ) {
                HeaderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.available_balance),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "₹ ${"%,.2f".format(balance)}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Button(
                        onClick = { viewModel.openWithdrawSheet() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = GreyBlue
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.withdraw),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.openDepositSheet() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkBlue,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.deposit),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                HistorySection(
                    transactions = transactions,
                    isExpanded = isHistoryExpanded,
                    onToggleExpand = { viewModel.toggleHistoryExpand() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            AnimatedVisibility(
                visible = activeSheet != ActiveSheet.NONE,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { viewModel.closeSheet() }
                )
            }

            AnimatedVisibility(
                visible = activeSheet != ActiveSheet.NONE,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                val isLoading = operationState is ResultState.Loading
                val errorMessage = (operationState as? ResultState.Error)?.message

                NumberPadSheet(
                    isDeposit = activeSheet == ActiveSheet.DEPOSIT,
                    selectedCurrency = selectedCurrency,
                    amountInput = amountInput,
                    onCurrencySelect = { viewModel.selectCurrency(it) },
                    onKeyClick = { viewModel.onNumpadKey(it) },
                    onAllClear = { viewModel.onNumpadClear() },
                    onBackspace = { viewModel.onNumpadBackspace() },
                    onConfirm = { viewModel.confirmOperation() },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = stringResource(R.string.user_img),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(R.string.good_morning),
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.user_name),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkGrey),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.notif),
                    contentDescription = stringResource(R.string.notifications),
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkGrey),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.setting),
                    contentDescription = stringResource(R.string.settings),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
