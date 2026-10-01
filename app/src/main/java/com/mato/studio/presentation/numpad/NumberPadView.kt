package com.mato.studio.presentation.numpad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mato.studio.R
import com.mato.studio.network.response.Currency
import com.mato.studio.ui.theme.DarkBlue


@Composable
fun NumberPadSheet(
    isDeposit: Boolean,
    selectedCurrency: Currency,
    amountInput: String,
    onCurrencySelect: (Currency) -> Unit,
    onKeyClick: (String) -> Unit,
    onAllClear: () -> Unit,
    onBackspace: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var currencyDropdownExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        color = DarkBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF334155))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isDeposit) stringResource(R.string.deposit_funds) else stringResource(R.string.withdraw_funds),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.32f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Blue)
                        .clickable(enabled = isDeposit && !isLoading) {
                            if (isDeposit) currencyDropdownExpanded = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = if (isDeposit) selectedCurrency.symbol else stringResource(R.string.rupee_symbol),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isDeposit) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.select_currency),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }


                    DropdownMenu(
                        expanded = currencyDropdownExpanded,
                        onDismissRequest = { currencyDropdownExpanded = false },
                        modifier = Modifier
                            .background(Color(0xFF1E293B))
                            .height(280.dp)
                    ) {
                        Currency.entries.forEach { curr ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${curr.code} (${curr.symbol})",
                                            color = if (curr == selectedCurrency) Color.Cyan else Color.White,
                                            fontWeight = if (curr == selectedCurrency) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                },
                                onClick = {
                                    onCurrencySelect(curr)
                                    currencyDropdownExpanded = false
                                }
                            )
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .weight(0.68f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = if (amountInput.isEmpty()) "0" else amountInput,
                        color = Color.DarkGray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NumpadKey(label = "7", modifier = Modifier.weight(1f)) { onKeyClick("7") }
                        NumpadKey(label = "8", modifier = Modifier.weight(1f)) { onKeyClick("8") }
                        NumpadKey(label = "9", modifier = Modifier.weight(1f)) { onKeyClick("9") }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NumpadKey(label = "4", modifier = Modifier.weight(1f)) { onKeyClick("4") }
                        NumpadKey(label = "5", modifier = Modifier.weight(1f)) { onKeyClick("5") }
                        NumpadKey(label = "6", modifier = Modifier.weight(1f)) { onKeyClick("6") }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Gray)
                        .clickable(enabled = !isLoading) { onAllClear() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.ac_key),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(61.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NumpadKey(label = "1", modifier = Modifier.weight(1f)) { onKeyClick("1") }
                NumpadKey(label = "2", modifier = Modifier.weight(1f)) { onKeyClick("2") }
                NumpadKey(label = "3", modifier = Modifier.weight(1f)) { onKeyClick("3") }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Red)
                        .clickable(enabled = !isLoading) { onBackspace() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                        contentDescription = stringResource(R.string.backspace),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(61.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NumpadKey(label = "0", modifier = Modifier.weight(1f)) { onKeyClick("0") }
                NumpadKey(label = "00", modifier = Modifier.weight(1f)) { onKeyClick("00") }
                NumpadKey(label = ".", modifier = Modifier.weight(1f)) { onKeyClick(".") }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.Green)
                        .clickable(enabled = !isLoading && amountInput.isNotEmpty() && (amountInput.toDoubleOrNull() ?: 0.0) > 0) {
                            onConfirm()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.confirm),
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NumpadKey(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Gray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
