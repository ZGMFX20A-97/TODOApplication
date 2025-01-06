package com.example.todo.service.task;

//タスクの状態を表す列挙型を定義する
public enum TaskStatus {
    //これからやるべくという状態
    TODO,
    //取り込み中の状態
    DOING,
    //完成した状態
    DONE

}
