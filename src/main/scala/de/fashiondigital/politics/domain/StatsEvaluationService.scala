package de.fashiondigital.politics.domain

import akka.http.scaladsl.model.Uri
import de.fashiondigital.politics.domain.model.SpeechStats
import de.fashiondigital.politics.http.DatasetDownloader

import scala.concurrent.{ExecutionContext, Future}

class StatsEvaluationService(datasetDownloader: DatasetDownloader,
                             statsCalculator: StatsCalculator)(implicit ec: ExecutionContext) {

  def evaluateStats(urls: List[Uri]): Future[SpeechStats] = {
    datasetDownloader.downloadAllData(urls).map { allSpeeches =>
      val stats = statsCalculator.calculateStats(allSpeeches)
      stats
    }
  }
}
