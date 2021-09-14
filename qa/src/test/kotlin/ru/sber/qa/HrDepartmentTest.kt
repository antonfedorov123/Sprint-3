package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.*
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HrDepartmentTest {

    // fields
    private lateinit var incomeBox: LinkedList<CertificateRequest>
    private lateinit var outcomeOutcome: LinkedList<Certificate>

    @BeforeEach
    fun init() {
        incomeBox = LinkedList()
        outcomeOutcome = LinkedList()
    }

    @Test
    fun receiveRequestThrowWeekendDayException() {
        val hr = mockkClass(HrDepartment::class)
        val request = mockk<CertificateRequest>()
        every { hr.clock } returns Clock.fixed(Instant.parse("2021-09-12T00:00:00Z"), ZoneOffset.UTC)
        every { request.certificateType } returns CertificateType.NDFL
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun receiveRequest() {

    }

    @Test
    fun processNextRequest() {

    }
}