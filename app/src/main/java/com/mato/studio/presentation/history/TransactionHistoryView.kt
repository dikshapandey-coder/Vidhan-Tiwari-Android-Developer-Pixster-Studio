package com.mato.studio.presentation.history

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mato.studio.R
import com.mato.studio.network.response.TransactionModel
import com.mato.studio.network.response.TransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import com.mato.studio.ui.theme.DarkBlue
import com.mato.studio.ui.theme.LightGreen
import com.mato.studio.ui.theme.LightRed

@Composable
fun HistorySection(
    transactions: List<TransactionModel>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 6.dp)
                .clickable { onToggleExpand() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFE2E8F0))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clickable { onToggleExpand() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.history_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Text(
                text = if (isExpanded) stringResource(R.string.history_collapse) else stringResource(R.string.history_expand),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF94A3B8),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onToggleExpand() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        if (transactions.isEmpty()) {
            EmptyHistoryView(modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = transactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: TransactionModel,
    modifier: Modifier = Modifier
) {
    val isDeposit = transaction.type == TransactionType.DEPOSIT
    val iconBgColor = if (isDeposit) LightGreen else LightRed

    val dateFormat = SimpleDateFormat("dd MMM yyyy", LocalLocale.current.platformLocale)
    val formattedDate = dateFormat.format(Date(transaction.timestamp))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    id = if (isDeposit) {
                        R.drawable.deposit
                    } else {
                        R.drawable.withdrawal
                    }
                ),
                contentDescription = if (isDeposit) {
                    stringResource(R.string.deposit_action)
                } else {
                    stringResource(R.string.withdrawal_action)
                },
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.transactionCode,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formattedDate,
                fontSize = 12.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Normal
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (isDeposit && transaction.currencyCode != "INR") {
                Text(
                    text = "${transaction.currencySymbol}${"%,.2f".format(transaction.originalAmount)}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "≈ ₹${"%,.2f".format(transaction.amountInr)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
            } else {
                val prefix = if (isDeposit) "+" else "-"
                val textColor = if (isDeposit) DarkBlue else Color.Red
                Text(
                    text = "$prefix ₹${"%,.2f".format(transaction.amountInr)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.no_data),
            contentDescription = stringResource(R.string.no_data),
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
    }
}
