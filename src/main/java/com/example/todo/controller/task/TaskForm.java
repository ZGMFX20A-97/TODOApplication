package com.example.todo.controller.task;

import com.example.todo.service.task.TaskEntity;
import com.example.todo.service.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//フロントからデータ受け付けるためのオブジェクト
public record TaskForm(

        //アノテーションをつけて入力値を検証する
        @NotBlank
        @Size(max = 256,message = "256文字以内で入力してください")
        String summary,
        String description,
        @NotBlank(message = "空は許されません")
        @Pattern(regexp = "TODO|DOING|DONE",message = "TODO,DOING,DONEのいずれかを指定してください")
        String status) {
    
    //TaskEntityをフロントで使うTaskFormへ変換するためのメソッド
    public static TaskForm fromEntity(TaskEntity taskEntity) {
        return new TaskForm(taskEntity.summary(), taskEntity.description(), taskEntity.status().name());
    }

    //フロントから受け付けたTaskFormオブジェクトをロジック層用のTaskEntityに変換するメソッド（新規作成用）
    public TaskEntity toEntity() {
        return new TaskEntity(null, summary(), description(), TaskStatus.valueOf(status()));
    }

    //フロントから受け付けたTaskFormオブジェクトをロジック層用のTaskEntityに変換するメソッド（編集用）
    public TaskEntity toEntity(long id) {
        return new TaskEntity(id, summary(), description(), TaskStatus.valueOf(status()));
    }
}
