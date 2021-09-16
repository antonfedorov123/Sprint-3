package ru.sber.qa

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.*
import ru.sber.qa.CertificateRequest
import ru.sber.qa.Scanner


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HrDepartmentTest {

    private val EMPLOYEE_ID = 123L

    private lateinit var hr: HrDepartment

    @BeforeEach
    fun init() {
        hr = mockkClass(HrDepartment::class)
    }

    @AfterEach
    fun afterTest(){
        unmockkAll()
    }

    @ParameterizedTest
    @ValueSource(strings = [ SATURDAY, SUNDAY ])
    fun receiveRequestThrowWeekendDayException(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.NDFL)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [ TUESDAY, THURSDAY ])
    fun receiveRequestThrowNotAllowReceiveRequestExceptionWithLabourBook(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.NDFL)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [ MONDAY, WEDNESDAY, FRIDAY ])
    fun receiveRequestThrowNotAllowReceiveRequestExceptionWithNdfl(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.LABOUR_BOOK)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [ TUESDAY, THURSDAY ])
    fun receiveRequestSuccessWithLabourBook(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.LABOUR_BOOK)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertDoesNotThrow{ HrDepartment.receiveRequest(request) }

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(100)
        assertDoesNotThrow{ HrDepartment.processNextRequest(EMPLOYEE_ID) }
    }

    @ParameterizedTest
    @ValueSource(strings = [ MONDAY, WEDNESDAY, FRIDAY ])
    fun receiveRequestSuccessWithNdfl(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.NDFL)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertDoesNotThrow{ HrDepartment.receiveRequest(request) }

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(100)
        assertDoesNotThrow{ HrDepartment.processNextRequest(EMPLOYEE_ID) }
    }

    companion object {

        const val MONDAY    = "2021-09-13T00:00:00Z"
        const val TUESDAY   = "2021-09-14T00:00:00Z"
        const val WEDNESDAY = "2021-09-15T00:00:00Z"
        const val THURSDAY  = "2021-09-16T00:00:00Z"
        const val FRIDAY    = "2021-09-17T00:00:00Z"
        const val SATURDAY  = "2021-09-18T00:00:00Z"
        const val SUNDAY    = "2021-09-19T00:00:00Z"

    }

}