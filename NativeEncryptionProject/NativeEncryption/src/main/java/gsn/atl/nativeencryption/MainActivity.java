package gsn.atl.nativeencryption;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button generateFile;
    Button encryptLibrary;
    Button encryptNative;
    Button decryptLibrary;
    Button decryptNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();
        setListeners();
    }

    private void getViews(){
        generateFile = (Button) findViewById(R.id.generate_file);
        encryptLibrary = (Button) findViewById(R.id.encrypt_library);
        encryptNative = (Button) findViewById(R.id.encrypt_native);
        decryptLibrary = (Button) findViewById(R.id.decrypt_library);
        decryptNative = (Button) findViewById(R.id.decrypt_native);
    }

    private void setListeners(){
        generateFile.setOnClickListener(generateFileListener);
        encryptLibrary.setOnClickListener(encryptLibraryListener);
        encryptNative.setOnClickListener(encryptNativeListener);
        decryptLibrary.setOnClickListener(decryptLibraryListener);
        decryptNative.setOnClickListener(decryptNativeListener);
    }

    View.OnClickListener generateFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            generateFile();
        }
    };

    private void generateFile(){
        // generate a file of gibberish to encrypt/decrypt
    }

    View.OnClickListener encryptLibraryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            encryptLibrary();
        }
    };

    private void encryptLibrary(){
        Encryption.encryptLibrary();
    }

    View.OnClickListener encryptNativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            encryptNative();
        }
    };

    private void encryptNative(){
        // encrypt using JNI
    }

    View.OnClickListener decryptLibraryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decryptLibrary();
        }
    };

    private void decryptLibrary(){
        // decrypt using java libs
    }

    View.OnClickListener decryptNativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decryptNative();
        }
    };

    private void decryptNative(){
        // decrypt using JNI
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
