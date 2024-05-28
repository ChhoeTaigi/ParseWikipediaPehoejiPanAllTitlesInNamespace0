import com.github.doyaaaaaken.kotlincsv.dsl.context.WriteQuoteMode
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File


const val POJ_UNICODE_REGEX =
    "^(ph|p|m|b|th|t|l|kh|k|ng|n|g|h|chh|ch|s|j)?((?:á|à|â|ā|a̍|̍ă|a|í|ì|î|ī|i̍|ĭ|i|ṳ́|ṳ̀|ṳ̂|ṳ̄|ṳ̍|ṳ̆|ṳ|ú|ù|û|ū|u̍|ŭ|u|ó͘|ò͘|ô͘|ō͘|o̍͘|ǒ͘|o͘|ó̤|ò̤|ô̤|ō̤|o̤̍|ŏ̤|o̤|é|è|ê|ē|e̍|ĕ|e|ó|ò|ô|ō|o̍|ŏ|o|r)*)(ⁿ|ḿ|m̀|m̂|m̄|m̍|m̆|m|ńg|ǹg|n̂g|n̄g|n̍g|n̆g|ng|ń|ǹ|n̂|n̄|n̍|n̆|n)?(p|t|k|h)?$"

fun main(args: Array<String>) {
    // https://dumps.wikimedia.org/zh_min_nanwiki/
    val f = File("zh_min_nanwiki-20240520-all-titles-in-ns0")

    val rowArrayList = ArrayList<ArrayList<String>>()
    f.forEachLine {
        if (it.containsPehoeji()) {
            println("POJ: $it")
            val poj = it.replace("_(", "(").replace("_", " ")
            val rowList = arrayListOf<String>(it, poj, "https://zh-min-nan.wikipedia.org/wiki/$it")
            rowArrayList.add(rowList)
        }
    }

    println("Total amount: ${rowArrayList.size}")

    // https://github.com/doyaaaaaken/kotlin-csv
    val writer = csvWriter {
        quote {
            mode = WriteQuoteMode.ALL
            char = '\"'
        }
    }
    writer.writeAll(rowArrayList, "output.csv", append = false)
}

fun String.containsPehoeji(): Boolean {
    val splitRegex = "([\\-_()])".toRegex()
    val splitStringList = this.split(splitRegex)
    var containsPoj = false
    for (splitString in splitStringList) {
//        print("$splitString/")

        if (splitString.isEmpty()) {
            continue
        }

        val pojRegex = Regex(POJ_UNICODE_REGEX, RegexOption.IGNORE_CASE)
        val matches = pojRegex.matches(splitString)
        if (matches) {
//            println("match splitString: $splitString")
            containsPoj = true
            break
        }
    }
//    println()

//    println("THIS: $this")

    return containsPoj
}