import android.content.Context
import android.provider.MediaStore

fun getAllFoldersWithVideos(context: Context): ArrayList<FolderModel> {
    val folderList = ArrayList<FolderModel>()
    val folderMap = HashMap<String, ArrayList<VideoModel>>()

    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME // Folder Name
    )

    val cursor = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        "${MediaStore.Video.Media.DATE_ADDED} DESC"
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val titleColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
        val pathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val folderColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val title = it.getString(titleColumn) ?: "Unknown"
            val path = it.getString(pathColumn)
            val duration = it.getLong(durationColumn)
            val size = it.getLong(sizeColumn)
            val folderName = it.getString(folderColumn) ?: "Internal Storage"

            val video = VideoModel(id, title, path, duration, size, folderName)

            // Map me check karke folder ke according list me add karein
            if (!folderMap.containsKey(folderName)) {
                folderMap[folderName] = ArrayList()
            }
            folderMap[folderName]?.add(video)
        }
    }

    // HashMap ko List me convert karein
    for ((name, list) in folderMap) {
        folderList.add(FolderModel(name, list))
    }

    return folderList
}
