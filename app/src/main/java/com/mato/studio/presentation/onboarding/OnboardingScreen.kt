package com.mato.studio.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mato.studio.R

@Composable
fun OnBoardingScreen(onStarted:()->Unit) {

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 60.dp),
        horizontalAlignment= Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        val annotatedString = buildAnnotatedString {
            append("Instantly Convert between over ")

            withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)) {
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("150 currencies")
                }
            }

            append(". Currency convert is your one stop solution for effortless currency conversion.")
        }
        Text(
            text = "Welcome to \nCurrency Convert!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(60.dp))

        Image(painter = painterResource(id = R.drawable.illustrator),
            contentDescription = "Onboarding",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = onStarted,
            modifier = Modifier.fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Gray
            )
        ) {
            Text(text = "Get Started")
        }

    }

    
}