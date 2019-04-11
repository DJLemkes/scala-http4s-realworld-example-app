package com.hhandoko.realworld.auth

//import java.time.Instant
//
//import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import pdi.jwt.JwtAlgorithm

import com.hhandoko.realworld.core.JwtToken

trait JwtSupport {

  // TODO: Move to configuration
  final val SECRET = "S3cret!"
  final val ALGO = JwtAlgorithm.HS256

  def generateToken(): JwtToken = {
    // TODO: Make expiration configurable, and use Scala time classes
    // FIXME: Graal native-image throws runtime error
    // See: https://github.com/oracle/graal/issues/1152
//    val claim = JwtClaim(
//      expiration = Some(Instant.now.plusSeconds(157784760).getEpochSecond),
//      issuedAt = Some(Instant.now.getEpochSecond)
//    )
//
//    JwtToken(JwtCirce.encode(claim, SECRET, ALGO))
    JwtToken("some.fake.token")
  }

}