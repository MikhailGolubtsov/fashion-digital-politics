package de.fashiondigital.politics.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller
import de.fashiondigital.politics.codecs.CirceJsonCodecs._
import de.fashiondigital.politics.domain.StatsEvaluationService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}

class StatsEvaluationRoute(service: StatsEvaluationService)(
  implicit ec: ExecutionContext)
  extends FailFastCirceSupport {

  implicit val uriUnmarshaller = Unmarshaller.apply[String, Uri] { implicit ec => str =>
    Future(Uri.apply(str))
  }

  def evaluationRoute: Route = {
    path("evaluation") {
      parameter('url.as(uriUnmarshaller).*) { urls =>
        get {
          onComplete({
            if (!urls.isEmpty) {
              service.evaluateStats(urls.toList).map { stats =>
                complete(stats)
              }
            } else {
              Future.successful(
                errorResponse(BadRequest, "At least one url must be specified in url parameter")
              )
            }
          }) {
            case scala.util.Success(response) => response
            // TODO in theory it'd be logical to be able to distinguish between different types of errors
            // in downloading files. If client return 4xx, then the response code should be also 400
            case scala.util.Failure(e) => {
              errorResponse(InternalServerError, s"Unexpected server error - ${e.getMessage}")
            }
          }
        }
      }
    }

  }

  private def errorResponse(statusCode: StatusCode, errorMsg: String): Route = {
    complete(statusCode -> Map("errorMsg" -> errorMsg).asJson)
  }
}
