package lars.be.contactselectionapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.value;

public class ContactIntentActivity extends AppCompatActivity {

    private static final int MY_PHONE_PERMISSION = 2;
    private final int PHONE = 0;
    private final int WEBSITE = 1;
    private ListView intentListView;
    private ArrayAdapter<String> adapter;
    private List<ContactObject> contactsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_intent);
        intentListView = (ListView) findViewById(R.id.listView1);

        contactsList = new ArrayList<ContactObject>();
        contactsList.add(new ContactObject("Android One", "0468257609", "www.androidATC.com"));
        contactsList.add(new ContactObject("Android Two", "222-2222-2222", "www.androidATC.com"));
        contactsList.add(new ContactObject("Android Three", "333-3333-3333", "www.androidATC.com"));

        List<String> listName = new ArrayList<String>();

        for (int i = 0; i < contactsList.size(); i++) {
            listName.add(contactsList.get(i).getName());
        }

        adapter = new ArrayAdapter<String>(ContactIntentActivity.this, android.R.layout.simple_list_item_1, listName);

        intentListView.setAdapter(adapter);

        intentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ContactIntentActivity.this, ContactPageActivity.class);
                i.putExtra("Object", contactsList.get(position));
                startActivityForResult(i, 0);
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        Bundle resultData = data.getExtras();
        String value = resultData.getString("value");
        switch (resultCode) {
            case PHONE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PHONE_PERMISSION);

                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + value)));
                break;
            case WEBSITE:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + value)));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PHONE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + value)));

                        return;
                    }

                }
                return;
            }

        }}
}


