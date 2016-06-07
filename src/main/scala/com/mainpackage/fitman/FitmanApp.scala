package com.mainpackage.fitman

import com.mainpackage.fitman.api.WeightResource
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}

/**
  * Created by kamen on 07.06.16.
  */
object FitmanApp extends FitmanServer{

}

class FitmanServer extends HttpServer {

  override protected def defaultFinatraHttpPort: String = ":8000"
  override protected def defaultTracingEnabled: Boolean = false
  override protected def defaultHttpServerName: String = "FitMan"

  override protected def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[CommonFilters]
      .add(new HelloController)
      .add(new WeightResource)
  }
}

class HelloController extends Controller {

  get("/hello") { request: Request =>
    "Fitman says hello"
  }

}