package com.earldouglas.fregedb

import java.sql.DriverManager

class HsqldbFileRepo extends HsqldbRepo {
  new java.io.File("feedbag.lck").delete()
  override def jdbcUrl: String = "jdbc:hsqldb:file:fregedb"
}

class HsqldbMemRepo extends HsqldbRepo {
  override def jdbcUrl: String = "jdbc:hsqldb:mem:fregedb"
}

trait HsqldbRepo {
  
  Class.forName("org.hsqldb.jdbcDriver")

  def jdbcUrl: String
  
  val conn = DriverManager.getConnection(jdbcUrl, "SA", "")

  try {
    conn.prepareStatement("CREATE TABLE ITEMS (FEED_ID VARCHAR(128), CONTENT VARCHAR(1024), CREATED TIMESTAMP)").execute()
  } catch {
    case _ =>
  }

  def addItem(feedId: String, item: Item) {
    val insert = conn.prepareStatement("INSERT INTO ITEMS (FEED_ID, CONTENT, CREATED) VALUES (?, ?, ?)")
    insert.setString(1, feedId)
    insert.setString(2, item.content)
    insert.setDate(3, convert(item.date))
    insert.execute()
    insert.close()
  }

  def getItems(feedId: String): List[Item] = {
    val select = conn.prepareStatement("SELECT CONTENT, CREATED FROM ITEMS WHERE FEED_ID = ? ORDER BY CREATED DESC LIMIT 100")
    select.setString(1, feedId)
    val rs = select.executeQuery()

    var items: List[Item] = Nil
    while (rs.next()) items = Item(rs.getString("CONTENT"), rs.getTimestamp("CREATED")) :: items
    rs.close()
    select.close()
    items.reverse
  }

  private def convert(date: java.util.Date): java.sql.Date = new java.sql.Date(date.getTime())

}
