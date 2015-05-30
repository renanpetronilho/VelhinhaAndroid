/**
 * Classe responsável por manipula toda  a lógida do jogo
 * @author renan
 * @versio 1.0
 */
package br.com.luthien.jogodavelha;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Jogo extends Activity implements OnClickListener {

    public static final int VS = 0;
    public static final int FACIL = 1;
    public static final int MEDIO = 2;
    public static final int DIFICIL = 3;
    private int nivelDificuldade = -1;

    TextView tvJogador1 = null;
    TextView tvJogador2 = null;
    TextView tvTextJogador1 = null;
    TextView tvTextJogador2 = null;

    private int quantasJogadas = 0;
    private String letra = "";

    private Button[] tabuleiro;
    private Integer vitoriasJogador = 0;
    private Integer vitoriasJogador2 = 0;

    private static int[][] combinacoesVitoriosas = new int[][] {
            { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, // linha vence
            { 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 }, // coluna vence
            { 1, 5, 9 }, { 3, 5, 7 } // diagonal vence
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_tabuleiro);

        Intent intent = getIntent();
        nivelDificuldade = intent.getIntExtra("level", FACIL);

        iniciaTabuleiro();

        // Views para o controle do placar
        tvJogador1 = (TextView) findViewById(R.id.txtPlayer1);
        tvJogador2 = (TextView) findViewById(R.id.txtPlayer2);

        // Views para os nomes dos labels
        tvTextJogador1 = (TextView) findViewById(R.id.labelPlayer1);
        tvTextJogador2 = (TextView) findViewById(R.id.labelPlayer2);

        if (nivelDificuldade == VS)
        {
            tvTextJogador1.setText("Jogador 1");
            tvTextJogador2.setText("Jogador 2");
        }
        else
        {
            tvTextJogador1.setText("Você");
            tvTextJogador2.setText("Android");
        }

        tvJogador1.setText("0");
        tvJogador2.setText("0");

        //Toast.makeText(this, "onCreate", 1).show();
    }

    /**
     * Inicializa o array de bot�es e seta evento onClickListener para cada
     * campo do tabuleiro.
     */
    private void iniciaTabuleiro() {


        tabuleiro = new Button[10];
        for (int i = 1; i < 10; i++)
            tabuleiro[i] = new Button(this);

        tabuleiro[1] = (Button) findViewById(R.id.btM1);
        tabuleiro[2] = (Button) findViewById(R.id.btM2);
        tabuleiro[3] = (Button) findViewById(R.id.btM3);
        tabuleiro[4] = (Button) findViewById(R.id.btM4);
        tabuleiro[5] = (Button) findViewById(R.id.btM5);
        tabuleiro[6] = (Button) findViewById(R.id.btM6);
        tabuleiro[7] = (Button) findViewById(R.id.btM7);
        tabuleiro[8] = (Button) findViewById(R.id.btM8);
        tabuleiro[9] = (Button) findViewById(R.id.btM9);

        for (int i = 1; i < 10; i++)
            tabuleiro[i].setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Indica que o cara jogou
     * @param v View - Item clicado
     */
    @Override
    public void onClick(View v) {

        Button btn = encontraCasaPressionada(v);

        if (nivelDificuldade == VS)
        {
            if (quantasJogadas % 2 == 0)
                jogadaPlayer1(btn);
            else
                jogadaPlayer2(btn);

            haVencedor();
        }
        else
        {
            jogadaUsuario(btn);

            if (!haVencedor())
                verificaJogadaAndroid();
        }
    }

    private Button encontraCasaPressionada(View v) {
        Button btn = null;

        switch (v.getId()) {
            case R.id.btM1:
                btn = (Button) findViewById(R.id.btM1);
                break;
            case R.id.btM2:
                btn = (Button) findViewById(R.id.btM2);
                break;
            case R.id.btM3:
                btn = (Button) findViewById(R.id.btM3);
                break;
            case R.id.btM4:
                btn = (Button) findViewById(R.id.btM4);
                break;
            case R.id.btM5:
                btn = (Button) findViewById(R.id.btM5);
                break;
            case R.id.btM6:
                btn = (Button) findViewById(R.id.btM6);
                break;
            case R.id.btM7:
                btn = (Button) findViewById(R.id.btM7);
                break;
            case R.id.btM8:
                btn = (Button) findViewById(R.id.btM8);
                break;
            case R.id.btM9:
                btn = (Button) findViewById(R.id.btM9);
                break;
        }
        return btn;
    }

    private void jogadaPlayer1(Button btn) {
        btn.setText("X");
        btn.setTextColor(Color.GREEN);
        btn.setEnabled(false);
        quantasJogadas++;
    }

    private void jogadaPlayer2(Button btn) {
        btn.setText("O");
        btn.setTextColor(Color.BLUE);
        btn.setEnabled(false);
        quantasJogadas++;
    }

    private void jogadaUsuario(Button btn) {
        btn.setText("X");
        btn.setTextColor(Color.GREEN);
        btn.setEnabled(false);
        quantasJogadas++;
    }

    public void verificaJogadaAndroid() {

        quantasJogadas++;

        //No nivel f�cil, com menos de 4 jogadas, movimento rand�mico
        if (nivelDificuldade == FACIL && quantasJogadas < 4) {
            jogadaAleatoria();
            return;
        }

        //No n�vel m�dio, s� joga randomicamente na primeira vez
        if (nivelDificuldade == MEDIO && quantasJogadas == 2) {
            jogadaAleatoria();
            return;
        }

        if (!jogadaParaVencer())
            if (!jogadaDefensiva())
                jogadaAleatoria();

        // Verifica se h� vencedor
        haVencedor();
    }

    //TODO  explicar melhor essa parada
    public boolean jogadaParaVencer() {

        boolean jogou = false;

        // Jogada para vencer
        // Verifica se possui 2 casas marcadas nas combina��es que vencem marca a terceira

        for (int i = 0; i <= 7; i++) {

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("O") &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText().equals("")) {

                jogadaAndroid(tabuleiro[Jogo.combinacoesVitoriosas[i][2]]);
                jogou = true;
                break;
            }

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("O") &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals("")) {

                jogadaAndroid(tabuleiro[Jogo.combinacoesVitoriosas[i][1]]);
                jogou = true;
                break;
            }

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals("O") &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("")) {

                jogadaAndroid(tabuleiro[Jogo.combinacoesVitoriosas[i][0]]);
                jogou = true;
                break;
            }
        }

        return jogou;
    }

    private boolean jogadaDefensiva() {

        boolean jogou = false;

        // Jogada defensiva
        // Verifica se possui 2 casas marcadas nas combina��es que vencem, e
        // marca a terceira para anular a jogada

        for (int i = 0; i <= 7; i++) {

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("X") &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText().equals("")) {

                jogadaAndroid(tabuleiro[Jogo.combinacoesVitoriosas[i][2]]);
                jogou = true;
                break;
            }

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("X") &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals("")) {

                jogadaAndroid(tabuleiro[Jogo.combinacoesVitoriosas[i][1]]);
                jogou = true;
                break;
            }

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals("X") &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("")) {

                jogadaAndroid(tabuleiro[Jogo.combinacoesVitoriosas[i][0]]);
                jogou = true;
                break;
            }
        }

        return jogou;
    }

    private void jogadaAndroid(Button casa) {
        casa.setTextColor(Color.BLUE);
        casa.setText("O");
        casa.setEnabled(false);
    }

    private void jogadaAleatoria() {

        Random x = new Random();
        int y = 1 + x.nextInt(9);

        if (y > 9)
            jogadaAleatoria();
        else {

            if (tabuleiro[y].getText().length() > 0)
                jogadaAleatoria();
            else
                jogadaAndroid(tabuleiro[y]);
        }
    }

    public boolean haVencedor() {

        boolean haVencedor = false;

        // Verifica se houve vencedor
        for (int i = 0; i <= 7 && !haVencedor; i++) {

            if (tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals(
                    tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText()) &&
                    tabuleiro[Jogo.combinacoesVitoriosas[i][1]].getText().equals(
                            tabuleiro[Jogo.combinacoesVitoriosas[i][2]].getText()) &&
                    !tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("")) {

                haVencedor = true;

                if (tabuleiro[Jogo.combinacoesVitoriosas[i][0]].getText().equals("X"))
                {
                    if (nivelDificuldade == VS)
                        letra = "Jogador 1";
                    else
                        letra = "Você";

                    vitoriasJogador++;
                }
                else
                {
                    if (nivelDificuldade == VS)
                    {
                        letra = "Jogador 2";
                    }
                    else
                    {
                        letra = "Android";

                        SharedPreferences prefs =
                                PreferenceManager.getDefaultSharedPreferences(this);
                    }

                    vitoriasJogador2++;
                }
            }
        }

        mensagemResultado(haVencedor);

        return haVencedor;
    }

    private void mensagemResultado(boolean haVencedor) {

        if (haVencedor) {
            new AlertDialog.Builder(Jogo.this).setTitle("Jogo da Velha!").
                    setMessage(letra + " venceu!").
                    setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    limpaJogo();
                                }
                            }).show();

        } else if (quantasJogadas >= 9 && !haVencedor) {
            new AlertDialog.Builder(Jogo.this).setTitle("Jogo da Velha!").
                    setMessage("Empate!").
                    setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) { }
                            }).show();

            limpaJogo();
        }
    }

    /**
     * Inicializa as vari�veis do jogo
     */
    public void limpaJogo() {

        // Reseta status dos bot�es
        for (int i = 1; i < 10; i++) {
            tabuleiro[i].setText("");
            tabuleiro[i].setEnabled(true);
        }

        // Reseta vari�veis
        quantasJogadas = 0;

        if (nivelDificuldade == VS)
        {
            tvJogador1.setText(vitoriasJogador.toString());
            tvJogador2.setText(vitoriasJogador2.toString());
        }
        else
        {
            tvJogador1.setText(vitoriasJogador.toString());
            tvJogador2.setText(vitoriasJogador2.toString());
        }
    }

}