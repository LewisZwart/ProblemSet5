package lewis.problemset5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Implements an adapter showing the names of the items in the given list, each with a checkbox
 * to indicate whether the task is completed.
 */
public class RowAdapter extends ArrayAdapter<ToDoItem> {

    // global variables
    Context adapterContext;
    ToDoManager toDoManager;

    public RowAdapter(Context context, ArrayList<ToDoItem> list) {
        super(context, R.layout.layout_single_row_checkboxes, R.id.itemTextView, list);

        adapterContext = context;
        toDoManager = ToDoManager.getInstance();
    }

    /*
     * Shows items of current TO DO list in listview, each with a checkbox at the end of the line.
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) adapterContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_single_row_checkboxes, parent, false);
        }

        // place items of TO DO list in textview
        TextView itemTextView = (TextView) view.findViewById(R.id.itemTextView);
        final String item = toDoManager.getItemName(position);
        itemTextView.setText(item);

        // create checkbox to indicate completedness of tasks
        CheckBox completedCheckBox = (CheckBox) view.findViewById(R.id.completedCheckBox);

        // set checkmark in checkbox if task is already completed
        boolean isCompleted = toDoManager.getItemCompleted(position);
        completedCheckBox.setChecked(isCompleted);

        // let the checking of the checkbox correspond to whether it taks is completed or not
        completedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDoManager.reverseItemCompleted(adapterContext, position);
            }
        });

        return view;
    }
}

