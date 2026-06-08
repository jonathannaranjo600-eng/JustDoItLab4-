package teccr.justdoitcloud.service;

import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    public void addTaskToUser(User user, Task task) {
        task.setUserId(user.getId());
        taskRepository.save(task);
    }

    public void advanceTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            Task.Status newStatus = switch (task.getStatus()) {
                case PENDING -> Task.Status.INPROGRESS;
                case INPROGRESS -> Task.Status.DONE;
                default -> task.getStatus();
            };
            Task updated = new Task(
                    task.getId(),
                    task.getDescription(),
                    task.getCreatedAt(),
                    task.getDeadline(),
                    newStatus
            );
            updated.setUserId(task.getUserId());
            taskRepository.save(updated);
        });
    }
}
