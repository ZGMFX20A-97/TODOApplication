package com.example.todo.service.task;

import com.example.todo.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//ビジネスロジック層の定義
@Service
@RequiredArgsConstructor 
public class TaskService {

    //LomBokの@RequiredArgsConstructor にてデータアクセスオブジェクトが自動生成される
    private final TaskRepository taskRepository;

    //全てのタスクをリストに入れてプレゼンテーション層へ返す
    public List<TaskEntity> find(TaskSearchEntity searchEntity) {

        return taskRepository.select(searchEntity);

    }

    //特定idのタスクを取得して返す。
    //空時の例外処理を簡単にするためOptionalでラップする
    public Optional<TaskEntity> findById(long taskId) {
        return taskRepository.selectById(taskId);
    }

    //トランザクションを張ることによって挿入操作が失敗した時のデータ整合性を保証する
    @Transactional
    public void create(TaskEntity newEntity) {
        taskRepository.insert(newEntity);
    }

    @Transactional
    public void update(TaskEntity entity) {
        taskRepository.update(entity);

    }
    @Transactional
    public void delete(long id) {
        taskRepository.delete(id);
    }
}
