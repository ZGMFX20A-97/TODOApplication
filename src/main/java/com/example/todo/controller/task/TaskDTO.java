package com.example.todo.controller.task;

import com.example.todo.service.task.TaskEntity;

//タスクのデータトランスファーオブジェクトを定義する
public record TaskDTO (
            //タスクのID
            Long id,
            //タスクの概要
            String summary,
            //タスクの詳細
            String description,
            //タスクの完成状態
            String status){

    //EntityからDTOオブジェクトを生成するメソッドを定義する
    public static TaskDTO toDTO(TaskEntity entity) {
        return new TaskDTO(
                entity.id(),
                entity.summary(),
                entity.description(),
                entity.status().name());
    }
}
