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

package uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository

import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.StaffRole

import scala.concurrent.Future

trait StaffRoleRepositoryAlgebra {

  def findById(id: Long)(using hc: HeaderCarrier): Future[Option[StaffRole]]

  def findByStaffId(staffId: Long)(using hc: HeaderCarrier): Future[Seq[StaffRole]]

  def findByTeamId(teamId: Long)(using hc: HeaderCarrier): Future[Seq[StaffRole]]

  def findByStaffAndTeam(
    staffId: Long,
    teamId:  Long
  )(using hc: HeaderCarrier): Future[Option[StaffRole]]

  def insert(staffRole: StaffRole)(using hc: HeaderCarrier): Future[Long]

  def update(staffRole: StaffRole)(using hc: HeaderCarrier): Future[Int]

  def delete(id: Long)(using hc: HeaderCarrier): Future[Int]
}
