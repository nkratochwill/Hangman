package client

fun main() {
    var a = "wasefa"
    var b = a.conceal()
    println(a.conceal())
    var c = a.unconceal('a')
    println(c)
}

private fun String.conceal(): String {
    val a = this.toCharArray()
    for (i in this.indices) {
        a[i] = '.'
    }
    return String(a)
}

private fun String.unconceal(letter: Char): String {
    val a = this.toCharArray()
    for (i in this.indices) {
        if (this[i] != letter) {
            a[i] = '.'
        }
    }
    return String(a)
}