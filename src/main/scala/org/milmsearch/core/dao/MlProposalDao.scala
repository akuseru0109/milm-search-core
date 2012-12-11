package org.milmsearch.core.dao

import mapper._

import org.milmsearch.core.ComponentRegistry.{dateTimeService => Time}
import org.milmsearch.core.domain.CreateMlProposalRequest
import org.milmsearch.core.domain.MlArchiveType
import org.milmsearch.core.domain.MlProposal
import org.milmsearch.core.domain.MlProposalStatus

import net.liftweb.mapper.CreatedUpdated
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedEmail
import net.liftweb.mapper.MappedEnum
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedText
import net.liftweb.mapper.MappedDateTime

/**
 * ML登録申請情報 の DAO
 */
trait MlProposalDao {
  def find(id: Long): Option[MlProposal]
  def create(request: CreateMlProposalRequest): Long
}

/**
 * MlProposalDao の実装クラス
 */
class MlProposalDaoImpl extends MlProposalDao {
  def find(id: Long) = None
  def create(request: CreateMlProposalRequest) = toMapper(request).saveMe().id
  
  /**
   * ML登録申請情報ドメインを Mapper オブジェクトに変換する
   */
  private def toMapper(request: CreateMlProposalRequest): MlProposalMapper = {
    val now = Time().now().toDate
    MlProposalMetaMapper.create
      .proposerName(request.proposerName)
      .proposerEmail(request.proposerEmail)
      .mlTitle(request.mlTitle)
      .status(request.status.toString)
      .archiveType(request.archiveType map { _.toString } getOrElse null)
      .archiveUrl(request.archiveUrl map { _.toString } getOrElse null)
      .message(request.comment getOrElse null)
      .createdAt(now)
      .updatedAt(now)
  }
}

/**
 * O/R マッパー
 */
package mapper {

  /**
   * ML登録申請情報テーブルの操作を行う
   */
  private[dao] object MlProposalMetaMapper extends MlProposalMapper
      with LongKeyedMetaMapper[MlProposalMapper] {
    override def dbTableName = "ml_proposal"
    override def fieldOrder = List(
      id, proposerName, proposerEmail, mlTitle, status,
      archiveType, archiveUrl, message, createdAt, updatedAt
    )
  }
  
  /**
   * ML登録申請情報のモデルクラス
   */
  private[dao] class MlProposalMapper extends
      LongKeyedMapper[MlProposalMapper] with IdPK {
    def getSingleton = MlProposalMetaMapper
  
    object proposerName extends MappedString(this, 200)
    object proposerEmail extends MappedEmail(this, 200)
    object mlTitle extends MappedString(this, 200)
    object status extends MappedString(this, 200)
    object archiveType extends MappedString(this, 200)
    object archiveUrl extends MappedText(this)
    object message extends MappedText(this)
    object createdAt extends MappedDateTime(this)
    object updatedAt extends MappedDateTime(this)
  }
}
