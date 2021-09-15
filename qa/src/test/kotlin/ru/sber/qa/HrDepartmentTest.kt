package ru.sber.qa

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.unmockkAll
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

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
    @ValueSource(strings = [
        "2021-09-11T00:00:00Z", // SATURDAY
        "2021-09-12T00:00:00Z" // SUNDAY
    ])
    fun receiveRequestThrowWeekendDayException(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.NDFL)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2021-09-14T00:00:00Z", // TUESDAY
        "2021-09-16T00:00:00Z" // THURSDAY
    ])
    fun receiveRequestThrowNotAllowReceiveRequestExceptionWithLabourBook(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.NDFL)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2021-09-13T00:00:00Z", // MONDAY
        "2021-09-15T00:00:00Z", // WEDNESDAY
        "2021-09-17T00:00:00Z" // FRIDAY
    ])
    fun receiveRequestThrowNotAllowReceiveRequestExceptionWithNdfl(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.LABOUR_BOOK)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2021-09-14T00:00:00Z", // TUESDAY
        "2021-09-16T00:00:00Z" // THURSDAY
    ])
    fun receiveRequestSuccessWithLabourBook(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.LABOUR_BOOK)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertDoesNotThrow{ HrDepartment.receiveRequest(request) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2021-09-13T00:00:00Z", // MONDAY
        "2021-09-15T00:00:00Z", // WEDNESDAY
        "2021-09-17T00:00:00Z" // FRIDAY
    ])
    fun receiveRequestSuccessWithNdfl(date: String) {
        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.NDFL)
        every { hr.clock } returns Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        assertDoesNotThrow{ HrDepartment.receiveRequest(request) }
    }

    @Test
    fun receiveRequestSuccess() {

        val request = CertificateRequest(EMPLOYEE_ID, CertificateType.LABOUR_BOOK)
        every { hr.clock } returns Clock.fixed(Instant.parse("2021-09-16T00:00:00Z"), ZoneOffset.UTC)

        assertDoesNotThrow{ hr.receiveRequest(request) }
        assertDoesNotThrow{ HrDepartment.processNextRequest(EMPLOYEE_ID) }
    }

}