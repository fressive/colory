package org.int100.colory.core.util

object TextFormatter {
    private val findParamsRegex = Regex("""\$\{(.*?)}+""")

    fun format(source: String, params: Map<String, Any>): String {
        var result = source
        findParamsRegex.findAll(source).forEach {
            result = result.replace(it.value, params[it.groupValues[1]]!!.toString())
        }

        return result
    }
}