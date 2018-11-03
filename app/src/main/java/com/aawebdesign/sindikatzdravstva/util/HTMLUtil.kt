package com.aawebdesign.sindikatzdravstva.util

import java.util.regex.Pattern

class HTMLUtil {

    private val REGEX_BRACKETS = "\\[([^]]+)\\]"
    private val REGEX_LINKS = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"
    private val REGEX_UNICODE = "&#[0-9][0-9][0-9][0-9];"
    private val REGEX_TAGS = "<TAG>(.*?)</TAG>"

    companion object {
        fun clean(html:String):String{
            var obj = HTMLUtil()
            return obj.clean(html)
        }

        fun fixUnicodeDash(html:String):String{
            var result = html
            if(html.contains("&#8211;")){
                result = html.replace("&#8211;", "-")
            }
            return result
        }

    }

    private fun clean(html: String): String {
        var html = fixUnicodeDash(html)
        html = removeAllUnicode(html)
        html = removeAllBracketsKeepingLinks(html)
        html = completeLinksToFiles(html)
        val htmlList = removeBracketsAndSeparateTextFromLinks(html)
        return toString(wrapLinks(htmlList))
    }



    private fun wrapLinks(htmlList: List<String>): List<String> {
        val newHtmlList = arrayListOf<String>()
        for (s in htmlList) {
            var string : String = s
            if (string.contains("vimeo") || string.contains("youtube") || string.contains("youtu.be")) {
                string = "<a href=\"$string\">ПОГЛЕДАЈТЕ ВИДЕО</a>"
            }
            string = string.replace("<LINK>", "")
            string = string.replace("</LINK>", "")
            newHtmlList.add(string)
        }
        return newHtmlList
    }

    private fun removeAllUnicode(rendered: String): String {
        return rendered.replace(REGEX_UNICODE.toRegex(), "")
    }

    private fun removeAllBracketsKeepingLinks(html: String): String {
        var html = html
        val brackets = getAllMatchingByRegex(html, REGEX_BRACKETS)
        for (bracket in brackets) {
            val linksInBracket = getAllMatchingByRegex(bracket, REGEX_LINKS)
            if (linksInBracket.isEmpty()) {
                html = html.replace(bracket, "")
            } else {
                html = html.replace(bracket, linkListToString(linksInBracket))
            }
        }
        return html
    }

    private fun getAllMatchingByRegex(source: String, regex: String): List<String> {
        val allMatching = arrayListOf<String>()
        val m = Pattern.compile(regex).matcher(source)
        while (m.find()) {
            allMatching.add(m.group())
        }
        return allMatching
    }

    private fun linkListToString(linksInBracket: List<String>): String {
        var string = ""
        for (s in linksInBracket) {
            string += "<LINK>$s</LINK>"
        }
        return string
    }

    private fun completeLinksToFiles(html: String): String {
        var html = html
        if (html.contains("<a href=\\\"/wp-content/")) {
            html = html.replace("<a href=\\\"/wp-content/", "<a href=\\\"http://zdravko.org.rs/wp-content/")
        }
        return html
    }

    private fun removeBracketsAndSeparateTextFromLinks(html: String): List<String> {
        var html = html
        val result = arrayListOf<String>()

        val regex = REGEX_TAGS.replace("TAG".toRegex(), "LINK")
        val linkList = getAllMatchingByRegex(html, regex)

        if (linkList.isEmpty()) {
            result.add(html)
            return result
        }

        for (link in linkList) {
            html = html.replace(link.toRegex(), "*")
        }

        val splittedHtml = toList(html.split("\\*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

        for (i in splittedHtml.indices) {
            result.add(splittedHtml[i])
            if (i != linkList.size)
                result.add(linkList[i])
        }
        return removeBlanks(result)
    }

    private fun toList(splitted: Array<String>): List<String> {
        val result = arrayListOf<String>()
        for (s in splitted) {
            result.add(s)
        }
        return result
    }

    private fun removeBlanks(result: List<String>): List<String> {
        val returnList = arrayListOf<String>()
        
        for (s in result) {
            var string : String = s
            if (string.isEmpty()) {
                continue
            }
            if (string.contains("\n")) {
                string = string.replace("\n", "")
            }
            if (string.contains("\\n")) {
                string = string.replace("\\n", "")
            }
            if (string == "<p>" || string == "</p>") {
                continue
            }
            if (string.startsWith("</p>")) {
                string = string.replace("</p>", "")
            }
            string = string.replace("<p></p>".toRegex(), "")
            returnList.add(string)
        }
        return returnList
    }

    private fun toString(clean: List<String>): String {
        var result = ""
        for (s in clean) {
            result += s + "\n"
        }
        return result
    }


}