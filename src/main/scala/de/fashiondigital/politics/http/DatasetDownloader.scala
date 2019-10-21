package de.fashiondigital.politics.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.stream.Materializer
import akka.stream.scaladsl.{Framing, Sink, Source}
import akka.util.ByteString
import de.fashiondigital.politics.codecs.SpeechCsvParser
import de.fashiondigital.politics.domain.model.Speech

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait DatasetDownloader {

  def downloadAllData(urls: List[Uri]): Future[Seq[Speech]]

}

object DatasetDownloader {

  def apply(csvParser: SpeechCsvParser,
            // TODO configure
            downloadFilesParallelism: Int = 10)(
             implicit ec: ExecutionContext,
             actorSystem: ActorSystem,
             materializer: Materializer): DatasetDownloader = new DatasetDownloader() {

    override def downloadAllData(urls: List[Uri]): Future[Seq[Speech]] = {

      val requests = urls.map { url =>
        (HttpRequest(method = HttpMethods.GET, uri = url), url)
      }

      val source = Source.apply(requests)
      val (_, results) =
        Http()
          .superPool[Uri]()
          // download files in parallel constructing a resulting stream as data arrives
          .flatMapMerge(downloadFilesParallelism, {
            // according to REST specs only 200 is a valid response code here (redirects are not followed)
            case (Success(response), _) if response.status.intValue() == 200 =>
              response
                .entity
                .dataBytes
                .via(Framing.delimiter(delimiter = ByteString("\n"), maximumFrameLength = 1000)) // TODO configure
                .map(_.utf8String)
                .drop(1) // drop first CSV header line
                .map(csvParser.parseRow)
            case (Success(response), url) =>
              Source.failed(new Exception(s"Unexpected response '${response.status.value}' calling url - '${url}'"))
            case (Failure(e), url) =>
              Source.failed(new Exception(s"Unexpected error calling url - '${url}'", e))
          })
          // TODO limit seq to avoid OOM when too many rows
          .runWith(source, Sink.seq)

      results

    }

  }
}