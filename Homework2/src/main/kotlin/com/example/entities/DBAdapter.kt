package com.example.entities

import com.example.entities.*
import com.example.util.DatabaseManager
import java.security.InvalidParameterException
import java.text.SimpleDateFormat
import java.util.*

object DBAdapter {
    private val manager = DatabaseManager()
    private val dateFormat = SimpleDateFormat("dd/M/yyyy")
    private val dateTimeFormat = SimpleDateFormat("dd/M/yyyy hh:mm")

    init {
        val createUsersTable = """
            create table if not exists users (
            id serial primary key,
            login varchar(256),
            password int,
            role int); 
        """.trimIndent()
        manager.execute(createUsersTable)

        val createMealsTable = """
            create table if not exists meals (
            id serial primary key,
            name varchar(256),
            price int,
            amount int,
            cookingTime int); 
        """.trimIndent()
        manager.execute(createMealsTable)

        val createUserActivityTable = """
            create  table if not exists userActivity (
            id serial primary key,
            userId int,
            action int,
            time varchar(256)); 
        """.trimIndent()
        manager.execute(createUserActivityTable)

        val createIncomeTable= """
            create  table if not exists income (
            id serial primary key,
            date varchar(256),
            sum int); 
        """.trimIndent()
        manager.execute(createIncomeTable)
    }

    fun addUser(login: String, password: Int, role: Int) {
        val query = """
            select * from users
            where login = ?
            """.trimIndent()
        val result = manager.executeQuery(query, listOf(login)) ?: throw NullPointerException()
        println(result)
        if (result.next()){
            println("USER EXISTS")
            throw InvalidParameterException("User with this login already exists")
        }
        val insertQuery = """
        insert into users (login, password, role)
        values (?, ?, ?);
        """.trimIndent()
        manager.update(insertQuery, listOf(login, password, role))
    }

    fun addMeal(name: String, price: UInt, cookingTime: UInt, amount: UInt) {
        val query = """
        select * from meals
        where name = ?;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(name)) ?: throw NullPointerException()
        if (result.next()){
            throw InvalidParameterException()
        }
        val insertQuery = """
        insert into meals (name, price, amount, cookingTime)
        values (?, ?, ?, ?);
        """.trimIndent()
        manager.update(insertQuery, listOf(name, price, amount, cookingTime))
    }

    fun addUserActivity(userId: Int, typeOfAction: Int) {
        val insertQuery = """
        insert into userActivity (userId, action, time)
        values ( ? ,  ? ,  ? );
        """.trimIndent()
        val res = manager.update(insertQuery, listOf(userId, typeOfAction, dateTimeFormat.format(Date())))
        println(res)
    }

    fun addPayment(sum: UInt){
        val query = """
        insert into income (date, sum)
        values ( ? , ? );
        """.trimIndent()
        manager.update(query, listOf(dateFormat.format(Date()), sum))
    }

    fun getUser(login:String, passwordHash: Int): User {
        val query = """
        select * from users
        where login =  ? 
        and password =  ? ;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(login, passwordHash)) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        if (result.getInt("role") == 2)
            return Admin( result.getInt("id"), result.getString("login"), result.getInt("password"))
        return Visitor(result.getInt("id"), result.getString("login"), result.getInt("password"))
    }

    fun getUserActivity(): MutableList<UserActivity>{
        val data: MutableList<UserActivity> = mutableListOf()
        val query = """
            select * from userActivity;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf()) ?: throw NullPointerException()
        do {
            data.add(UserActivity(result.getInt("userId"), result.getInt("action"),
                result.getString("time")))
        } while (result.next())
        return data
    }

    fun getTotalIncome(): Map<String, Int>{
        val income : MutableMap<String, Int> = mutableMapOf()
        val query = """
            select * from income;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf()) ?: throw NullPointerException()
        while (result.next()){
            if (result.getString("date") !in income){
                income[result.getString("date")] = 0
            }
            income[result.getString("date")] = income[result.getString("date")]!! + result.getInt("sum")
        }
        return income
    }

    fun decreaseMealAmount(mealId: Int){
        val name = getMeal(mealId).name
        val query = """
            select * from meals
            where name =  ? ;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(name)) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        val currAmount = result.getInt("amount")
        if (currAmount <= 0){
            throw InvalidParameterException("not enough meals")
        }
        val updateQuery = """
            UPDATE meals SET amount =  ? 
            WHERE id =  ? ;
        """.trimIndent()
        manager.update(updateQuery, listOf((currAmount - 1), mealId))
    }

    fun increaseMealAmount(mealId: Int, amount: UInt = 1u){
        val query = """
            select * from meals
            where name =  ? ;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId)) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        val currAmount = result.getInt("amount")
        val updateQuery = """
            UPDATE meals SET amount =  ? 
            WHERE name =  ? ;
        """.trimIndent()
        manager.update(updateQuery, listOf((currAmount+amount.toInt()), mealId))
    }

    fun changeMealPrice(mealId: Int, price: UInt){
        val query = """
            select * from meals
            where id =  ? ;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId)) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        val updateQuery = """
            UPDATE meals SET price =  ? 
            WHERE name =  ? ;
        """.trimIndent()
        manager.update(updateQuery, listOf(price, mealId))
    }

    fun getMeal(mealId: Int) : Meal {
        val query = """
            select * from meals
            where id =  ? ;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId)) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        return Meal(result.getString("name"), result.getInt("cookingTime").toUInt(),
            result.getInt("price").toUInt(), result.getInt("id"))
    }

    fun getMenu() : MutableList<Meal>{
        val query = """
        select * from meals;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf()) ?: throw NullPointerException()
        val meals = mutableListOf<Meal>()
        while (result.next()){
            meals += Meal(result.getString("name"),result.getInt("cookingTime").toUInt(),result.getInt("price").toUInt(), result.getInt("id"))
        }
        return meals
    }

    fun removeMealFromMenu(mealId: Int){
        val name = getMeal(mealId).name
        val query = """
        select * from meals
        where name =  ? ;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(name)) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        val deleteQuery = """
        delete from meals
        where name =  ? 
        """.trimIndent()
        manager.update(deleteQuery, listOf(name))
    }

}