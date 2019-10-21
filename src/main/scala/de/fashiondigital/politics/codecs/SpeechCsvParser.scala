package de.fashiondigital.politics.codecs

import java.time.LocalDate

import de.fashiondigital.politics.domain.model.Speech

class SpeechCsvParser {

  def parseRow(row: String): Speech = {
    try {
      val cols = row.split(",").map(_.trim)
      Speech(speaker = cols(0),
        subject = cols(1),
        date = LocalDate.parse(cols(2)),
        numberOfWords = Integer.parseInt(cols(3)))
    } catch { case e: Exception =>
      throw new RuntimeException(s"Error parsing a speech from the row '${row}'", e)
    }
  }
}
