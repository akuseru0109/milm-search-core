package org.milmsearch.core.service
import java.net.URL

import org.milmsearch.core.dao.MlProposalDao
import org.milmsearch.core.domain.MlArchiveType
import org.milmsearch.core.domain.CreateMlProposalRequest
import org.milmsearch.core.domain.MlProposalStatus
import org.milmsearch.core.ComponentRegistry
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory
import org.scalatest.FunSuite

class MlProposalServiceSuite extends FunSuite
    with MockFactory with ProxyMockFactory {

  test("create full") {
    val request = CreateMlProposalRequest(
      "申請者の名前",
      "proposer@example.com",
      "MLタイトル",
      MlProposalStatus.New,
      Some(MlArchiveType.Mailman),
      Some(new URL("http://localhost/path/to/archive/")),
      Some("コメント(MLの説明など)\nほげほげ)")
    )

    val m = mock[MlProposalDao]
    m expects 'create withArgs(request) returning 1L

    expect(1L) {
      ComponentRegistry.mlProposalDao.doWith(m) {
        new MlProposalServiceImpl().create(request)
      }
    }
  }

  test("delete_正常") { ////
    // mockは戻り値なしで良い。戻り値がない場合のexpectの書き方は後ほど
    val id = 1L

    val m = mock[MlProposalDao]
    m expects 'delete withArgs (1L) returning true

    expect(true) {
      ComponentRegistry.mlProposalDao.doWith(m) {
        new MlProposalServiceImpl().delete(id)
      }
    }
  }

  test("delete_id該当なし") { ////
    // mockは戻り値なしで良い。
    val id = 1L

    val m = mock[MlProposalDao]
    m expects 'delete withArgs (1L) returning false

    expect(false) {
      ComponentRegistry.mlProposalDao.doWith(m) {
        new MlProposalServiceImpl().delete(id)
      }
    }
  }

  test("delete_サーバエラー") { ////
    // mockが例外を発生させる
    val id = 1L

    val m = mock[MlProposalDao]
    m expects 'delete withArgs (1L) throws new RuntimeException("Server Error!")

    intercept[RuntimeException] {
      ComponentRegistry.mlProposalDao.doWith(m) {
        new MlProposalServiceImpl().delete(id)
      }
    }
  }

}