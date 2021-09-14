package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertArrayEquals

import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ScannerTest {

    @BeforeAll
    fun init() {
        mockkObject(Random)
    }

    @Test
    fun throwTimeoutExceptionTest() {
        every { Random.nextLong(5000L, 15000L) } returns 20_000L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    // Тест на эквивалентность данных
    @Test
    fun getScanDataTest() {

        val data = Random.nextBytes(100)
        every { Random.nextLong(5000L, 15000L) } returns 5_000L
        every { Random.nextBytes(100) } returns data
        val result = Scanner.getScanData()

        assertArrayEquals(result, data)
    }

    @AfterAll
    fun afterTest(){
        unmockkAll()
    }
}