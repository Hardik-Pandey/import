package in.weclub.srmweclubapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDescription extends AppCompatActivity {

    private Button b;
    private boolean registered;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference("Event");
        DatabaseReference dr = database.getReference("Users");
            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String g = dataSnapshot.child(u.getUid()).child("Registered Events").child(getIntent().
                            getExtras().getString("Event ID"))
                            .getValue(String.class);
                    if (g != null)
                        registered = g.equals("Registered");
                    else
                        registered = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s = getIntent().getExtras().getString("Event ID");
                    DataSnapshot ds = dataSnapshot.child(s);
                    String name = ds.child("Event Name").getValue(String.class);
                    String spk = ds.child("Speaker").getValue(String.class);
                    String url = ds.child("Image").getValue(String.class);
                    String decp = ds.child("Description").getValue(String.class);

                    img = (ImageView) findViewById(R.id.imageView4);
                    Glide.with(EventDescription.this).load(url).into(img);
                    TextView n = findViewById(R.id.eventName1);
                    TextView det = findViewById(R.id.eventDet);
                    TextView des = findViewById(R.id.eventDesc);
                    det.setText(decp);

                    final String i = ds.getKey();

                    n.setText(name);
                    des.setText(String.format("Speaker: %s", spk));

                    b = (Button) findViewById(R.id.button2);
                    if (!registered) {
                        //b.getBackground().clearColorFilter();
                        b.setBackgroundColor(Color.rgb(228, 10, 9));
                        b.setOnClickListener(new View.OnClickListener() {
                            final String f = i;

                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder adb = new AlertDialog.Builder(EventDescription.this
                                        , R.style.AlertDialogTheme);
                                adb.setMessage(Html.fromHtml("<font color='#000000'>Do you want to register in this event?</font>"))
                                        .setPositiveButton("YES",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        DatabaseReference ref1 = database.getReference("Users");
                                                        ref1.child(u.getUid()).child("Registered Events").child(f).setValue("Registered");
                                                        Toast.makeText(EventDescription.this, "Event Registration Successful", Toast.LENGTH_SHORT).show();
                                                        registered = true;
                                                        changeCol();
                                                    }
                                                }).setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).create();
                                adb.setTitle(Html.fromHtml("<font color='#000000'>Confirm Registration</font>"));
                                adb.show();
                            }
                        });

                        //b.getBackground().setColorFilter(Color.rgb(0,130,50), PorterDuff.Mode.MULTIPLY);
                    } else {
                        changeCol();
                        b.setOnClickListener(new View.OnClickListener() {
                            final String f = i;

                            @Override
                            public void onClick(View view) {
                                /*
                                Intent i = new Intent(EventDescription.this, ClaimReward.class);
                                i.putExtra("UID", getIntent().getExtras().getString("Event ID"));
                                i.putExtra("Title", "Confirm Registration");
                                */
                            }
                        });
                        //b.getBackground().setColorFilter(Color.rgb(0,130,50), PorterDuff.Mode.MULTIPLY);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }
    private void changeCol()
    {
        b.setBackgroundColor(Color.rgb(0,100,13));
        b.setText("Registered");
    }

}
