package ru.lingstra.avitocopy

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val s = setOf(TT(1, "111"), TT(2, "2"))
        val f = setOf(TT(0, "0"), TT(1, "1"))
        val t: MutableSet<TT> = s.toMutableSet()
        t.addAll(f)
        println(t)
    }

    data class TT(val int: Int, val str: String) {
        override fun equals(other: Any?): Boolean {
            return int == (other as? TT)?.int
        }

        override fun hashCode(): Int {
            return int
        }
    }

    @Test
    fun testPrint(){
        val list = listOf(1, 2, 3)
        println(list)
    }
}
