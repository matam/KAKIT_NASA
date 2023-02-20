package edu.zut.erasmus_plus.retrofit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

class ApodsRecyclerViewAdapter (private val list: List<AstronomyPictureDayEntity>):RecyclerView.Adapter<ApodsRecyclerViewAdapter.ViewHolder>() {
    lateinit var context : Context

    //only needed for removal
    private var listData: MutableList<AstronomyPictureDayEntity> = list as MutableList<AstronomyPictureDayEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.material_card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val apodItem = listData[position]
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        // sets the image to the imageview from our itemHolder class
        Glide.with(context)
            .load(apodItem.url)
            .placeholder(circularProgressDrawable)
            .skipMemoryCache(false)//for caching the image url in case phone is offline
            .into(holder.imageViewAPD)

        // sets the text to the textview from our itemHolder class
        holder.titleAPD.text = apodItem.title
        holder.copyrigthAPD.text = apodItem.copyright
        holder.dateAPD.text = apodItem.date
        holder.showImageAPD.setOnClickListener {
            val intent = Intent(context, ShowImage::class.java)
            intent.putExtra("url", apodItem.url)
            context.startActivity(intent)
        }

        holder.deleteImageAPD.setOnClickListener{
            listData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val imageViewAPD: ImageView
        val titleAPD : TextView
        val copyrigthAPD : TextView
        val dateAPD: TextView
        val showImageAPD : Button
        val deleteImageAPD : Button
        init {
            imageViewAPD =  view.findViewById(R.id.imageviewAPD)
            titleAPD = view.findViewById(R.id.titleAPD)
            copyrigthAPD = view.findViewById(R.id.copyrigthAPD)
            dateAPD = view.findViewById(R.id.dateAPD)
            showImageAPD = view.findViewById(R.id.showImageAPD)
            deleteImageAPD = view.findViewById(R.id.deleteImageAPD)
        }
    }
}
