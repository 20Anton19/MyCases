package com.example.mycases

class SanitizeString() {
    companion object {
        fun sanitizeString(input: String): String {
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