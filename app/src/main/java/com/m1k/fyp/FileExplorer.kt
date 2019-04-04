package com.m1k.fyp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_file_explorer.*
import java.io.File

class FileExplorer : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterF.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_explorer)

        layoutManager = GridLayoutManager(this, 2)
        ImageList.layoutManager = layoutManager

        adapter = RecyclerAdapterF(this)
        ImageList.adapter = adapter

        findViewById<RecyclerView>(R.id.ImageList).addItemDecoration(MarginItemDecoration(70))


    }


    //from https://medium.com/@elye.project/right-way-of-setting-margin-on-recycler-views-cell-319da259b641
    inner class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
                    top = spaceHeight
                }
                left = spaceHeight
                right = spaceHeight
                bottom = spaceHeight
            }
        }
    }
    //https://medium.com/@elye.project/right-way-of-setting-margin-on-recycler-views-cell-319da259b641

    inner class RecyclerAdapterF(private val fileExplorer: FileExplorer) :
        RecyclerView.Adapter<RecyclerAdapterF.ViewHolder>() {

        private var allFiles: MutableList<File> = mutableListOf()
        private var allImages: MutableList<Bitmap> = mutableListOf()
        private var path = this@FileExplorer.getExternalFilesDir("")?.toString()

        init {
            path += if (GlobalApp.isLogged()) {
                "/${GlobalApp.getLogged()}"
            } else {
                "/Public"
            }

            val fp = File(path).listFiles()
            if (fp.isNotEmpty()) {
                allFiles = fp.toMutableList()

                for (f in allFiles) {
                    allImages.add(BitmapFactory.decodeFile(f.toString()))
                }
            }
        }

        override fun onBindViewHolder(p0: RecyclerAdapterF.ViewHolder, p1: Int) {
            if (itemCount > 0) {
                p0.pecsImage.setImageBitmap(allImages[p1])
            }
        }

        override fun getItemCount(): Int {
            return allFiles.size
        }

        //we can reuse the same code as in PECSActivity, as theyre both displaying images...
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerAdapterF.ViewHolder {
            val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.pecs_holder, viewGroup, false)
            return ViewHolder(v)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var pecsImage: ImageView = itemView.findViewById(R.id.pecs_image)

            init {
                itemView.setOnClickListener {
                    val im = ImageView(itemView.context)
                    im.setImageBitmap(allImages[adapterPosition])
                    AlertDialog.Builder(itemView.context).setView(im).create().show()
                }
            }
        }

    }
}

