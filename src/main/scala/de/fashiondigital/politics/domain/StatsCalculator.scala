package de.fashiondigital.politics.domain

import de.fashiondigital.politics.domain.model.{Speech, SpeechStats}

class StatsCalculator(yearToCheck: Int,
                      subjectToCheck: String) {

  def calculateStats(speeches: Seq[Speech]): SpeechStats = {
    SpeechStats(
      getSpeakerWithMostSpeechesInTheYear(speeches, yearToCheck),
      getMostFrequentSpeakerOnSubject(speeches, subjectToCheck),
      getLeastWordySpeaker(speeches)
    )
  }

  def getLeastWordySpeaker(speeches: Seq[Speech]): Option[String] = {
    val speechesBySpeaker: Map[String, Seq[Speech]] = speeches.groupBy(_.speaker)
    if (!speeches.isEmpty) {
      val (speaker, _) = speechesBySpeaker.minBy { case (_, speeches) => speeches.map(_.numberOfWords).sum }
      Some(speaker)
    } else {
      None
    }
  }

  def getMostFrequentSpeakerOnSubject(speeches: Seq[Speech], subject: String): Option[String] = {
    val speechesOnSubject = speeches.filter(_.subject == subject)
    getSpeakerWithMostSpeeches(speechesOnSubject)
  }

  def getSpeakerWithMostSpeechesInTheYear(speeches: Seq[Speech], year: Int): Option[String] = {
    val speachesInTheYear = speeches.filter(_.date.getYear == year)
    getSpeakerWithMostSpeeches(speachesInTheYear)
  }

  private def getSpeakerWithMostSpeeches(speeches: Seq[Speech]): Option[String] = {
    val speechesBySpeaker: Map[String, Seq[Speech]] = speeches.groupBy(_.speaker)
    if (!speeches.isEmpty) {
      val (speaker, _ ) = speechesBySpeaker.maxBy { case (_, speeches) => speeches.size }
      Some(speaker)
    } else {
      None
    }
  }
}
