package com.example.projeto.expresions

import android.widget.ImageView
import com.example.projeto.R
import coil.load

fun ImageView.loadImage(
    url: String? = null,
    fallback: Int = R.drawable.no_image
) {
    load(url) {
        fallback(fallback)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}