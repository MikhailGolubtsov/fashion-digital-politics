package de.fashiondigital.politics

import akka.http.scaladsl.server.{HttpApp, Route}
import akka.stream.ActorMaterializer
import de.fashiondigital.politics.codecs.SpeechCsvParser
import de.fashiondigital.politics.domain.{StatsCalculator, StatsEvaluationService}
import de.fashiondigital.politics.http.{DatasetDownloader, StatsEvaluationRoute}

object WebServer extends HttpApp {


  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override protected def routes: Route = {
    implicit val system = systemReference.get()
    implicit val executionContext = system.dispatcher
    implicit val materializer = ActorMaterializer()

    // TODO configure
    val yearToCheck = 2013
    val subjectToCheck = "Innere Sicherheit"
    val datasetDownloader = DatasetDownloader(csvParser = new SpeechCsvParser)
    val statsCalculator = new StatsCalculator(yearToCheck, subjectToCheck)
    val statsEvaluationService = new StatsEvaluationService(datasetDownloader, statsCalculator)
    val route = new StatsEvaluationRoute(statsEvaluationService)
    route.evaluationRoute
  }
}