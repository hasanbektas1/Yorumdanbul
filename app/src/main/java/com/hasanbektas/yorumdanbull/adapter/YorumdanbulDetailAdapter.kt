package com.hasanbektas.yorumdanbull.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hasanbektas.yorumdanbull.R
import com.hasanbektas.yorumdanbull.model.YorumdanbulCommentDataModel
import com.hasanbektas.yorumdanbull.view.DetailFragment
import kotlinx.android.synthetic.main.recycler_row_comment.view.*

class YorumdanbulDetailAdapter (val commentList: ArrayList<YorumdanbulCommentDataModel>, var detailFragment: DetailFragment) :
    RecyclerView.Adapter<YorumdanbulDetailAdapter.CommentsHolder>()  {

    class CommentsHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row_comment, parent, false)
        return CommentsHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.view.recyclerRowCommentText.text = searchWordColor(
            ContextCompat.getColor(holder.itemView.context, R.color.yb_color),
            commentList.get(position).comment, listOf(detailFragment.searchTextactionColor))

        holder.view.recyclerRowCommentDateText.text = commentList[position].commenCreatedDate
    }

    fun searchWordColor(
        searchColor: Int,
        searchWord: String,
        comments: List<String>,
    ): SpannableString {
        val spannableString = SpannableString(searchWord)
        comments.forEach { comment ->

            if (!comment.isBlank()) {
                var startIndex = searchWord.indexOf(comment, 0, true)

                while (startIndex >= 0) {
                    spannableString.setSpan(
                        ForegroundColorSpan(searchColor), startIndex, startIndex + comment.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    startIndex = searchWord.indexOf(comment, startIndex + comment.length)
                }
            }
        }
        return spannableString
    }
}