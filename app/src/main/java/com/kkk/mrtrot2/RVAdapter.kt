package com.kkk.mrtrot2

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RVAdapter(private val context:Context) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    var datas = mutableListOf<ProfileData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)

        return ViewHolder(view)
    }

    interface ItemClick
    {
        fun onClick(view: View,position: Int)
    }
    var itemClick : ItemClick?=null

    override fun onBindViewHolder(holder: RVAdapter.ViewHolder, position: Int) {

        if(itemClick!=null) {
            holder?.itemView.setOnClickListener { v->
                itemClick!!.onClick(v,position)
            }
        }
        holder.bind(datas[position],position)
    }

    override fun getItemCount(): Int = datas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val singername: TextView = itemView.findViewById(R.id.singername)
        private val singerphoto: ImageView = itemView.findViewById(R.id.singerphoto)
        private val commentCount: TextView = itemView.findViewById(R.id.tv_comment_count)
        private val singerCrown :ImageView = itemView.findViewById(R.id.crown)
        private val singerRanking: TextView = itemView.findViewById(R.id.rankingText)
        private val bestCommentDecorator: LinearLayout = itemView.findViewById(R.id.best_comment_decorator)

        fun bind(item : ProfileData,position: Int){
            singername.text = item.name
            Glide.with(itemView).load(item.img).into(singerphoto)
            commentCount.text = item.commentCount.toString()

            if(position==0) {
                singerCrown.setImageResource(R.drawable.goldcrown)
                singerRanking.text="응원댓글 1위"
                bestCommentDecorator.visibility = View.VISIBLE
            } else if(position==1) {
                singerCrown.setImageResource(R.drawable.silvercrown)
                singerRanking.text="응원댓글 2위"
                bestCommentDecorator.visibility = View.VISIBLE
            } else if(position==2) {
                singerCrown.setImageResource(R.drawable.bronzecrown)
                singerRanking.text="응원댓글 3위"
                bestCommentDecorator.visibility = View.VISIBLE
            } else {
                singerCrown.setImageBitmap(null)
                singerRanking.text=null
                bestCommentDecorator.visibility = View.GONE
            }
        }
    }

}