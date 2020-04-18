package osahner.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class PoiExportService(
  @Qualifier("objectMapper") private val mapper: ObjectMapper
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
      val map = mapper.convertValue<Map<String, Any>>(entity)
      val list = header.keys.map { map[it] }
      row = sheet.createRow(rowNo++)
      list.withIndex().forEach { (cellNo, entity) ->
        row.createCell(cellNo).apply {
          when (entity) {
            is Number -> setCellValue(entity.toDouble())
            is String -> setCellValue(entity as String?)
            is Boolean -> setCellValue((entity as Boolean?)!!)
            is Collection<*> -> setCellValue(entity.joinToString("; "))
            is Any -> setCellValue(mapper.writeValueAsString(entity))
            else -> setCellValue("") // null -> empty text field
          }
        }
      }
    }
  }
}
