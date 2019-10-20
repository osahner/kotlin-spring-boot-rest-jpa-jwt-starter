package osahner.service

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.stereotype.Component
import java.util.*

@Component
class PoiExportService {

  fun buildExcelDocument(titel: String? = "Export", header: Collection<String>, result: Collection<Any>): Workbook {
    val mapper = jacksonObjectMapper()

    val workbook = HSSFWorkbook()
    val sheet = workbook.createSheet(titel)
    sheet.defaultColumnWidth = 40

    val headerFont = workbook.createFont()
    headerFont.bold = true
    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(headerFont)
    headerCellStyle.borderBottom = BorderStyle.MEDIUM
    val createHelper = workbook.creationHelper
    var rowNo = 0
    var row = sheet.createRow(rowNo++)

    header.withIndex().forEach { (cellNo, it) ->
      val cell = row.createCell(cellNo)
      cell.setCellValue(createHelper.createRichTextString(it.capitalize()))
      cell.setCellStyle(headerCellStyle)
    }

    result.forEach { entity ->
      val map = mapper.convertValue<Map<String, Any>>(entity)
      val list = header.map { map[it] }
      row = sheet.createRow(rowNo++)
      list.withIndex().forEach { (cellNo, entity) ->
        createCellWith(row, cellNo, entity)
      }
    }

    return workbook
  }


  private fun createCellWith(row: Row, cellNo: Int, value: Any?) {
    val cell = row.createCell(cellNo)
    when (value) {
      is Number -> cell.setCellValue(value.toDouble())
      is String -> cell.setCellValue(value as String?)
      is Date -> cell.setCellValue(value as Date?)
      is Boolean -> cell.setCellValue((value as Boolean?)!!)
      is Collection<*> -> cell.setCellValue(value.joinToString(", "))
      else -> cell.setCellValue("-")
    }
  }
}
