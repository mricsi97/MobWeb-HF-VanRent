package hu.bme.aut.vanrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ListActivity.theme);
        setContentView(R.layout.activity_details);

        final EditText etCustomerName = findViewById(R.id.etCustomerName);
        final EditText etCustomerCity = findViewById(R.id.etCustomerCity);
        final EditText etCustomerAddress = findViewById(R.id.etCustomerAddress);
        final EditText etCustomerEmail = findViewById(R.id.etCustomerEmail);
        final EditText etCustomerPhone = findViewById(R.id.etCustomerPhone);

        final Button btnReserve = findViewById(R.id.btnReserve);
        final RadioGroup rgPaymentMethod = findViewById(R.id.rgPaymentMethod);

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCustomerName.getText().toString().isEmpty()) {
                    etCustomerName.requestFocus();
                    etCustomerName.setError(getString(R.string.enter_name));
                    return;
                }

                if (etCustomerCity.getText().toString().isEmpty()) {
                    etCustomerCity.requestFocus();
                    etCustomerCity.setError(getString(R.string.enter_city));
                    return;
                }

                if (etCustomerAddress.getText().toString().isEmpty()) {
                    etCustomerAddress.requestFocus();
                    etCustomerAddress.setError(getString(R.string.enter_address));
                    return;
                }

                if (etCustomerEmail.getText().toString().isEmpty()) {
                    etCustomerEmail.requestFocus();
                    etCustomerEmail.setError(getString(R.string.enter_email));
                    return;
                }

                if (etCustomerPhone.getText().toString().isEmpty()) {
                    etCustomerPhone.requestFocus();
                    etCustomerPhone.setError(getString(R.string.enter_phone));
                    return;
                }

                int checkedID = rgPaymentMethod.getCheckedRadioButtonId();
                switch (checkedID){
                    case -1: {
                        Toast.makeText(DetailsActivity.this, R.string.choose_payment, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case R.id.rbCashPayment: {
                        Intent intent = new Intent(DetailsActivity.this, ResultActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.rbCreditCardPayment: {
                        Toast.makeText(DetailsActivity.this, R.string.card_payment_unavailable, Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        });

    }
}
