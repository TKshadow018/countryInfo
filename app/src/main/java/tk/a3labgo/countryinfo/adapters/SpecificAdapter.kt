package tk.a3labgo.countryinfo.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.callback.PropertyInterface
import tk.a3labgo.countryinfo.models.KeyValue


class SpecificAdapter(private val context: Context) : RecyclerView.Adapter<SpecificAdapter.ViewHolder>() {
    private var visibleItems: List<KeyValue>
    private var propertyInterface: PropertyInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.specific_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.text = visibleItems[i+1].key
        viewHolder.desc.text = visibleItems[i+1].value
        viewHolder.itemView.setOnClickListener{
            when {
                visibleItems[i+1].key == context.getText(R.string.map) -> {
                    val mapArr = visibleItems[i+1].value.split(",").toTypedArray()
                    val latitude = mapArr[0]
                    val longitude = mapArr[1]
                    val label = ""
                    val uriBegin = "geo:$latitude,$longitude"
                    val query = "$latitude,$longitude($label)"
                    val encodedQuery: String = Uri.encode(query)
                    val uriString = "$uriBegin?q=$encodedQuery&z=5"
                    val uri: Uri = Uri.parse(uriString)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
                visibleItems[i+1].key == context.getText(R.string.capital) -> {
                    val latitude = 0
                    val longitude = 0
                    val label = visibleItems[i+1].value
                    val uriBegin = "geo:$latitude,$longitude"
                    val query = "$latitude,$longitude($label)"
                    val encodedQuery: String = Uri.encode(query)
                    val uriString = "$uriBegin?q=$encodedQuery&z=13"
                    val uri: Uri = Uri.parse(uriString)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
                visibleItems[i+1].key == context.getText(R.string.memberOf) || visibleItems[i+1].key == context.getText(R.string.currencyUsed) || visibleItems[i+1].key == context.getText(R.string.spokenLang) -> {
                    propertyInterface?.clickedProperty(visibleItems[i+1].value)
                }
                else -> {
                    propertyInterface?.clickedProperty(visibleItems[0].value)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return visibleItems.size - 1
    }

    fun setSpecificList(visibleItems: ArrayList<KeyValue>) {
        this.visibleItems = visibleItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val desc: TextView = itemView.findViewById(R.id.itemDescription)
    }

    init {
        this.visibleItems = ArrayList()
        propertyInterface = context as PropertyInterface
    }
}