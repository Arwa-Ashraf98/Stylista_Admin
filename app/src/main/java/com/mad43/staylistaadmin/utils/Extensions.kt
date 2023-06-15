package com.mad43.staylistaadmin.utils


import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.mad43.staylistaadmin.R

fun Fragment.showToast(massage: String?) {
    Toast.makeText(requireContext(), "$massage", Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.loading)
        .error(R.drawable.error_image)
        .into(this)
}

fun Fragment.navigateToNextScreen(destination: Int) {
    findNavController().navigate(resId = destination)
}

fun View.visibilityInVisible() {
    this.visibility = View.INVISIBLE
}

fun View.visibilityGone() {
    this.visibility = View.GONE
}

fun View.visibilityVisible() {
    this.visibility = View.VISIBLE
}

fun MaterialButton.disableButton(){
    this.isClickable = false
}

fun Fragment.showSnackBarMessage(message: String) {
    val snack = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
    snack.show()
}

fun ProgressBar.showProgress() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hideProgress() {
    this.visibility = View.GONE
}

fun EditText.clearText() {
    this.setText("")
}

fun EditText.unEnableEditText() {
    this.isEnabled = false
}

fun EditText.enableEditText(){
    this.isEnabled = true
}

fun EditText.setErrorMessage(message: String) {
    this.error = message
}