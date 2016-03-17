package babylon.babylonapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import babylon.babylonapp.DAO.UserDAO;
import babylon.babylonapp.Model.User;
import babylon.babylonapp.R;
import babylon.babylonapp.Request.Requests;

/**
 * Created by manuel on 14/3/16.
 */
public class UserProfileActivity extends FragmentActivity implements OnMapReadyCallback {

    private int userId = -1;
    private ProgressDialog pDialog;
    private GoogleMap googleMap;
    private GoogleApiClient client;

    private TextView tvUsernameUser, tvNameUser, tvEmailUser, tvPhoneUser, tvWebsiteUser,
            tvCompanyNameWindow, tvCompanyCatchPhraseWindow, tvCompanyBsWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_fragment);

        tvUsernameUser = (TextView)findViewById(R.id.tvUserNameUser);
        tvNameUser = (TextView)findViewById(R.id.tvNameUser);
        tvEmailUser = (TextView)findViewById(R.id.tvEmailUser);
        tvPhoneUser = (TextView)findViewById(R.id.tvPhoneUser);
        tvWebsiteUser = (TextView)findViewById(R.id.tvWebsiteUser);
        tvCompanyNameWindow = (TextView) findViewById(R.id.tvCompanyNameUser);
        tvCompanyCatchPhraseWindow = (TextView) findViewById(R.id.tvCompanyCatchPhraseUser);
        tvCompanyBsWindow = (TextView) findViewById(R.id.tvCompanyBsUser);

        // prepare the progressDialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        // get userId from the previous activity
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            userId = intent.getIntExtra("userId", -1);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // get the user information
        if (userId != -1) {
            pDialog.show();
            UserDAO.getUserById(this, userId, "userProfileGetUserById", new Requests.MyRequestCallback() {
                @Override
                public void onResponse(Object objectResponse) {
                    pDialog.dismiss();
                    final User user  = (User) objectResponse;
                    showUser(user);
                }

                @Override
                public void onError(String error) {
                    pDialog.dismiss();
                    finish();
                    /* do something */
                }
            });
        }
    }


    private void showUser(final User user){
        // print values
        tvUsernameUser.setText(user.getUsername());
        tvNameUser.setText(user.getName());
        tvEmailUser.setText(user.getEmail());
        tvPhoneUser.setText(user.getPhone());
        tvWebsiteUser.setText(user.getWebsite());
        tvCompanyNameWindow.setText(user.getCompany().getName());
        tvCompanyCatchPhraseWindow.setText("\""+user.getCompany().getCatchPhrase()+"\"");
        tvCompanyBsWindow.setText(user.getCompany().getBs());

        /*  MAP */
        // Add marker
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng())));

        // Move camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng())));

        // create a new infoWindow with the address
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.custom_window, null);
                TextView tvTitleUserWindow = (TextView) view.findViewById(R.id.tvUserNameInfoWindow);
                TextView tvSuiteWindow = (TextView) view.findViewById(R.id.tvSuiteInfoWindow);
                TextView tvStreetWindow = (TextView) view.findViewById(R.id.tvStreetInfoWindow);
                TextView tvCityWindow = (TextView) view.findViewById(R.id.tvCityInfoWindow);
                TextView tvZipcodeWindow = (TextView) view.findViewById(R.id.tvZipcodeInfoWindow);

                tvTitleUserWindow.setText(user.getUsername());
                tvSuiteWindow.setText(user.getAddress().getSuite());
                tvStreetWindow.setText(user.getAddress().getStreet());
                tvCityWindow.setText(user.getAddress().getCity());
                tvZipcodeWindow.setText(user.getAddress().getZipcode());
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }
}
