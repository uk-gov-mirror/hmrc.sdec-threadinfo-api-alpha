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

package uk.gov.hmrc.sdecthreadinfoapialpha.hcp.connector

import play.api.libs.json.Json
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.sdecthreadinfoapialpha.config.AppConfig
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.StaffRoleRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.StaffRole
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HCPStaffRoleDataAccess @Inject(
  httpClient: HttpClientV2,
  appConfig: AppConfig
)(using ec: ExecutionContext) extends StaffRoleRepositoryAlgebra {
  private val StaffResource = "staff"

  override def findById(id: Long)(using hc: HeaderCarrier): Future[Option[StaffRole]] =
    httpClient
      .get(url"${appConfig.ipaasUrl}/$StaffResource/$id")
      .execute[Option[StaffRole]]

  override def findByStaffId(staffId: Long)(using hc: HeaderCarrier): Future[Seq[StaffRole]] =
    httpClient
      .get(url"${appConfig.ipaasUrl}/$StaffResource/staff/$staffId")
      .execute[Seq[StaffRole]]

  override def findByTeamId(teamId: Long)(using hc: HeaderCarrier): Future[Seq[StaffRole]] =
    httpClient
      .get(url"${appConfig.ipaasUrl}/$StaffResource/team/$teamId")
      .execute[Seq[StaffRole]]

  override def findByStaffAndTeam(staffId: Long, teamId: Long)(using hc: HeaderCarrier): Future[Option[StaffRole]] =
    httpClient
      .get(url"${appConfig.ipaasUrl}/$StaffResource/staff/$staffId/team/$teamId")
      .execute[Option[StaffRole]]

  override def insert(staffRole: StaffRole)(using hc: HeaderCarrier): Future[Long] =
    httpClient
      .post(url"${appConfig.ipaasUrl}/$StaffResource/")
      .withBody(Json.toJson(staffRole))
      .execute[Long]

  override def update(staffRole: StaffRole)(using hc: HeaderCarrier): Future[Int] =
    httpClient
      .put(url"${appConfig.ipaasUrl}/$StaffResource/")
      .withBody(Json.toJson(staffRole))
      .execute[Int]

  override def delete(id: Long)(using hc: HeaderCarrier): Future[Int] =
    httpClient
      .delete(url"${appConfig.ipaasUrl}/$StaffResource/$id")
      .execute[Int]
}
