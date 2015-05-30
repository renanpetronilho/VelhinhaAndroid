package br.com.luthien.jogodavelha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Splash extends Activity {

    private boolean ativo = true;
    private static final int TEMPO_SPLASH = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);

        // thread para mostrar a SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    aguardaInicializacao();
                } catch (InterruptedException e) {

                } finally {
                    finish();
                    startActivity(new Intent("br.com.luthien.jogodavelha.MainActivity"));
                    stop();
                }
            }

            private void aguardaInicializacao() throws InterruptedException {
                int waited = 0;
                while (ativo && waited < Splash.TEMPO_SPLASH) {
                    sleep(100);
                    if (ativo)
                        waited += 100;
                }
            }
        };

        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            ativo = false;

        return true;
    }
}
