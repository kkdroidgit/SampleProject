package com.example.android.sampleproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.sampleproject.model.ListViewModel
import com.example.android.sampleproject.R
import com.example.android.sampleproject.adapter.OnBlogItemClickListener
import com.example.android.sampleproject.adapter.RecyclerAdapter
import com.example.android.sampleproject.model.Bloggers
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import kotlinx.android.synthetic.main.activity_bloggers.*
import java.util.*

class BloggersActivity : AppCompatActivity(), OnBlogItemClickListener{

    lateinit var viewModel: ListViewModel
    private val usersAdapter = RecyclerAdapter(arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bloggers)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.listBloggers.observe(this, Observer { list ->
            list?.let {
                recyclerView.visibility = View.VISIBLE
                usersAdapter.updateUsers(it)
            }
        })
        viewModel.apiError.observe(this, Observer { isError ->
            isError?.let { list_error.visibility = if (it) View.VISIBLE else View.GONE }
        })
        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(item: Bloggers, position: Int) {

        val branchUniversalObject = BranchUniversalObject()
            .setCanonicalIdentifier(UUID.randomUUID().toString())
            .setTitle(item.title)
            .setContentDescription("Check out this blog on openAcademy")
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setContentMetadata(
                ContentMetadata().addCustomMetadata("blogger_name", item.title)
            )
        branchUniversalObject.listOnGoogleSearch(this)
        BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM).addContentItems(branchUniversalObject).logEvent(this)

        val intent = Intent(this,BloggerDetailActivity::class.java)
        intent.putExtra("EXTRA", item)
        Branch.getInstance().userCompletedAction("Launched Detail Activity " )
        startActivity(intent)
    }

}