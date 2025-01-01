package com.example.todo.service.task;

import java.util.List;
import java.util.Optional;

public record TaskSearchDTO (
        String summary,
        List<String> statuslist
){
    public boolean isChecked(String status){
        return Optional.ofNullable(statuslist)
                .map(l->l.contains(status))
                .orElse(false);
    }
}
