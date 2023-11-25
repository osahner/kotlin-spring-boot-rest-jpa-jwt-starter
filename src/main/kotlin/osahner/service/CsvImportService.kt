package osahner.service

import com.opencsv.bean.CsvToBeanBuilder
import org.apache.commons.io.input.BOMInputStream.builder
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class CsvImportService {
  final inline fun <reified T : CsvImportDto<S>, S> import(file: MultipartFile): Collection<S> =
    builder().setInputStream(file.inputStream).get().bufferedReader().use { stream ->
      CsvToBeanBuilder<T>(stream)
        .withType(T::class.java)
        .withIgnoreLeadingWhiteSpace(true)
        .withSeparator(';')
        .build()
        .parse()
        .map { it.toEntity() }
    }
}

fun interface CsvImportDto<S> {
  fun toEntity(): S
}
