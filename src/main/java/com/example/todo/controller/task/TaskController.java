package com.example.todo.controller.task;

import com.example.todo.service.task.TaskNotFoundException;
import com.example.todo.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks") //RequestMappingにてパスの共通部分を省略することができる
public class TaskController {

    //LombokによりTaskServiceのインスタンスをDI
    private final TaskService taskservice;

    //タスク一覧を表示する(検索の場合は検索結果を)
    @GetMapping
    public String list(TaskSearchForm searchForm, Model model) {

        var taskList = taskservice.find(searchForm.toEntity()).stream().map(TaskDTO::toDTO).toList();

        //現在のタスクリストをViewに渡す
        model.addAttribute("taskList", taskList);
        model.addAttribute("searchDTO",searchForm.toDTO());

        //　/tasksからのGETrequestが来た際にはlist.htmlを返す
        return "tasks/list";
    }

    //特定タスクの詳細を表示する
    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long taskId, Model model) {//@PathVariableを使用してパス内のidを取得してtaskIdに結びつける

        var taskDTO = taskservice.findById(taskId).map(TaskDTO::toDTO)
                .orElseThrow(TaskNotFoundException::new);  //Optionalの中身が空の場合は自作の例外をスローする
        model.addAttribute("task", taskDTO);

        return "tasks/detail";
    }

    //タスク作成画面を表示する
    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form, Model model) {
        
        model.addAttribute("mode", "CREATE");
        return "tasks/form";
    }

    //作成のPOSTrequestを受け付けるエンドポイント
    @PostMapping
    public String create(@Validated TaskForm taskForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            //もしバリデーションエラーが発生した際、今の入力内容の入ったタスク作成画面を返す（UX向上のため）
            return showCreationForm(taskForm, model);
        }

        taskservice.create(taskForm.toEntity());

        //多重サブミットを防ぐためPRGパターンで一覧画面へリダイレクトする
        return "redirect:/tasks";
    }

    //タスクを編集する画面を表示する
    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        //DBから編集したいタスクを取得する
        var form = taskservice.findById(id).map(TaskForm::fromEntity)
                .orElseThrow(TaskNotFoundException::new);
        //取得したデータをTaskFormにつめてフロントへ送る
        model.addAttribute("taskForm", form);
        //編集と作成に共通のページで表示したいため、EDITの入ったmode属性をフロントに渡すことでHTMLを編集用画面に切り替える
        model.addAttribute("mode", "EDIT");

        return "tasks/form";


    }
    //タスクの更新を受け付けるエンドポイント
    @PutMapping("{id}")
    public String update(@PathVariable("id") long id,
                         @Validated @ModelAttribute("taskForm") TaskForm form,
                         BindingResult bindingresult, Model model) {
        if (bindingresult.hasErrors()) {
            model.addAttribute("mode", "EDIT");
            return "tasks/form";
        }

        var entity = form.toEntity(id);
        taskservice.update(entity);

        return "redirect:/tasks/{id}";

    }

    //タスク削除のリクエストを受け付けるエンドポイント
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {
        taskservice.delete(id);
        return "redirect:/tasks";
    }
}
