package com.mibodega.mystore.shared;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.mibodega.mystore.R;
import com.mibodega.mystore.views.signIn.SignInActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    private static final String MY_CHANNEL_ID = "piddeChannel";
    private PendingIntent pendingIntent;

    private Context contexT;
    private Resources resourceS;
    private View vieW;
    public static ProgressDialog progressDialog;

    public String encryptSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String convertDateToClearFormat(String date) {
        String createdAtString = date;

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Asegura que el formato de entrada esté en UTC
        Date createdAtDate = null;
        try {
            createdAtDate = isoFormat.parse(createdAtString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        SimpleDateFormat desiredFormat = new SimpleDateFormat("EEEE d MMM h:mm a", new Locale("es", "ES"));
        desiredFormat.setTimeZone(TimeZone.getTimeZone("America/Lima")); // Configura la zona horaria a Lima, Perú
        return desiredFormat.format(createdAtDate);
    }
    public Dialog getAlertCustom(Context context, String type, String title, String message, boolean hasButtons) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_custom_alert);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroun_custom_rectangle);
        }
        Display display;
        Point size = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = context.getDisplay();
            display.getSize(size);
        } else {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                display = windowManager.getDefaultDisplay();
                display.getSize(size);
            }
        }
        int screenWidth = size.x;
        int dialogWidth = (int) (screenWidth * 0.9);
        int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        ImageView img_icon = dialog.findViewById(R.id.Imgv_icon_dialog);
        ImageButton btn_close = dialog.findViewById(R.id.Imgb_custom_closeDialog);
        TextView tv_title = dialog.findViewById(R.id.Tv_accion_custom_dialog);
        TextView tv_message = dialog.findViewById(R.id.Tv_message_custom_dialog);
        switch (type) {
            case "success": {
                tv_title.setText(title);
                tv_message.setText(message);
                img_icon.setImageResource(R.drawable.baseline_check_24);
                img_icon.setColorFilter(ContextCompat.getColor(context, R.color.toast_success), PorterDuff.Mode.MULTIPLY);
                break;
            }
            case "danger": {
                tv_title.setText(title);
                tv_message.setText(message);
                img_icon.setImageResource(R.drawable.baseline_error_24);
                img_icon.setColorFilter(ContextCompat.getColor(context, R.color.toast_error), PorterDuff.Mode.MULTIPLY);

                break;
            }
            case "warning": {
                tv_title.setText(title);
                tv_message.setText(message);
                img_icon.setImageResource(R.drawable.baseline_warning_24);
                img_icon.setColorFilter(ContextCompat.getColor(context, R.color.toast_alert), PorterDuff.Mode.MULTIPLY);
                break;
            }
        }

        Button btn_accept = dialog.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (hasButtons) {
            btn_cancel.setVisibility(View.VISIBLE);
            btn_accept.setVisibility(View.VISIBLE);
            btn_close.setVisibility(View.GONE);
            dialog.setCancelable(false);
        } else {
            btn_cancel.setVisibility(View.GONE);
            btn_accept.setVisibility(View.GONE);
            btn_close.setVisibility(View.VISIBLE);
            dialog.setCancelable(true);
        }
        return dialog;
    }
    public void getAlertDialog(Context context, String title, String message, String color) {
        Resources resources = context.getResources();
        String colorTitle = "";
        if (color.equals("verde")) {
            colorTitle = Integer.toString(resources.getColor(R.color.toast_success));
        } else if (color.equals("rojo")) {
            colorTitle = Integer.toString(resources.getColor(R.color.toast_error));
        } else if (color.equals("amarillo")) {
            colorTitle = Integer.toString(resources.getColor(R.color.toast_alert));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(Html.fromHtml("<font color='" + colorTitle + "'>" + title + "</font>"));
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    //notificaction section functions
    public void getNotificacionPush(Activity activity, String message, String title, RemoteViews view) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            System.out.println("No tiene permisos");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotification(activity, message, title, view);
        } else {
            showNewNotification(activity, message, title, view);
        }
    }
    private void showNotification(Activity activity, String message, String title, RemoteViews view) {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    MY_CHANNEL_ID,
                    "NotificacionesPees",
                    NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        showNewNotification(activity, message, title, view);
    }
    private void showNewNotification(Activity activity, String message, String title, RemoteViews view) {
        setPedingIntent(activity);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getApplicationContext(), MY_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_check_24)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT);

        if (view != null) {
            builder.setCustomContentView(view);
            System.out.println("entra a vista de notificacion");
        } else {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }


        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationManagerCompat.from(activity.getApplicationContext()).notify(1, builder.build());
    }
    private void setPedingIntent(Activity activity) {
        Class<? extends SignInActivity> classe = new SignInActivity().getClass();

        Intent intent = new Intent(activity, classe);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity.getApplicationContext());
        stackBuilder.addParentStack(classe);
        stackBuilder.addNextIntent(intent);
        int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
        pendingIntent = stackBuilder.getPendingIntent(1, flag);
    }
    //***********

    public String formatDecimal(double amount) {
        return String.format("%.2f", amount);
    }
    public String getDateDDMMYYYY() {
        Date fechaActual = new Date();
        // Formatear la fecha en el formato DD/MM/YYYY
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaActual);
        return fechaFormateada;
    }
    public String getDateYesterdayDDMMYYYY() {
        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Crear un objeto Calendar con la fecha actual
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);

        // Restar un día al Calendar
        calendar.add(Calendar.DATE, -1);

        // Obtener la fecha de ayer a partir del Calendar
        Date fechaAyer = calendar.getTime();

        // Formatear la fecha de ayer en el formato DD/MM/YYYY
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaAyer);

        return fechaFormateada;
    }
    public String getDateTimeDDMMYYYYHHMMSS() {
        Date fechaActual = new Date();
        // Formatear la fecha en el formato DD/MM/YYYY HH:mm:ss
        SimpleDateFormat formatoFechaTiempo = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaTiempoFormateada = formatoFechaTiempo.format(fechaActual);
        return fechaTiempoFormateada;
    }

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    }
                }
            } else {
                // Para versiones anteriores a Android Marshmallow
                android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    return true;
                }
            }
        }

        // Si no hay conexión, muestra un mensaje y retorna falso
        return false;
    }

}
