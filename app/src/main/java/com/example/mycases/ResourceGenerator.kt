package com.example.mycases

import android.content.Context

class ResourceGenerator() {

    companion object {
        fun getResourceId(context: Context, name: String, skin: String): Int {
            val imgName = sanitizeString(name + "__" + skin)
            return context.resources.getIdentifier(imgName, "drawable", context.packageName)
        }
        private fun sanitizeString(input: String): String {
            var imgName = input
            // Проверяем, содержит ли строка символ '-'
            if (imgName.contains('-')) {
                // Если содержит, удаляем все символы '-'
                imgName = imgName.replace("-", "")
            }
            // Проверяем, содержит ли строка символ ' '
            if (imgName.contains(' ')) {
                // Если содержит, заменяем все символы на '_'
                imgName = imgName.replace(" ", "_")
            }
            // Если не содержит, возвращаем исходную строку
            return imgName.lowercase()
        }
    }

}