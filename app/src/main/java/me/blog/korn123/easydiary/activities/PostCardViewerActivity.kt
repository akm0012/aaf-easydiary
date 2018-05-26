package me.blog.korn123.easydiary.activities

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import io.github.aafactory.commons.utils.ColorUtils
import kotlinx.android.synthetic.main.activity_post_card_viewer.*
import kotlinx.android.synthetic.main.content_post_card_viewer.*
import me.blog.korn123.commons.utils.FontUtils
import me.blog.korn123.easydiary.R
import me.blog.korn123.easydiary.adapters.PostcardAdapter
import me.blog.korn123.easydiary.extensions.config
import me.blog.korn123.easydiary.helper.DIARY_POSTCARD_DIRECTORY
import me.blog.korn123.easydiary.helper.POSTCARD_SEQUENCE
import me.blog.korn123.easydiary.helper.TransitionHelper
import java.io.File


/**
 * Created by CHO HANJOONG on 2018-05-18.
 */

class PostCardViewerActivity : EasyDiaryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_card_viewer)
        toolbar.setNavigationOnClickListener { onBackPressed() }
//        setSupportActionBar(toolbar)
        FontUtils.getTypeface(this, assets, config.settingFontName)?.let {
            toolbar_layout.setCollapsedTitleTypeface(it)
            toolbar_layout.setExpandedTitleTypeface(it)
        }

//        val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
//            flexWrap = FlexWrap.WRAP
//            flexDirection = FlexDirection.ROW
////            alignItems = AlignItems.FLEX_START
//            justifyContent = JustifyContent.FLEX_START 
//        }
        
        val spacesItemDecoration = SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.card_layout_padding))
        val gridLayoutManager = GridLayoutManager(this, 2)

        val listPostcard = File(Environment.getExternalStorageDirectory().absolutePath + DIARY_POSTCARD_DIRECTORY)
                .listFiles()
                .filter { it.extension.equals("jpg", true)}
                .sortedDescending()
        recyclerView.apply {
            layoutManager = gridLayoutManager
            addItemDecoration(spacesItemDecoration)
            adapter = PostcardAdapter(
                    this@PostCardViewerActivity,
                    listPostcard,
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val intent = Intent(this@PostCardViewerActivity, PostcardViewPagerActivity::class.java)
                        intent.putExtra(POSTCARD_SEQUENCE, position)
                        TransitionHelper.startActivityWithTransition(this@PostCardViewerActivity, intent)
                    }
            )
//            setHasFixedSize(true)
//            clipToPadding = false
        }
        if (listPostcard.isEmpty()) {
            infoMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            app_bar.setExpanded(false)
        }

        toolbarImage.setColorFilter(ColorUtils.adjustAlpha(config.primaryColor, 0.5F))
    }

    

    internal class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            when (position % 2) {
                0 -> {
                    outRect.right = space
                }
                else -> outRect.right = 0
            }
            
            when (position < 2) {
                true -> outRect.top = 0 
                false -> outRect.top = space 
            }
        }
    }
}