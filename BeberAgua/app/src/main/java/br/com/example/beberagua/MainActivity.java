package br.com.example.beberagua;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btnNotify;
    private EditText editMinutos;
    private TimePicker timePicker;
    private Button btnLimpar;

    private Boolean ativado;

    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_name);

        btnNotify = findViewById(R.id.btn_notificar);
        editMinutos = findViewById(R.id.et_numero);
        timePicker = findViewById(R.id.time_picker);

        timePicker.setIs24HourView(true);

        preferencias = getSharedPreferences("db", Context.MODE_PRIVATE);

        // Ao executar pela primeira vez o app, definir a variavel "ativado" como falso porque o "DB" não foi criado ainda
        // Se estiver ativado e o usuário fechar o app, quando voltar resgata os dados do db
        ativado = preferencias.getBoolean("ativado", false);

        setInterface(ativado, preferencias);

        btnNotify.setOnClickListener(notificarListener);
    }

    private void setInterface(boolean ativado, SharedPreferences prefs) {
        if (ativado) {
            btnNotify.setText(R.string.pausar);
            btnNotify.setBackgroundResource(R.drawable.bg_button_pausar);
            int intervalo = prefs.getInt("intervalo", 0);
            int hora = prefs.getInt("hora", timePicker.getCurrentHour());
            int minuto = prefs.getInt("minuto", timePicker.getCurrentMinute());

            editMinutos.setText(String.valueOf(intervalo));
            timePicker.setCurrentHour(hora);
            timePicker.setCurrentMinute(minuto);
        } else {
            btnNotify.setText(R.string.notificar);
            btnNotify.setBackgroundResource(R.drawable.bg_button_notificar);
            Calendar calendar = Calendar.getInstance();
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY)); // 24 hrs
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }
    }

    private void alerta(int idAlerta) {
        Toast.makeText(MainActivity.this, idAlerta, Toast.LENGTH_SHORT).show();
    }

    private boolean intervaloValido() {
        String sIntervalo = editMinutos.getText().toString();

        if (sIntervalo.isEmpty()) {
            alerta(R.string.msg_vazio);
            return false;
        }

        if (sIntervalo.equalsIgnoreCase("0")) {
            alerta(R.string.msg_zero);
            return false;
        }
        return true;
    }

    private void atualizarPreferencias(boolean adicionando, int intervalo, int hora, int minuto) {
        SharedPreferences.Editor editor = preferencias.edit();
        // Salva as preferências num db nativo (SharedPreferences)
        if (adicionando) {
            editor.putBoolean("ativado", true);
            editor.putInt("intervalo", intervalo);
            editor.putInt("hora", hora);
            editor.putInt("minuto", minuto);
        // Remove as preferências salvas no db nativo
        } else {
            editor.putBoolean("ativado", false);
            editor.remove("intervalo");
            editor.remove("hora");
            editor.remove("minuto");
        }
        // Aplica as transformações
        editor.apply();
    }

    private void setNotificacao(boolean adicionado, int i, int h, int m) {
        // A activity atual chama a classe NotificationPublisher responsável por mostrar a notificação
        Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);
        // Ativa a notificação e o alarme
        if (adicionado) {
            // Passa os parâmetros para a notificação!
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION, "Hora de beber água!");
            // Ao tocar na notificação vai reabrir a MainActivity
            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), i * 60 * 1000, broadcast);
       // Destiva a notificação e o alarme
        } else {
            //notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);

            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, 0);

            //alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(broadcast);
        }
    }

    private View.OnClickListener notificarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!ativado) {
                // Valida o intervalo de tempo informado pelo usuário, se for falso elimina de cara
                if (!intervaloValido()) return;

                int intervalo = Integer.parseInt(String.valueOf(editMinutos.getText()));

                int hora = timePicker.getCurrentHour();

                int minuto = timePicker.getCurrentMinute();

                atualizarPreferencias(true, intervalo, hora, minuto);

                setInterface(true, preferencias);

                setNotificacao(true, intervalo, hora, minuto);

                alerta(R.string.alarme_ativado);

                ativado = true;
            } else {
                atualizarPreferencias(false, 0, 0, 0);

                setInterface(false, preferencias);

                setNotificacao(false, 0, 0, 0);

                alerta(R.string.alarme_desativado);

                ativado = false;
            }
            //Log.d("teste","hora:" + hora + " minuto: " + minuto + " intervalo " + intervalo + "ativado: "+ ativado);
        }
    };
}