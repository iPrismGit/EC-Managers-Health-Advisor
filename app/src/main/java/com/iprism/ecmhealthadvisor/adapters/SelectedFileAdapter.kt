import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.SelectedFileItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.FileItem
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.FileType

class SelectedFileAdapter(
    private val items: MutableList<FileItem>,
    private val onListChanged: (() -> Unit)? = null
) : RecyclerView.Adapter<SelectedFileAdapter.FileViewHolder>() {

    inner class FileViewHolder(val binding: SelectedFileItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SelectedFileItemBinding.inflate(inflater, parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = items[position]

        if (item.type == FileType.IMAGE) {
            holder.binding.imageView.setImageURI(item.uri)
            holder.binding.fileName.text = ""
        } else {
            holder.binding.imageView.setImageResource(R.drawable.ic_img)
            holder.binding.fileName.text = getFileNameFromUri(holder.itemView.context, item.uri)
        }

        holder.binding.deleteImg.setOnClickListener {
            items.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, items.size)
            onListChanged?.invoke()
        }
    }

    override fun getItemCount(): Int = items.size

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: -1
        cursor?.moveToFirst()
        val name = if (nameIndex >= 0) cursor?.getString(nameIndex) else "Unknown"
        cursor?.close()
        return name ?: "Unknown"
    }

}
