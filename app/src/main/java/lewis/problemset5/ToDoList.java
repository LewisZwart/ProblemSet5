package lewis.problemset5;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * A ToDoList is a list of ToDoItems.
 */
public class ToDoList implements Serializable{

    // fields of a ToDoList
    private String title;
    private ArrayList<ToDoItem> toDoList;

    /*
     * Returns default empty toDoList.
     */
    public ToDoList(String titleArg) {
        title = titleArg;
        toDoList = new ArrayList<>();
    }

    /*
     * Returns an arraylist of its items.
     */
    public ArrayList<ToDoItem> getItems() {
        return toDoList;
    }

    /*
     * Returns the item at the given position (only to be called with valid position).
     */
    public ToDoItem getItem(int position) {
        return toDoList.get(position);
    }

    /*
     * Returns its title.
     */
    public String toString() {
        return title;
    }

    /*
     * Adds a new item with the given name to the list of items.
     */
    public void addItem(String itemName) {
        ToDoItem item = new ToDoItem(itemName);
        toDoList.add(item);
    }

    /*
     * Removes the item at the given position from the list.
     */
    public boolean removeItem(int position) {

        // if position is valid, remove item at that position
        if (position >= 0 && position < toDoList.size()) {
            toDoList.remove(position);
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * Reverses the completedness of a task.
     */
    public boolean reverseCompleted(int position) {

        // if position is valid, reverse completedness of the indicated item
        if (position >= 0 && position < toDoList.size()) {
            ToDoItem item = toDoList.get(position);
            ToDoItem newItem = item.reverseCompleted();
            toDoList.set(position, newItem);

            return true;
        }
        else {
            return false;
        }
    }
}
