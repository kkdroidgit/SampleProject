package com.example.android.sampleproject.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sampleproject.R
import com.example.android.sampleproject.model.BlogModel
import com.example.android.sampleproject.model.Bloggers
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.*
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapter(
    private val list: ArrayList<Bloggers>,
    var clickListener: OnBlogItemClickListener
) :
    RecyclerView.Adapter<CustomViewHolder>() {

    fun updateUsers(newList: List<Bloggers>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CustomViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }
}

class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imageView = view.imageView_blog
    private val title = view.textView_title
    private val about = view.textView_about
    private val shareIcon = view.share_button

    fun bind(bloggers: Bloggers, action: OnBlogItemClickListener) {
        title.text = bloggers.title
        about.text = bloggers.about
        Picasso.get().load(bloggers.image).into(imageView)
        val r = itemView.context as Activity

        shareIcon.setOnClickListener {

            Branch.getInstance().userCompletedAction("Share Clicked " + bloggers.title)


            val branchUniversalObject = BranchUniversalObject()
                .setCanonicalIdentifier(UUID.randomUUID().toString())
                .setTitle(bloggers.title)
                .setContentImageUrl(bloggers.image)
                .setContentDescription("Check out this blog on openAcademy")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(
                    ContentMetadata().addCustomMetadata("blogger_name", bloggers.title)
                        .addCustomMetadata("blogger_about", bloggers.about)
                        .addCustomMetadata("blogger_image", bloggers.image)
                        .addCustomMetadata("blog_url", bloggers.blogUrl)
                )

            val lp = LinkProperties()
                .setCampaign(bloggers.title)
                .addControlParameter("\$desktop_url", "r89zd.app.link")
                .addControlParameter("\$ios_url", "r89zd.app.link")

            val ss = ShareSheetStyle(itemView.context,
                "Check this out!",
                "This stuff is awesome: ")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With")


            branchUniversalObject.showShareSheet(r, lp, ss,
                object : Branch.BranchLinkShareListener {
                    override fun onShareLinkDialogLaunched() {}
                    override fun onShareLinkDialogDismissed() {}
                    override fun onLinkShareResponse(
                        sharedLink: String?,
                        sharedChannel: String?,
                        error: BranchError?
                    ) {
                    }
                    override fun onChannelSelected(channelName: String) {}
                })
        }

        itemView.setOnClickListener {
            action.onClick(bloggers, adapterPosition)
        }
    }

}

interface OnBlogItemClickListener {
    fun onClick(item: Bloggers, position: Int)
}