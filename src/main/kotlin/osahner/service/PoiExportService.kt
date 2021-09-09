package osahner.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.poi.hssf.usermodel.HSSFDataFormat
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.stereotype.Component
import osahner.toDate
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


@Component
class PoiExportService {
  private val mapper = Jackson2ObjectMapperBuilder().build<ObjectMapper>()
  private val dotRegex = Regex("\\.")

  fun toResponseEntity(wb: Workbook, name: String): ResponseEntity<ByteArrayResource> {
    val headers = HttpHeaders().apply {
      add("Content-Disposition", "filename=\"Export-${name}-${Date().time}.xls\"")
    }
    val bos = ByteArrayOutputStream()
    bos.use {
      wb.apply {
        write(bos)
      }
    }
    val resource = ByteArrayResource(bos.toByteArray())
    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(resource.contentLength())
      .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
      .body(resource)
  }

  fun buildExcelDocument(
    titel: String? = "Export",
    headers: Collection<String>,
    result: Collection<Any>
  ) = buildExcelDocument(
    titel,
    headers.associateWith { header -> header.replaceFirstChar { c -> c.titlecase() } },
    result
  )

  fun buildExcelDocument(
    titel: String? = "Export",
    headers: Map<String, String>,
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

    headers.values.withIndex().forEach { (cellNo, header) ->
      row.createCell(cellNo).apply {
        setCellValue(createHelper.createRichTextString(header))
        setCellStyle(headerCellStyle)
      }
    }

    result.forEach { entity ->
      val list = headers.keys.map { key ->
        readDeepInstanceProperty(entity, key)
      }
      row = sheet.createRow(rowNo++)
      list.withIndex().forEach { (cellNo, cell) ->
        row.createCell(cellNo).apply {
          when (cell) {
            is LocalDate -> {
              setCellValue(cell.toDate())
              setCellStyle(dateStyle)
            }
            is LocalDateTime -> {
              setCellValue(cell.toDate())
              setCellStyle(dateStyle)
            }
            is Number -> setCellValue(cell.toDouble())
            is String -> setCellValue(cell as String?)
            is Boolean -> setCellValue((cell as Boolean?)!!)
            is Collection<*> -> setCellValue(cell.joinToString("; "))
            else -> setCellValue(mapper.writeValueAsString(cell))
          }
        }
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun readDeepInstanceProperty(instance: Any, propertyName: String): Any {
    var prob = propertyName
    var obj = instance
    if (dotRegex.containsMatchIn(propertyName)) {
      do {
        val (o, p) = prob.split(dotRegex, 2)
        prob = p
        obj = readInstanceProperty(obj, o)
      } while (dotRegex.matches(prob))
    }
    return readInstanceProperty(obj, prob)
  }

  @Suppress("UNCHECKED_CAST")
  private fun readInstanceProperty(instance: Any, propertyName: String): Any = try {
    (instance::class.memberProperties
      .first { it.name == propertyName } as KProperty1<Any, *>).get(instance)!!
  } catch (e: Exception) {
    ""
  }
}
