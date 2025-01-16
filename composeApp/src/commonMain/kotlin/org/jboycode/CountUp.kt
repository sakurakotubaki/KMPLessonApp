package org.jboycode

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun CountUp() {
    val count = remember { mutableStateOf(0) }
    Text("Count: ${count.value}")
    Button(onClick = { count.value++ }) {
        Text("Increment")
    }
}