package tk.a3labgo.countryinfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.callback.HomeInterface
import tk.a3labgo.countryinfo.models.Country


class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var countrylist: List<Country>
    private var homeInterface: HomeInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.itemTextView.text = countrylist[i].name
        val imageLoader = ImageLoader.Builder(context).componentRegistry {
            add(SvgDecoder(context))
        }.build()
        val request = ImageRequest.Builder(context).data(countrylist[i].flag).target(viewHolder.itemImageView).build()
        imageLoader.enqueue(request)
        viewHolder.itemView.setOnClickListener{
            if (this.homeInterface != null) {
                this.homeInterface!!.clickedCountry(countrylist[i].callingCodes[0],countrylist[i].nativeName)
            } else{
                Toast.makeText(context, "Error, Contact Developer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return countrylist.size
    }

    fun setList(countryList: List<Country>) {
        this.countrylist = countryList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.itemTextView)
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
    }

    init {
        countrylist = ArrayList()
        homeInterface = context as HomeInterface
    }
}