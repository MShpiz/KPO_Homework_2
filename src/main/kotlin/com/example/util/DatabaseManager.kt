package com.example.util

import util.PropertiesUtil
import java.sql.DriverManager
import java.sql.ResultSet

class DatabaseManager {
    companion object {
        private const val URL_KEY: String = "db.url"
        private const val PASS: String = "db.pass"
        private const val USERNAME: String = "db.username"
    }

    private fun getConnection() = DriverManager.getConnection(
        PropertiesUtil.get(URL_KEY),
        PropertiesUtil.get(USERNAME),
        PropertiesUtil.get(PASS)
    )

    fun execute(sql: String): Boolean {
        val conn = getConnection()
        try {
            conn.use {
                val query = conn.prepareStatement(sql)
                return query.execute()
            }

        } catch (e: Exception) {
            conn.close()

            println(e.message)
            return false
        }
    }

    fun executeQuery(sql: String, args: List<Any>): ResultSet? {
        val conn = getConnection()

        try {
            conn.use {
                val query = conn.prepareStatement(sql)
                var idx = 1
                for (arg in args){

                    if (arg is UInt){
                        query.setInt(idx++, arg.toInt())
                    }
                    else if (arg is Int){
                        query.setInt(idx++, arg)
                    }
                    else{
                        query.setString(idx++, arg.toString())
                    }
                }
                return query.executeQuery()
            }

        } catch (e: Exception) {
            conn.close()

            println(e.message)
            return null
        }
    }

    fun update(sql: String,  args: List<Any>): Int {

        val conn = getConnection()
        try {
            conn.use {
                val query = conn.prepareStatement(sql)
                var idx = 1
                for (arg in args){

                    if (arg is UInt){
                        query.setInt(idx++, arg.toInt())
                    }
                    else if (arg is Int){
                        query.setInt(idx++, arg)
                    }
                    else{
                        query.setString(idx++, arg.toString())
                    }
                }
                println(query.toString())
                return query.executeUpdate()
            }

        } catch (e: Exception) {
            conn.close()

            println(e.message)
            return 1
        }
    }
}