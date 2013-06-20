package lib.ui;
import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Main extends Activity implements View.OnClickListener{
    private boolean mDTMFToneEnabled;  
    private Object mToneGeneratorLock = new Object();  
    private ToneGenerator mToneGenerator;  
    private static final int TONE_LENGTH_MS = 150;  
    private static final int TONE_RELATIVE_VOLUME = 80;  
    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_MUSIC;  
    private EditText mDigits;  
    private ImageButton deleteIMB;  
    private ImageButton dialIMB;  
    
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
       /* setContentView(R.layout.dial);  
        mDigits = (EditText) findViewById(R.id.digits);  
        deleteIMB=(ImageButton) findViewById(R.id.delete_back);  
        dialIMB=(ImageButton) findViewById(R.id.start_dial);  
        deleteIMB.setOnClickListener(this);  
        dialIMB.setOnClickListener(this);  
        setupKeypad();  */
    }  
    @Override
    public void onClick(View v)
    {
        
    }  
   
}  