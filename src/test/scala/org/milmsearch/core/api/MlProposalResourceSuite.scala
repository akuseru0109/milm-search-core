package org.milmsearch.core.api
import java.net.URI
import java.net.URL

import org.milmsearch.core.domain.MlArchiveType
import org.milmsearch.core.domain.CreateMlProposalRequest
import org.milmsearch.core.domain.MlProposalStatus
import org.milmsearch.core.service.MlProposalService
import org.milmsearch.core.ComponentRegistry
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory
import org.scalatest.FunSuite

class MlProposalResourceSuite extends FunSuite
  with MockFactory with ProxyMockFactory {

  test("create full") {
    val json = """
      |{
      |  "proposerName": "申請者の名前",
      |  "proposerEmail": "proposer@example.com",
      |  "mlTitle": "MLタイトル",
      |  "status": "new",
      |  "archiveType": "mailman",
      |  "archiveUrl": "http://localhost/path/to/archive/",
      |  "comment": "コメント(MLの説明など)"
      |}""".stripMargin

    val request = CreateMlProposalRequest(
      "申請者の名前",
      "proposer@example.com",
      "MLタイトル",
      MlProposalStatus.New,
      Some(MlArchiveType.Mailman),
      Some(new URL("http://localhost/path/to/archive/")),
      Some("コメント(MLの説明など)"))

    val m = mock[MlProposalService]
    m expects 'create withArgs(request) returning 1L

    val response = ComponentRegistry.mlProposalService.doWith(m) {
      new MlProposalResource().create(json)
    }

    expect(201) { response.getStatus }
    expect(new URI("/ml-proposal/1")) {
      response.getMetadata().getFirst("Location")
    }
  }

  test("delete_正常") {
    // mockは戻り値なしで良い。
    val id = "1"

    val m = mock[MlProposalService]
    m expects 'delete withArgs (1L) returning true

    val response = ComponentRegistry.mlProposalService.doWith(m) {
      new MlProposalResource().delete(id)
    }

    expect(204) {
      response.getStatus
    }
  }

  test("delete_id該当なし") {
    // mockは戻り値なしで良い。
    val id = "1"

    val m = mock[MlProposalService]
    m expects 'delete withArgs (1L) returning false

    val response = ComponentRegistry.mlProposalService.doWith(m) {
      new MlProposalResource().delete(id)
    }

    expect(404) {
      response.getStatus
    }
  }

  test("delete_id数値エラー") {
    // mockは戻り値なしで良い。
    val id = "a"

    val m = mock[MlProposalService]
    //m expects 'delete withArgs (1L) returning true

    val response = ComponentRegistry.mlProposalService.doWith(m) {
      new MlProposalResource().delete(id)
    }

    expect(400) {
      response.getStatus
    }
  }
  
    test("delete_サーバエラー") {
    // mockが例外を発生させる
    val id = "1"

    val m = mock[MlProposalService]
    m expects 'delete withArgs (1L) throws new RuntimeException("Server Error!")

    val response = ComponentRegistry.mlProposalService.doWith(m) {
      new MlProposalResource().delete(id)
    }

    expect(500) {
      response.getStatus
    }
  }

}