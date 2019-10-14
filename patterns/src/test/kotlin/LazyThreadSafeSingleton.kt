import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Dummy {
    init {
        println("Init dummy object!")
    }
    fun print() = println("Print object $this")
}

object DummySingleton {
    val instance: Dummy by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Dummy()
    }
}

class LazyThreadSafeSingletonTest {

    @Test
    fun Singleton() {
        val dummyFirst = DummySingleton.instance
        val dummySecond = DummySingleton.instance

        dummyFirst.print()
        dummySecond.print()
        assertThat(dummyFirst).isEqualTo(dummySecond)
        assertThat(dummyFirst).isSameAs(dummySecond)
        assertTrue(dummyFirst === dummySecond)
    }
}