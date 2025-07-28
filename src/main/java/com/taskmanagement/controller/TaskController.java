package com.taskmanagement.controller;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("tasks", taskService.getUserTasks(user));
        return "tasks/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Task task = taskService.getById(id, user);
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute Task updated, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Task existing = taskService.getById(id, user);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setDeadline(updated.getDeadline());
        existing.setPriority(updated.getPriority());
        existing.setStatus(updated.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        taskService.save(existing);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Task task = taskService.getById(id, user);
        taskService.delete(task);
        return "redirect:/tasks";
    }
}