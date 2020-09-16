package tk.a3labgo.countryinfo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import tk.a3labgo.countryinfo.models.Country
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.activities.SpecificCountryActivity
import kotlin.collections.ArrayList

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var countrylist: List<Country>
    val imageLoader = ImageLoader(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTextView.text = countrylist[i].name
        println(countrylist[i].flag)
        val imageLoader = ImageLoader.Builder(context).componentRegistry {
            add(SvgDecoder(context))
        }.build()
        val request = ImageRequest.Builder(context).data(countrylist[i].flag).target(viewHolder.itemImageView).build()
        imageLoader.enqueue(request)
//        viewHolder.itemImageView.setOnClickListener {
//            val intent = Intent(context, SpecificCountryActivity::class.java)
//            intent.putExtra("callingCode", countrylist[i].callingCodes[0])
//            context.startActivity(intent)
//        }
        viewHolder.itemView.setOnClickListener{
            val intent = Intent(context, SpecificCountryActivity::class.java)
            intent.putExtra("callingCode", countrylist[i].callingCodes[0])
            intent.putExtra("countryName", countrylist[i].nativeName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return countrylist.size
    }

    fun setList(countrylist: List<Country>) {
        this.countrylist = countrylist
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView
        val itemImageView: ImageView

        init {
            itemTextView = itemView.findViewById(R.id.itemTextView)
            itemImageView = itemView.findViewById(R.id.itemImageView)
        }
    }

    init {
        countrylist = ArrayList()
    }
}