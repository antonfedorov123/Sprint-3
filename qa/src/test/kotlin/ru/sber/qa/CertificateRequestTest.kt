package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CertificateRequestTest {

    @AfterEach
    fun afterTest(){
        unmockkAll()
    }

    @ParameterizedTest
    @EnumSource(value = CertificateType::class, names = ["NDFL", "LABOUR_BOOK"])
    fun process(certificateType: CertificateType) {
        val employeeNumber = 321L
        val hrEmployeeNumber = 123L
        val data = ByteArray(10)

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns data

        val request = CertificateRequest(employeeNumber, certificateType)
        val certificate = request.process(hrEmployeeNumber)
        assertEquals(certificate.data, data)
        assertEquals(certificate.certificateRequest.employeeNumber, request.employeeNumber)
        assertEquals(certificate.certificateRequest.certificateType, request.certificateType)
    }

}