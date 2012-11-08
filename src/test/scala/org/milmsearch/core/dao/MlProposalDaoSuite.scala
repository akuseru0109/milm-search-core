package org.milmsearch.core.dao
import org.scalatest.FunSuite
import org.milmsearch.core.ComponentRegistry
import org.milmsearch.core.domain.MlProposal
import org.milmsearch.core.domain.MlProposalStatus
import org.scalatest.BeforeAndAfterAll
import org.milmsearch.core.Bootstrap
import net.liftweb.util.Props
import net.liftweb.mapper.DB
import net.liftweb.mapper.Schemifier
import mapper.MlProposalMetaMapper

class MlProposalDaoSuite extends FunSuite with BeforeAndAfterAll {
  // TODO

  override def beforeAll {
    Schemifier.schemify(true, Schemifier.infoF _,
      MlProposalMetaMapper)
  }

  test("insert full") { pending }
  test("delete_正常") {
    val id = 1L
    DB.runUpdate("INSERT INTO ml_proposal VALUES(?,?,?,?,?,?,?,?,?,?)", 
        List(1, "name2", "sample2@sample.com", "title2", 2, 1,
            "http://sample.com2", "message2", "2012-10-10 10:10:11", "2012-10-11 10:10:11"
            )) // 一旦挿入して、（prepared statement）
    expect(true) {
      new MlProposalDaoImpl().delete(id) // それを削除する
    }
    expect(0) { // 削除結果を確認する
      val (columns, rows) = DB.runQuery("SELECT COUNT(id) FROM ml_proposal") //件数を取得するSQL
      rows.head.head.toInt // runQuery の戻り値は (List(COUNT(ID)),List(List(0)))
    }
  }

  test("delete_idなし") {
    val id = 1L
    expect(false) {
      new MlProposalDaoImpl().delete(id)
    }
    expect(0) {
      val (columns, rows) = DB.runQuery("SELECT COUNT(id) FROM ml_proposal") //件数を取得するSQL
      rows.head.head.toInt // runQuery の戻り値は (List(COUNT(ID)),List(List(0)))
    }
  }
}