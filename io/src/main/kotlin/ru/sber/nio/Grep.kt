package ru.sber.nio

import java.io.File
import java.nio.file.Files
import kotlin.io.path.useLines

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
object Grep {

    /**
     * Метод должен выполнить поиск подстроки subString во всех файлах каталога logs.
     * Каталог logs размещен в данном проекте (io/logs) и внутри содержит другие каталоги.
     * Результатом работы метода должен быть файл в каталоге io(на том же уровне, что и каталог logs), с названием result.txt.
     * Формат содержимого файла result.txt следующий:
     * имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     * Результирующий файл должен содержать данные о найденной подстроке во всех файлах.
     * Пример для подстроки "22/Jan/2001:14:27:46":
     * 22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
     */

    const val LOGS_PATH = "io/logs"
    const val FILE_NAME = "io/result.txt"

    fun find(subString: String) {

        val logsPath = File(LOGS_PATH).toPath()
        val result = File(FILE_NAME)

        result.outputStream().use { stream ->
            Files.find(logsPath, 2, { file, _ -> file.toString().endsWith(".log") })
                .forEach { log ->
                    log.useLines { lines ->
                        lines
                            .filter { line -> line.contains(subString) }
                            .forEach { line ->
                                stream.write(("$log.name : ${log.useLines { it.indexOf(line) + 1 }} : $line\n").toByteArray())
                            }
                    }
                }
        }
    }

}

fun main() {
    Grep.find("109.252.87.217")
}