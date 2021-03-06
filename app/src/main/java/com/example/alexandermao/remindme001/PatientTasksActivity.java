package com.example.alexandermao.remindme001;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/* This activity is part of the tablayout

    1. Should contain a list of tasks in chronological order. Tasks should be sorted in priority queue in chronological order already
    2. Should display tasks, finished tasks should be gray, new tasks should be colored. Only display completed tasks within the day. Use java.util.Date.
    3. Clicking the task should take us to TaskViewActivity. Hitting the checkmark by the tasks should set task to completed and turn it gray.
    4. Clicking new task should take us to NewTaskActivity. Current caretaker should be defaulted as the caretaker for the new task.
    5. User should be able to scroll up and down to view all tasks.


 */
public class PatientTasksActivity extends Fragment {

    private GlobalVars globalVars;
    private String user;
    private String currPatient;
    private ArrayList<String> myPatients;
    private View view;
    private ArrayList<JSONObject> tasks;

    private String serverURL = "http://54.67.72.192/";
    private String patientListSuffix = "get_user_patients?user=%s";

    private ListView tasksListView;
    private TaskAdapter taskAdapter;
    private View addButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_patient_tasks, container, false);

        globalVars = GlobalVars.getSingleInstance();
        user = globalVars.getUser();
        currPatient = globalVars.getPatient();

        tasks = new ArrayList<>();
        myPatients = new ArrayList<>();

        this.tasksListView = view.findViewById(R.id.listview);

        String patientListURL = String.format(serverURL + patientListSuffix, user);
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        JsonArrayRequest patientsJSON = new JsonArrayRequest(Request.Method.GET, patientListURL, new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        addPatients(response);
                        taskAdapter = new TaskAdapter();
                        tasksListView.setAdapter(taskAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(patientsJSON);

        addButton = view.findViewById(R.id.add_task);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        this.tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewTask((JSONObject) tasksListView.getItemAtPosition(position));
            }
        });

        return view;
    }

    public void viewTask(JSONObject o) {
        Intent intent = new Intent(view.getContext(), TaskViewActivity.class);
        intent.putExtra("task", o.toString());
        startActivity(intent);

    }
    public void addTask() {
        Intent intent = new Intent(view.getContext(), NewTaskActivity.class);
        startActivity(intent);
    }
    public void refresh() {
        String patientListURL = String.format(serverURL + patientListSuffix, user);
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        JsonArrayRequest patientsJSON = new JsonArrayRequest(Request.Method.GET, patientListURL, new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        addPatients(response);
                        taskAdapter = new TaskAdapter();
                        tasksListView.setAdapter(taskAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(patientsJSON);
    }

    public void addPatients(JSONArray patientsJSON) {
        if (patientsJSON != null) {
            for (int i = 0; i < patientsJSON.length(); i++) {
                try {
                    JSONObject p = (JSONObject) patientsJSON.get(i);
                    System.out.println(p.getJSONObject("profile").getString("name"));
                    System.out.println(currPatient);
                    if (p.getJSONObject("profile").getString("name").equals(currPatient)) {
                        JSONArray pn = p.getJSONArray("tasks");
                        for (int j = 0; j < pn.length(); j++) {
                            tasks.add((JSONObject)pn.get(j));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return;

    }


    public class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Object getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.patient_list_item, null);
            TextView pname = (TextView) convertView.findViewById(R.id.patientname);
            try {
                pname.setText(tasks.get(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
