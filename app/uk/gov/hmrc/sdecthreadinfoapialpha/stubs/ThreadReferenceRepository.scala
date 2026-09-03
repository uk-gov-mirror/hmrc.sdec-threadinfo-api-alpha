/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.sdecthreadinfoapialpha.stubs

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.ThreadReferenceNotFoundException
import uk.gov.hmrc.sdecthreadinfoapialpha.model.{ThreadReference, ThreadStatus}
import uk.gov.hmrc.sdecthreadinfoapialpha.repository.ThreadReferenceRepositoryAlgebra

import java.time.{LocalDate, LocalDateTime}
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class ThreadReferenceRepository extends ThreadReferenceRepositoryAlgebra {

  private val threadReferenceCache: Cache[String, ThreadReference] = Scaffeine()
    .build[String, ThreadReference]()

  private val activeMessage: String =
    """
      |Dear Jenny
      |
      |We are reviewing your recent submission and need some further information before we can continue.
      |
      |Please reply to this thread with any details that may help us assess your case, including anything that has changed since your original submission.
      |
      |Best regards
      |
      |HMRC
      """.stripMargin

  private val closedMessage: String =
    """
      |Dear Jenny
      |
      |HMRC has concluded that we will close this thread.
      |
      |Thanks for your cooperation.
      |
      |Best regards
      |
      |HMRC
      """.stripMargin

  seedDummyData()

  private def seedDummyData(): Unit = {
    insertThreadReference(
      ThreadReference(
        id = "THREAD1000AA",
        recipientName = Some("Jenny Worthy"),
        message = Some(activeMessage),
        status = ThreadStatus.Active,
        createdTimeStamp = LocalDateTime.now().minusDays(2),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(3),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-001"
      )
    )

    insertThreadReference(
      ThreadReference(
        id = "THREAD2000BB",
        recipientName = Some("Jenny Worthy"),
        message = None,
        status = ThreadStatus.Draft,
        createdTimeStamp = LocalDateTime.now().minusDays(1),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(2),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-002"
      )
    )

    insertThreadReference(
      ThreadReference(
        id = "THREAD3000CC",
        recipientName = Some("Jenny Worthy"),
        message = Some(closedMessage),
        status = ThreadStatus.Closed,
        createdTimeStamp = LocalDateTime.now().minusDays(1),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(2),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-003"
      )
    )

    insertThreadReference(
      ThreadReference(
        id = "THREAD4000DD",
        recipientName = Some("Jenny Worthy"),
        message = Some(closedMessage),
        status = ThreadStatus.Archived,
        createdTimeStamp = LocalDateTime.now().minusDays(1),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(2),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-004"
      )
    )

    insertThreadReference(
      ThreadReference(
        id = "THREAD5000EE",
        recipientName = None,
        message = None,
        status = ThreadStatus.Draft,
        createdTimeStamp = LocalDateTime.now().minusDays(1),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(2),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-005"
      )
    )
  }

  def insertThreadReference(threadRef: ThreadReference): Future[Unit] = {
    threadReferenceCache.put(threadRef.id, threadRef)
    Future.successful(())
  }

  override def getByThreadReference(id: String): Future[ThreadReference] =
    threadReferenceCache
      .getIfPresent(id)
      .fold(
        Future.failed(ThreadReferenceNotFoundException(id))
      )(Future.successful)
}
