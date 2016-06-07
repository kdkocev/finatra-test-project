package com.mainpackage.fitman.api

import java.time.Instant

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.exceptions.BadRequestException
import com.twitter.finatra.validation.{Size, Range}

import scala.collection.mutable

/**
  * Created by kamen on 07.06.16.
  */
class WeightResource extends Controller {

  val db = mutable.Map[String, List[Weight]]()

  get("/weights") { request: Request =>
    info("finding all weights for all users ...")
    db
  }

  get("/weights/:user") { request: Request =>
    info(s"""finding weight for ${request.params("user")}""")
    db.getOrElse(request.params("user"), List())
  }

  post("/weights") { weight: Weight =>
    val r = time(s"Total Time take to post weight for user '${weight.user}' is %d ms") {
      val weightsForUser = db.get(weight.user) match {
        case Some(weights) => weights :+ weight
        case None => List(weight)
      }
      db.put(weight.user, weightsForUser)
      response.created.location(s"/weights/${weight.user}")
    }
    r
  }

  get("/request-info") { request: Request =>
    println(request.remoteAddress)
    println(request.path)
    println(request.userAgent)
    response.accepted.body("done").toFuture
  }

}


case class Weight (
              @Size(min = 1, max = 25) user:String,
              @Range(min = 25, max = 200) weight: Int,
              status: Option[String],
              posted_at: String = Instant.now().toString)