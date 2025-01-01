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
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskservice;


    @GetMapping
    public String list(TaskSearchForm searchForm, Model model) {

        var taskList = taskservice.find(searchForm.toEntity()).stream().map(TaskDTO::toDTO).toList();
        model.addAttribute("taskList", taskList);
        model.addAttribute("searchDTO",searchForm.toDTO());
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long taskId, Model model) {
        var taskDTO = taskservice.findById(taskId).map(TaskDTO::toDTO)
                .orElseThrow(TaskNotFoundException::new);
        model.addAttribute("task", taskDTO);

        return "tasks/detail";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute("taskForm") TaskForm form, Model model) {
//        if(form==null){
//            form=new TaskForm(null,null,null);
//        }
        //model.addAttribute("taskForm",form);　@ModelAttributeによってコメントアウトされたブロックは不要になる
        model.addAttribute("mode", "CREATE");
        return "tasks/form";
    }

    @PostMapping
    public String create(@Validated TaskForm taskForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showCreationForm(taskForm, model);
        }
        taskservice.create(taskForm.toEntity());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        var form = taskservice.findById(id).map(TaskForm::fromEntity)
                .orElseThrow(TaskNotFoundException::new);

        model.addAttribute("taskForm", form);
        model.addAttribute("mode", "EDIT");

        return "tasks/form";

    }

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

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {
        taskservice.delete(id);
        return "redirect:/tasks";
    }
}
