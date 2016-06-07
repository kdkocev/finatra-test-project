package com.mainpackage.fitman.api

import com.mainpackage.fitman.FitmanServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

/**
  * Created by kamen on 07.06.16.
  */
class WeightResourceFeatureTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(
    twitterServer = new FitmanServer
  )

  "WeightResource" should {
    "Save user weight when POST request is made" in {
      server.httpPost(
        path = "/weights",
        postBody =
          """
            |{
            |"user":"kamen",
            |"weight":70,
            |"status":"Feeling awesome!!!"
            |}
          """.stripMargin,
        andExpect = Status.Created,
        withLocation = "/weights/kamen"
      )
    }
  }

  "List all weights for a user when GET request is made" in {
    val response = server.httpPost(
      path = "/weights",
      postBody =
        """
          |{
          |"user":"test_user_1",
          |"weight":90,
          |"posted_at":"2016-01-03T14:34:08.981Z"
          |}
        """.stripMargin,
      andExpect = Status.Created
    )

    println(response.location.get)

    server.httpGetJson[List[Weight]](
      path = response.location.get,
      andExpect = Status.Ok,
      withJsonBody =
        """
          |[
          |  {
          |    "user" : "test_user_1",
          |    "weight" : 90,
          |    "posted_at" : "2016-01-03T14:34:08.981Z"
          |  }
          |]
        """.stripMargin
    )
  }

  "Bad request when user is not present in request" in {
    server.httpPost(
      path = "/weights",
      postBody =
        """
          |{
          |"weight":85
          |}
        """.stripMargin,
      andExpect = Status.BadRequest
    )
  }


}
