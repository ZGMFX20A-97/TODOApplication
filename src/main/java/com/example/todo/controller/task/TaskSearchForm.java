package com.example.todo.controller.task;

import com.example.todo.service.task.TaskSearchDTO;
import com.example.todo.service.task.TaskSearchEntity;
import com.example.todo.service.task.TaskStatus;

import java.util.List;
import java.util.Optional;

//検索条件を入れるオブジェクトを定義する
public record TaskSearchForm (
        String summary,
        List<String> status
){
    public  TaskSearchEntity toEntity(){
        var statusEntityList = Optional.ofNullable(status())
                .map(statuslist->
                        statuslist.
                                stream().
                                map(TaskStatus::valueOf).
                                toList()).orElse(List.of());
        return new TaskSearchEntity(summary(), statusEntityList);
    }

    public TaskSearchDTO toDTO() {
        return new TaskSearchDTO(summary(),status());
    }
}
