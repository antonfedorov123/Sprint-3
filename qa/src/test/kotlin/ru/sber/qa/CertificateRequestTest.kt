package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CertificateRequestTest {

    @Test
    fun process() {
        val employeeNumber = 321L
        val hrEmployeeNumber = 123L
        val data = ByteArray(10)

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns data

        CertificateType.values().forEach {
            val request = CertificateRequest(employeeNumber, it)
            assertEquals(request, request.process(hrEmployeeNumber).certificateRequest)
        }
    }

    @AfterAll
    fun afterTest(){
        unmockkAll()
    }
}