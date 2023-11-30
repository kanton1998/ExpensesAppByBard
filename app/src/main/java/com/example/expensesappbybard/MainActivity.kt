package com.example.expensesappbybard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.cancel

import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var database: ExpensesDatabase
    private lateinit var expensesDao: ExpensesDao
    private val expenses = mutableStateOf<List<Expenses>>(emptyList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            ExpensesDatabase::class.java,
            "expenses_database"
        ).build()
        expensesDao = database.expensesDao()

        setContent {
            ExpenseList()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExpenseList() {
    val expenses by remember {
        mutableStateOf(expensesDao.getAll().toMutableList())
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                actions = {
                    IconButton(
                        onClick = {
                            addExpense()
                        }
                    ) {
                        Icon(Icons.Filled.Add)
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            content = {
                items(expenses) { expense ->
                    ExpenseItem(expense)
                }
            }
        )
    }
}

@Composable
fun ExpenseItem(expense: Expenses) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(expense.amount.toString())
                Text(expense.category)

            }

            IconButton(
                onClick = {
                    deleteExpense(expense)
                }
            ) {
                Icon(Icons.Filled.Delete)
            }
        }
    }
}

fun addExpense() {
    val amount = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = {
            scope.cancel()
        },
        title = "Neue Ausgabe hinzufügen",
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = amount.value,
                    label = { Text("Betrag") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = category.value,
                    label = { Text("Kategorie") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        buttons = {
            Button(
                onClick = {
                    scope.launch {
                        expensesDao.insert(
                            Expenses(
                                id = 0,
                                amount = amount.value.toDouble(),
                                category = category.value,
                                date = System.currentTimeMillis()
                            )
                        )
                    }
                    amount.value = ""
                    category.value = ""
                }
            ) {
                Text("Hinzufügen")
            }
        }
    )
}
