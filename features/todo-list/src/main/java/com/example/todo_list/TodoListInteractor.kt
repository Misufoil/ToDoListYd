package com.example.todo_list

//interface TodoListInteractor {
//    fun getAllTodo(): Flow<RequestResult<List<TodoUI>>>
//    suspend fun deleteTodo(todoUI: TodoUI)
//    suspend fun insertTodo(todoUI: TodoUI)
//}
//
//class TodoListInteractorImpl @Inject constructor(private val repository: TodoRepository) :
//    TodoListInteractor {
//    override fun getAllTodo(): Flow<RequestResult<List<TodoUI>>> {
//        return repository.getAll().map { requestResult ->
//            requestResult.map { todos ->
//                todos.map { it.toTodoUI() }
//            }
//        }
//    }
//
//    override suspend fun deleteTodo(todoUI: TodoUI) {
//        repository.deleteTodoToLocalBD(todoUI.toTodo())
//    }
//
//    override suspend fun insertTodo(todoUI: TodoUI) {
//        repository.insertTodoToLocalBD(todoUI.toTodo())
//    }
//}

