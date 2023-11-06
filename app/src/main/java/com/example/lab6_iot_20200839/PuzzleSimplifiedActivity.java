package com.example.lab6_iot_20200839;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

import com.example.lab6_iot_20200839.databinding.ActivityPuzzleSimplifiedBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream;

import java.util.Random;

public class PuzzleSimplifiedActivity extends AppCompatActivity {
    private ActivityPuzzleSimplifiedBinding binding;
    private ImageView imagenSubida;
    private Button btnsubirImage;
    private Button btnempezarJuego;
    private GridLayout gridLayout;
    private String[] list;
    private int randomColumns;
    private Bitmap[] imageParts;
    private int dimensiones = randomColumns * randomColumns;
    private static final int minColumns = 3;
    private static final int maxColumns = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPuzzleSimplifiedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imagenSubida=findViewById(R.id.imagenSubida);
        gridLayout = findViewById(R.id.buttons);
        btnsubirImage=findViewById((R.id.btnSubirImagen));
        btnempezarJuego=findViewById(R.id.btnIniciarJuego);



        btnsubirImage.setOnClickListener(view ->  {
            randomColumns=getRandomBoardSize();
            // Configura el GridLayout con el número de columnas aleatorio
            gridLayout.setColumnCount(randomColumns);
            gridLayout.setRowCount(randomColumns);
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        btnempezarJuego.setOnClickListener(view ->  {
            mezclar(imageParts);
            displayImageParts(imageParts);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        imagenSubida.setImageURI(uri);
        if (resultCode == RESULT_OK && uri != null) {
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                int newWidth = (int) getResources().getDimension(R.dimen.image_width);
                int newHeight = (int) getResources().getDimension(R.dimen.image_height);
                Bitmap resizedBitmap = resizeImage(originalBitmap, newWidth, newHeight);

                imagenSubida.setImageBitmap(resizedBitmap);
                imagenSubida.setVisibility(View.GONE);
                imageParts = splitImage(resizedBitmap, randomColumns); // Dividir la imagen en partes
                displayImageParts(imageParts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int getRandomBoardSize() {
        Random random = new Random();
        int randomIndex = random.nextInt(3) + 3; // Genera un número aleatorio entre 3 y 5 (ambos incluidos)
        return randomIndex;
    }
    private void mezclar(Bitmap[] imageParts){
        int index;
        Bitmap temp;
        Random random = new Random();
        for (int i=imageParts.length - 1; i > 0; i--){
            index = random.nextInt(i+1);
            temp = imageParts[index];
            imageParts[index] = imageParts[i];
            imageParts[i] = temp;
        }
    }
    private void init() {
        list = new String[dimensiones];
        for (int i = 0; i<dimensiones;i++){
            list[i] = String.valueOf(i);
        }
    }
    private Bitmap[] splitImage(Bitmap image, int numParts) {
        int width = image.getWidth();
        int height = image.getHeight();
        int partWidth = width / numParts;
        int partHeight = height / numParts;
        Bitmap[] parts = new Bitmap[numParts * numParts];

        int partNumber = 0;
        for (int i = 0; i < numParts; i++) {
            for (int j = 0; j < numParts; j++) {
                Bitmap partBitmap = Bitmap.createBitmap(image, j * partWidth, i * partHeight, partWidth, partHeight);
                parts[partNumber] = partBitmap;
                partNumber++;

                Log.d("PartInfo", "Part " + partNumber + ": Width = " + partBitmap.getWidth() + ", Height = " + partBitmap.getHeight());
            }
        }

        return parts;
    }


    private String encodeBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    private void displayImageParts(Bitmap[] imageParts) {
        gridLayout.removeAllViews(); // Limpia el GridLayout antes de agregar nuevas partes

        int numRows = gridLayout.getRowCount();
        int numCols = gridLayout.getColumnCount();
        int marginInPixels = (int) getResources().getDimension(R.dimen.margin_1dp);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int index = i * numCols + j;
                if (index < imageParts.length) {
                    Bitmap partBitmap = imageParts[index];
                    ImageView imageView2 = new ImageView(this);
                    imageView2.setImageBitmap(partBitmap);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels); // Aplica márgenes de 1dp
                    imageView2.setLayoutParams(params);
                    gridLayout.addView(imageView2);
                }
            }
        }
    }
    private Bitmap resizeImage(Bitmap originalImage, int newWidth, int newHeight) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, false);
    }


}