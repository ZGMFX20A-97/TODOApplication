package com.example.todo.repository.task;

import com.example.todo.service.task.TaskEntity;
import com.example.todo.service.task.TaskSearchEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository {
    @Select("""
            <script>
            select id,summary,description,status from tasks
            <where>
             <if test='condition.summary != null and !condition.summary.isBlank()'>
             summary like concat('%',#{condition.summary},'%')
             </if>
             <if test='condition.status != null and !condition.status.isEmpty()'>
             and status in(
             <foreach item='item' index='index' collection='condition.status' separator=','>
             #{item}
             </foreach>
             )
             </if>
            </where>
            </script>
            """)
    List<TaskEntity> select(@Param("condition") TaskSearchEntity condition);

    @Select("select id,summary,description,status from tasks where id=#{taskId};")
    Optional<TaskEntity> selectById(@Param("taskId") long taskId);

    @Insert("""
            insert into tasks(summary,description,status)
            values(#{task.summary},#{task.description},#{task.status});
            """)
    void insert(@Param("task") TaskEntity newEntity);

    @Update("""
            update tasks
            set
                summary=#{task.summary},
                description=#{task.description},
                status=#{task.status}
            where
                id=#{task.id}
            """)
    void update(@Param("task") TaskEntity entity);
    @Delete("""
            delete from tasks where id = #{taskId}
            """)
    void delete(@Param("taskId") long id);
}
