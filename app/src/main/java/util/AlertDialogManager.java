package util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pong.moviespopcorn.R;

/**
 * Created by pong on 10/4/15 AD.
 */
public class AlertDialogManager {

    private static AlertDialogManager instance = null;

    private AlertDialogManager(){

    }

    public static AlertDialogManager getInstance() {
        if(instance == null) {
            instance = new AlertDialogManager();
        }
        return instance;
    }

    public static void alert(Context context, String message) {

        final Dialog alertDialog = initDialog(new Dialog(context));

        setupLayout(alertDialog,message);
        alertDialog.show();
    }

    private static Dialog initDialog(Dialog alertDialog){
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog);
        return alertDialog;
    }

    private static void setupLayout(Dialog alertDialog,String message){
        TextView textView = (TextView) alertDialog.findViewById(R.id.text_dialog);
        textView.setText(message);

        Button btnDone = (Button) alertDialog.findViewById(R.id.btn_dialog);
        btnDone.setOnClickListener(new OnButtonClickListener(alertDialog));
    }

    static class OnButtonClickListener implements View.OnClickListener{
        private Dialog _dialog;

        public OnButtonClickListener(Dialog dialog){
            this._dialog = dialog;
        }
        @Override
        public void onClick(View arg0) {
            _dialog.dismiss();
        }
    }
}
