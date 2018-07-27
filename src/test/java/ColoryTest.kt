import org.int100.colory.core.ColoryTemplate

object ColoryTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val template = ColoryTemplate("example/template.xml").parseTemplate()
        template.getRender(mapOf("title" to "Reol - No Title")).saveImage("E:\\test.png")

    }
}