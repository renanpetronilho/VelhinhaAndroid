package br.com.luthien.jogodavelha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Sobre extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tv = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sobre);
        tv = (TextView) findViewById(R.id.txtAuthor);
        tv.setSelected(true);

    }

}