package br.com.example.beberagua;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationPublisher extends BroadcastReceiver {

    public static final String KEY_NOTIFICATION = "key_notification";
    public static final String KEY_NOTIFICATION_ID = "key_notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Salva uma View (MainActivity) que será reaberta quando o usuário tocar na notificação
        Intent it = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, it, 0);

        // getSystemService Pega um serviço do android, no caso aqui um serviço de notificação
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Parâmentros vindos da MainActivity
        String message = intent.getStringExtra(KEY_NOTIFICATION);
        int id = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);

        Notification notification = getNotification(message, context, notificationManager, pIntent);
        //NotificationManager realiza a notificação, necessita de um ID e de um objeto de notificação
        notificationManager.notify(id, notification);
    }
    // Método que constrói a notificação
    private Notification getNotification(String conteudo, Context context, NotificationManager manager, PendingIntent intent) {

        Notification.Builder builder =
                new Notification.Builder(context.getApplicationContext())
                        .setContentText(conteudo) // Mensagem
                        .setContentIntent(intent) // Intent para abrir a MainActivity
                        .setTicker("Alerta") // Título
                        .setAutoCancel(false) // Evita cancelar se não pressionar
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setSmallIcon(R.mipmap.ic_launcher); // Ícone
        // Para a Notification funcionar, principalmente nos dispositivos mais recentes, é necessário criar um Channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel =
                    new NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        return builder.build();
    }
}
