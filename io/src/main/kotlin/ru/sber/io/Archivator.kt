package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
object Archivator {

    const val FILE_NAME: String = "logfile.log"
    const val ZIP_NAME: String = "logfile.zip"
    const val UNZIPPED_FILE_NAME: String = "unzippedLogfile.log"

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        ZipOutputStream(FileOutputStream(ZIP_NAME)).use { zipOutputStream ->
            FileInputStream(FILE_NAME).use { fileInputStream ->

                val entry = ZipEntry(UNZIPPED_FILE_NAME)
                zipOutputStream.putNextEntry(entry)

                val buffer = ByteArray(fileInputStream.available())
                fileInputStream.read(buffer)
                zipOutputStream.write(buffer)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {

        if(File(ZIP_NAME).exists()) {
            println("Nooo")
        }

        ZipInputStream(FileInputStream(ZIP_NAME))
            .use { zipInputStream ->
                FileOutputStream(UNZIPPED_FILE_NAME).use { fileOutputStream ->

                    while (zipInputStream.nextEntry != null) {
                        var char = zipInputStream.read()
                        while (char != -1) {
                            fileOutputStream.write(char)
                            char = zipInputStream.read()
                        }
                    }

                }
            }
    }

}

fun main() {
    Archivator.zipLogfile()
    Archivator.unzipLogfile()
}