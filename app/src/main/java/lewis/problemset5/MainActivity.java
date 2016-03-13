package lewis.problemset5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/*
 * Lewis Zwart
 * 10251057
 *
 * Shows an overview of the current TO DO lists. Lists can be added or removed, and by clicking
 * a list, the user is directed to the activity ShowList where the specific list is shown.
 */
public class MainActivity extends AppCompatActivity {

    // define global variables
    ToDoManager toDoManager;
    Button addListButton;
    EditText newListEditText;
    ListView toDoListsView;
    ArrayAdapter adapter;

    /*
     * Restores the previous app state if there was one, and shows a list of current TO DO lists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link layout elements to variables
        toDoListsView = (ListView) findViewById(R.id.listsListView);
        addListButton = (Button) findViewById(R.id.addToDoListButton);
        newListEditText = (EditText) findViewById(R.id.newToDoEditText);

        // load previous app state
        toDoManager = ToDoManager.getInstance();
        toDoManager.loadData(this);

        // if application was left while viewing a specific list, open activity to show it
        if (toDoManager.getCurrentPosition() != -1) {
            Intent intent = new Intent(this, ShowListActivity.class);
            startActivity(intent);
            finish();
        }

        // otherwise, show list of all TO DO lists
        adapter = new ArrayAdapter(this, R.layout.layout_single_row, R.id.ListItemTextView,
                toDoManager.getToDoLists());
        toDoListsView.setAdapter(adapter);

        setListeners();
    }

    /*
     * Sets the click listeners on the listview elements and the add button.
     */
    public void setListeners() {

        // when the add button is clicked, create new TO DO list with the name given in the textfield
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = newListEditText.getText().toString();

                // if nonempty name is given, add a TO DO list with that name
                if (!listName.equals("")) {

                    // add list to manager and adjust information on screen
                    toDoManager.addList(getApplicationContext(), listName);
                    newListEditText.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // when a TO DO list is clicked, go to that list in the ShowList activity
        toDoListsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // save new position of viewed list
                toDoManager.setCurrentPosition(getApplicationContext(), position);

                // show list in ShowList activity
                Intent intent = new Intent(getApplicationContext(), ShowListActivity.class);
                startActivity(intent);
            }
        });



        // when a TO DO list is long-pressed, remove it from list after confirmation of user
        toDoListsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String item = toDoManager.getListName(position);

                // create alert dialog to get confirmation from user
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.areYouSure) + item + getString(R.string.fromToDo));

                // if yes is clicked, remove item from list
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // remove list from list of TO DO lists and from screen
                        toDoManager.removeList(getApplicationContext(), position);
                        adapter.notifyDataSetChanged();
                    }
                });

                // if no is clicked, do nothing
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int it) {
                    }
                });

                // show alert dialog
                AlertDialog confirmDelete = builder.create();
                confirmDelete.show();

                return true;
            }
        });
    }

    /*
     * Exits application if back button is pressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
