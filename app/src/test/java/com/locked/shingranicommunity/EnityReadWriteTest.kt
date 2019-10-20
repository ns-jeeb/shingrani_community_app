package com.locked.shingranicommunity

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.locked.shingranicommunity.tutorials.User
import com.locked.shingranicommunity.tutorials.UserDao
import com.locked.shingranicommunity.tutorials.UserDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import androidx.test.runner.AndroidJUnit4
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import java.lang.reflect.Array.set


@RunWith(AndroidJUnit4::class.java)
class EnityReadWriteTest {
    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, UserDatabase::class.java).build()
        userDao = db.loginDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
//        val user: User = TestCase.createUser(3).apply {
//            set("george",0,"")
//        }
//        userDao.findByName(user.name,user.username)
//        val byName = userDao.findByName("Najeeb ","na.jeeb@gmail.com")
//        assertThat(byName.username, equalTo(user.username))
    }
}