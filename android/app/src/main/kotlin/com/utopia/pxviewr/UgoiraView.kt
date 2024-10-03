package com.utopia.UgoiraView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

class UgoiraView(context: Context) : ImageView(context) {
    private var models: ArrayList<UgoiraViewModel>? = null
    private var animationDrawable: AnimationDrawable = AnimationDrawable()
    private var width = 0
    private var height = 0

    private fun setupAnimationDrawable() {
        models?.let {
            for (model in it) {
                val drawable = BitmapDrawable(this.resources, model.bitmap)
                animationDrawable.addFrame(drawable, model.delay)
            }
        }

        animationDrawable.isOneShot = false
        animationDrawable.start()
        this.setImageDrawable(animationDrawable)
    }

    fun setImages(images: ReadableArray) {
        models = ArrayList(images.size())
        for (index in 0 until images.size()) {
            val map: ReadableMap = images.getMap(index)!!
            models?.add(UgoiraViewModel(map.getString("uri")!!, map.getInt("delay"), null))
        }
        maybeLoadImages()
    }

    private fun maybeLoadImages() {
        if (width > 0 && height > 0 && !models.isNullOrEmpty()) {
            val context = this.context
            Observable.fromIterable(models)
                .flatMap(Function<UgoiraViewModel, ObservableSource<UgoiraViewModel>> { model ->
                    Observable.create(ObservableOnSubscribe<UgoiraViewModel> { emitter ->
                        Glide.with(context).asBitmap()
                            .load(model.uri)
                            .into(object : SimpleTarget<Bitmap>(width, height) {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    model.bitmap = resource
                                    emitter.onNext(model)
                                    emitter.onComplete()
                                }
                            })
                    })
                }).subscribe(object : Observer<UgoiraViewModel> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(ugoiraViewModel: UgoiraViewModel) {}

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {
                        setupAnimationDrawable()
                    }
                })
        }
    }

    fun setWidth(width: Int) {
        this.width = width
        maybeLoadImages()
    }

    fun setHeight(height: Int) {
        this.height = height
        maybeLoadImages()
    }

    fun setImageScaleType(resizeMode: String) {
        val scaleType = when (resizeMode) {
            "contain" -> ScaleType.FIT_CENTER
            "cover" -> ScaleType.CENTER_CROP
            "stretch" -> ScaleType.FIT_XY
            "center" -> ScaleType.CENTER_INSIDE
            else -> ScaleType.CENTER_CROP
        }
        this.scaleType = scaleType
    }

    fun pauseAnimation() {
        if (animationDrawable.isRunning) {
            this.post { animationDrawable.stop() }
        }
    }

    fun resumeAnimation() {
        if (!animationDrawable.isRunning) {
            this.post { animationDrawable.start() }
        }
    }
}
