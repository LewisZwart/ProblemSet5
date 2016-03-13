package lewis.problemset5;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * A ToDoManager keeps track of all TO DO lists of the user, and saves which list was view last.
 */
public class ToDoManager {

    // fields of a ToDoManager
    ArrayList<ToDoList> listOfLists;
    int currentPosition;
    private static ToDoManager ourInstance = new ToDoManager();

    /*
     * Returns the current instance of ToDoManager.
     */
    public static ToDoManager getInstance() {
        return ourInstance;
    }

    /*
     * Returns a default ToDoManager.
     */
    private ToDoManager() {
        listOfLists = new ArrayList<>();
        currentPosition = -1;
    }

    /*
     * Returns the list of all TO DO lists.
     */
    public ArrayList<ToDoList> getToDoLists() {
        return listOfLists;
    }

    /*
     * Returns the TO DO list at the given position.
     * Only to be called from ShowListActivity with valid position.
     */
    public String getListName(int position) {
        return listOfLists.get(position).toString();
    }

    /*
    * Returns the list that is currently viewed.
    * Only to be called from ShowListActivity.
    */
    private ToDoList getCurrentList() {
            return listOfLists.get(currentPosition);
    }

    /*
     * Returns the name of the currently viewed list.
    */
    public String getCurrentListName() {
        return getCurrentList().toString();
    }

    /*
    * Returns the items of the currently viewed list.
    */
    public ArrayList<ToDoItem> getCurrentListItems() {
        return getCurrentList().getItems();
    }

    /*
     * Returns the integer representing the currently viewed list (-1 for the list of lists).
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /*
     * Sets and saves the position of the list that will be shown.
     */
    public boolean setCurrentPosition(Context context, int position) {

        // if position is valid, set position and save
        if (position >= -1 && position < listOfLists.size()) {
            currentPosition = position;
            savePosition(context);
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * Adds and saves an empty TO DO list with the given name to the list of TO DO lists.
     */
    public void addList(Context context, String listName) {

        // create empty list with given name
        ToDoList newList = new ToDoList(listName);

        // add and save
        listOfLists.add(newList);
        saveLists(context);
    }

    /*
     * Removes the list at the given position from the list of TO DO lists.
     */
    public boolean removeList(Context context, int position) {

        //if position is valid, remove list and save changes
        if (position >= 0 && position < listOfLists.size()) {
            listOfLists.remove(position);
            saveLists(context);
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * Returns the item at the given position of the current list.
     * Only to be called from ShowListActivity with valid position.
     */
    public String getItemName(int position) {
        ToDoItem item = getCurrentList().getItem(position);
        return item.toString();
    }

    /*
     * Adds and saves an item with the given name to the current TO DO list.
     */
    public void addItem(Context context, String itemName) {

        // construct list with item added
        ToDoList newToDoList = listOfLists.get(currentPosition);
        newToDoList.addItem(itemName);

        // adjust and save the list in the toDoManager
        listOfLists.set(currentPosition, newToDoList);
        saveLists(context);
    }

    /*
     * Removes the item at the given position from the currently viewed list.
     */
    public boolean removeItem(Context context, int position) {

        // if position is valid, remove item and save changes
        if (position >= 0 && position < listOfLists.size()) {
            getCurrentList().removeItem(position);
            saveLists(context);
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * Returns a boolean, indicating whether the task at the given position is completed.
     */
    public boolean getItemCompleted(int position) {
        ToDoItem item = getCurrentList().getItem(position);
        return item.isCompleted();
    }

    /*
     * Reverses and saves completedness of the item at the given position of currently viewed list.
     */
    public void reverseItemCompleted(Context context, int position) {
        getCurrentList().reverseCompleted(position);
        saveLists(context);
    }

    /*
     * Loads previous list of TO DO lists, and the position of the list that was viewed when exiting.
     */
    public void loadData(Context context) {
        try{
            // open file
            FileInputStream inputStreamLists = context.openFileInput("toDoLists.data");
            ObjectInputStream objectStreamLists = new ObjectInputStream(inputStreamLists);

            // get TO DO lists
            listOfLists = (ArrayList<ToDoList>) objectStreamLists.readObject();

            // close file
            objectStreamLists.close();
            inputStreamLists.close();
        }

        // on first usage, file does not exist, so don't show error to user if file not found
        catch (ClassNotFoundException e) {
            listOfLists = new ArrayList<>();
            e.printStackTrace();
        }
        catch (IOException e) {
            listOfLists = new ArrayList<>();
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("Preferences",
                Context.MODE_PRIVATE);
        currentPosition = sharedPreferences.getInt("position", -1);
    }

    /*
     * Saves the TO DO lists in the file toDoLists.data.
     */
    private void saveLists(Context context) {
        try {
            // open output stream
            FileOutputStream outputStream = context.openFileOutput
                    ("toDoLists.data", Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);

            // save state
            objectStream.writeObject(listOfLists);

            // close output stream
            objectStream.close();
            outputStream.close();
        }

        // if file not found, show error toast
        catch (IOException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, R.string.writeError, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*
     * Saves position indicating currently viewed list in shared preferences.
     */
    private void savePosition(Context context) {

        // get shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // add position
        editor.putInt("position", currentPosition);
        editor.commit();
    }
}
