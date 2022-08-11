package com.example.appfilmecatalogo.presenter.helpers

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.appfilmecatalogo.presenter.viewmodel.Movie.MovieDetailsViewModel
import com.facebook.shimmer.ShimmerFrameLayout

class ImageDetailListener(
    private val shimmerFrameLayout: ShimmerFrameLayout,
    private val movieDetailsViewModel: MovieDetailsViewModel
) :
    RequestListener<Drawable?> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable?>?,
        isFirstResource: Boolean,
    ): Boolean {
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable?>?,
        dataSource: DataSource?,
        isFirstResource: Boolean,
    ): Boolean {
        movieDetailsViewModel.imageDetailSelected(resource?.toBitmap())
        shimmerFrameLayout.hideShimmer()
        return false
    }
}
