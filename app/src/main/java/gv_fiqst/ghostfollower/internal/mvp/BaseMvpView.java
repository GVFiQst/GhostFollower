package gv_fiqst.ghostfollower.internal.mvp;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseMvpView<T> extends AppCompatActivity implements MvpView<T> {
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
