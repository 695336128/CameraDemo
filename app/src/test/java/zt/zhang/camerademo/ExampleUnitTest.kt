package zt.zhang.camerademo

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val list = listOf(1,2,3,4,5,6,7,8,9,8,7,6,5,4)

        println(list.contains(14))                         // false
        println(list.elementAt(4))                  // ArrayIndexOutOfBoundsException
        println(list.elementAtOrElse(14) {it + 3})  // 17
        println(list.elementAtOrNull(14))           // null
        println(list.first{ it % 3 == 0 })                  // 3
        println(list.filterNotNull())
        println(list.indexOf(5))                            // 4
        println(list.indexOfFirst { it == 14 })             // -1
        println(list.indexOfLast { it == 4 })               // 13
        println(list.last { it % 3 == 0 })                  // 6
        println(list.lastIndexOf(14))               // -1
        println(list.single { it == 2 })                    // 2
        println(list.singleOrNull { it == 14 })             // null
    }
}
