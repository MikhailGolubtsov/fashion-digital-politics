package de.fashiondigital.politics.domain.model

import java.time.LocalDate

final case class Speech(speaker: String,
                        subject: String,
                        date: LocalDate,
                        numberOfWords: Int)