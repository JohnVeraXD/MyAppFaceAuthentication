package com.example.myappfaceauthentication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.myappfaceauthentication.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClasificarImg {

    public static String clasificarImagen(Context context, Bitmap image) {
        String Opcion = "Nada";
        int tamañoImagen = 32;
        try {

            Model model = Model.newInstance(context);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * tamañoImagen * tamañoImagen * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[tamañoImagen * tamañoImagen];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;

            for (int i = 0; i < tamañoImagen; i++) {
                for (int j = 0; j < tamañoImagen; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            //Se procesa el modelo
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // Predecir a cual pertenece
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            //Dar el resultado dependiendo cual es el mas alto
            String[] classes = {"Elon Musk", "Jeff Bezos", "Mark Zuckerberg"};

            Opcion = (classes[maxPos]);

            //Cerrar el modelo
            model.close();

        } catch (IOException e) {
            Log.d(String.valueOf(e), "Error");
        }
        return Opcion;
    }
}
