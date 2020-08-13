package osahner.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.poi.hssf.usermodel.HSSFDataFormat
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Component
class PoiExportService(
  private val mapper: ObjectMapper
) {
  fun buildExcelDocument(
    titel: String? = "Export",
    header: Collection<String>,
    result: Collection<Any>
  ) = buildExcelDocument(titel, header.map { it to it.capitalize() }.toMap(), result)

  fun buildExcelDocument(
    titel: String? = "Export",
    header: Map<String, String>,
    result: Collection<Any>
  ): Workbook = HSSFWorkbook().apply {
    val sheet = createSheet(titel).apply {
      defaultColumnWidth = 40
    }
    val headerFont = createFont().apply {
      bold = true
    }
    val headerCellStyle = createCellStyle().apply {
      setFont(headerFont)
      borderBottom = BorderStyle.MEDIUM
    }
    val dateStyle = createCellStyle().apply {
      dataFormat = HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm")
    }

    val createHelper = creationHelper
    var rowNo = 0
    var row = sheet.createRow(rowNo++)

    header.values.withIndex().forEach { (cellNo, it) ->
      row.createCell(cellNo).apply {
        setCellValue(createHelper.createRichTextString(it))
        setCellStyle(headerCellStyle)
      }
    }

    result.forEach { entity ->
      val list = header.keys.map {
        readInstanceProperty(entity, it)
      }
      row = sheet.createRow(rowNo++)
      list.withIndex().forEach { (cellNo, cell) ->
        row.createCell(cellNo).apply {
          when (cell) {
            is LocalDate -> {
              setCellValue(Date.from(cell.atStartOfDay(ZoneId.systemDefault()).toInstant()) as Date)
              setCellStyle(dateStyle)
            }
            is Number -> setCellValue(cell.toDouble())
            is String -> setCellValue(cell as String?)
            is Boolean -> setCellValue((cell as Boolean?)!!)
            is Collection<*> -> setCellValue(cell.joinToString("; "))
            is Any -> setCellValue(mapper.writeValueAsString(cell))
            else -> setCellValue("") // null -> empty text field
          }
        }
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun readInstanceProperty(instance: Any, propertyName: String): Any? {
    return try {
      (instance::class.memberProperties.first { it.name == propertyName } as KProperty1<Any, *>).get(instance)
    } catch (e: Exception) {
      ""
    }
  }
}
