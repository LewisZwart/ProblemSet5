package lewis.problemset5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * This activity shows one of the user's TO DO lists, and allows him/her to add and remove items.
 * Each task can be marked with a checksign to indicate whether it is completed.
 */

public class ShowListActivity extends AppCompatActivity {

    ToDoManager toDoManager;
    RowAdapter adapter;
    ListView itemsListView;
    Button addItemButton;
    EditText newItemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        // assign variables to layout elements
        TextView listTitleTextView = (TextView) findViewById(R.id.listTitleView);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        newItemEditText = (EditText) findViewById(R.id.newItemEditText);
        itemsListView = (ListView) findViewById(R.id.itemsListView);

        // get overall
        toDoManager = ToDoManager.getInstance();
        toDoManager.loadData(this);
        String nameToDoList = toDoManager.getCurrentListName();

        // show name of TO DO list
        listTitleTextView.setText(nameToDoList);

        // show items of TO DO list
        adapter = new RowAdapter(this, toDoManager.getCurrentListItems());
        itemsListView.setAdapter(adapter);

        setListeners();
    }

    /*
     * Sets the click listeners on the listview elements and the add button.
     */
    public void setListeners() {

        // when the add button is clicked, create new TO DO item with name given in the textfield
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = newItemEditText.getText().toString();

                // if nonempty name is given, add a TO DO list with that name
                if (!itemName.equals("")) {

                    // add list to state
                    toDoManager.addItem(getApplicationContext(), itemName);
                    //ToDoItem newItem = new ToDoItem(itemName);
                    //toDoList.addItem(newItem);

                    //toDoManager.replaceCurrentList(getApplicationContext(), toDoList);

                    // adjust information on screen
                    newItemEditText.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // when a TO DO list is long-pressed, remove it from list after confirmation of user
        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String item = toDoManager.getItemName(position);

                // create alert dialog to get confirmation from user
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowListActivity.this);
                builder.setMessage(getString(R.string.sureRemove) + item + getString(R.string.fromToDoList));

                // if yes is clicked, remove item from list
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // adjust state and remove from screen
                        toDoManager.removeItem(getApplicationContext(), position);
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

    public void onBackPressed() {

        // set new position to main
        toDoManager.setCurrentPosition(this, -1);

        // reload main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
