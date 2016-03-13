package lewis.problemset5;

import java.io.Serializable;

/*
 * A ToDoItem is a task of the user, indicating whether it is completed or not.
 */
public class ToDoItem implements Serializable{

    // fields of a ToDoItem
    private String title;
    private boolean completed;

    /*
     * Returns an uncompleted task with the given name.
     */
    public ToDoItem(String titleArg) {
        title = titleArg;
        completed = false;
    }

    /*
     * Returns the name of the task.
     */
    public String toString() {
        return title;
    }

    /*
     * Returns whether the task is completed.
     */
    public boolean isCompleted() {
        return completed;
    }

    /*
     * Reverses completedness of the item, and returns the result.
     */
    public ToDoItem reverseCompleted() {
        completed = !completed;
        return this;
    }

}
