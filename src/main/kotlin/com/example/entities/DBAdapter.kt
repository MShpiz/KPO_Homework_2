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
            create table if not exists userActivity (
            id serial primary key,
            userId int,
            action int,
            time string); 
        """.trimIndent()
        manager.execute(createUserActivityTable)

        val createIncomeTable= """
            create table if not exists income (
            id serial primary key,
            date string,
            sum int); 
        """.trimIndent()
        manager.execute(createIncomeTable)
    }

    fun addUser(login: String, password: Int, role: Int) {
        val query = """
            select * from users
            where login = '?'
            """.trimIndent()
        val result = manager.
        executeQuery(query, listOf(login, password.toString(), role.toString())) ?: throw NullPointerException()
        if (result.next()){
            throw InvalidParameterException("User with this login already exists")
        }
        val insertQuery = """
        insert into users (login, password, role)
        values ('?', '?', '?');
        """.trimIndent()
        manager.update(insertQuery, listOf(login, password.toString(), role.toString()))
    }

    fun addMeal(name: String, price: UInt, cookingTime: UInt, amount: UInt) {
        val query = """
        select * from meals
        where name = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(name)) ?: throw NullPointerException()
        if (result.next()){
            throw InvalidParameterException()
        }
        val insertQuery = """
        insert into meals (name, price, amount, cookingTime)
        values ('?', '?','?', '?');
        """.trimIndent()
        manager.update(insertQuery, listOf(name, price.toString(), amount.toString(), cookingTime.toString()))
    }

    fun addUserActivity(userId: Int, typeOfAction: Int) {
        val query = """
        select * from users
        where id = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(userId.toString())) ?: throw NullPointerException()
        if (!result.next()){
            throw InvalidParameterException()
        }
        val insertQuery = """
        insert into userActivity (userId, action, time)
        values ('?', '?', '?');
        """.trimIndent()
        manager.update(insertQuery, listOf(userId.toString(), typeOfAction.toString(), dateTimeFormat.format(Date())))
    }

    fun addPayment(sum: UInt){
        val query = """
        insert into income (date, sum)
        values ('?','?');
        """.trimIndent()
        manager.update(query, listOf(dateFormat.format(Date()), sum.toString()))
    }

    fun getUser(login:String, passwordHash: Int): User {
        val query = """
        select * from users
        where login = '?'
        and password = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(login, passwordHash.toString())) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        if (result.getInt("role") == 1)
            return Admin( result.getInt("id").toUInt(), result.getString("login"), result.getInt("password"))
        return Visitor(result.getInt("id").toUInt(), result.getString("login"), result.getInt("password"))
    }

    fun getUserActivity(): MutableList<UserActivity>{
        val data: MutableList<UserActivity> = mutableListOf<UserActivity>()
        val query = """
            select * from userActivity;
        """.trimIndent()
        val result = manager.executeQuery(query, listOf()) ?: throw NullPointerException()
        while (result.next()){
            data.add(UserActivity(result.getInt("userId"), result.getString("typeOfAction"),
                result.getString("time")))
        }
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
        val query = """
            select * from meals
            where id = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId.toString())) ?: throw NullPointerException()
        val currAmount = result.getInt("amount")
        if (currAmount <= 0){
            throw InvalidParameterException("not enough meals")
        }
        val updateQuery = """
            UPDATE meals SET amount = '?'
            WHERE id = '?';
        """.trimIndent()
        manager.update(updateQuery, listOf((currAmount - 1).toString(), mealId.toString()))
    }

    fun increaseMealAmount(mealId: Int, amount: UInt = 1u){
        val query = """
            select * from meals
            where id = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId.toString())) ?: throw NullPointerException()
        val currAmount = result.getInt("amount")
        val updateQuery = """
            UPDATE meals SET amount = '?'
            WHERE id = '?';
        """.trimIndent()
        manager.update(updateQuery, listOf((currAmount+amount.toInt()).toString(), mealId.toString()))
    }

    fun changeMealPrice(mealId: Int, price: UInt){
        val query = """
            select * from meals
            where id = '?';
        """.trimIndent()
        manager.executeQuery(query, listOf(mealId.toString())) ?: throw NullPointerException()

        val updateQuery = """
            UPDATE meals SET price = '?'
            WHERE id = '?';
        """.trimIndent()
        manager.update(updateQuery, listOf(price.toString(), mealId.toString()))
    }

    fun getMeal(mealId: Int) : Meal {
        val query = """
        select * from meals
        where id = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId.toString())) ?: throw NullPointerException()

        return Meal(result.getString("name"), result.getInt("cookingTime").toUInt(), result.getInt("price").toUInt(), result.getInt("id"))
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
        val query = """
        select * from meals
        where id = '?';
        """.trimIndent()
        val result = manager.executeQuery(query, listOf(mealId.toString())) ?: throw NullPointerException()
        if (!result.next()){
            throw NullPointerException()
        }
        val deleteQuery = """
        delete from meals
        where id = '?'
        """.trimIndent()
        manager.update(deleteQuery, listOf(mealId.toString()))
    }

}