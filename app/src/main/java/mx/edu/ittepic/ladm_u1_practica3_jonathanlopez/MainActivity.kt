package mx.edu.ittepic.ladm_u1_practica3_jonathanlopez

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,     //context es estática  --- check
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            //SI ENTRA ENTONCES AUN NO SE HAN OTORGADO PERMISOS
            //EL SIGUIENTE CODIGO LOS SOLICITA
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

        }else{
            Toast.makeText(this,"Permisos ya otorgados",Toast.LENGTH_LONG).show()
        }

        btnGuardarSD.setOnClickListener {
            if(txtGuardarSD.text.isEmpty()){Toast.makeText(this,"Ingrese un nombre para guardar el archivo",Toast.LENGTH_LONG).show()}else
            {
            guardarSD()}
        }//GuardarSD

        btnLeerSD.setOnClickListener {
            if(txtLeerSD.text.isEmpty()){Toast.makeText(this,"Ingrese un nombre para leer",Toast.LENGTH_LONG).show()}else
            leerSD()
        }//LeerSD
        btnAsignar.setOnClickListener {
            if(txtPosicion.text.isEmpty()){
                Toast.makeText(this,"Ingrese una posicion del 0 al 9",Toast.LENGTH_LONG).show()
            }else
            {
            asignar()}
        }
        btnMostrar.setOnClickListener {

            mostrarSD()
        }





    }


    var arreglo : Array<Int> =  Array(10, {0})

    private fun mostrarSD() {
        try {
            var data=""
            (0 .. 9).forEach {

                data += "[${arreglo[it]}]  "
            }

            AlertDialog.Builder(this).setMessage("Arreglo:"+"\n\n"+data).setPositiveButton("ok"){d,i->
                d.dismiss()

            }.show()




        }catch (error:IOException){
            AlertDialog.Builder(this)
                .setMessage(error.message.toString())
                .setTitle("Alerta")
                .setPositiveButton("OK"){d,i->
                    d.dismiss()
                }.show()
        }



    }

    private fun leerSD() {
        try {
            var rutaSD=Environment.getExternalStorageDirectory()
            var datosArchivo= File(rutaSD.absolutePath,txtLeerSD.text.toString())

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data= flujoEntrada.readLine()//.split("&")
            var vector = data.split("&")

            (0 .. 9).forEach {
                arreglo[it]=vector[it].toInt()
            }

            flujoEntrada.close()
            Toast.makeText(this,"Arreglo en memoria sobreescrito",Toast.LENGTH_LONG).show()


        }
        catch (error:FileNotFoundException){
            AlertDialog.Builder(this)
                .setMessage("El archivo no existe")
                .setTitle("Alerta")
                .setPositiveButton("OK"){d,i->
                    d.dismiss()
                }.show()
        }catch (error:IOException){
            AlertDialog.Builder(this)
                .setMessage(error.message.toString())
                .setTitle("Alerta")
                .setPositiveButton("OK"){d,i->
                    d.dismiss()
                }.show()
        }
    }



    private fun asignar() {

        arreglo[txtPosicion.text.toString().toInt()]= txtvValor.text.toString().toInt()
    }

    private fun guardarSD() {
        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,txtGuardarSD.text.toString())

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))

            var data = ""

            (0..9).forEach {it
                data+= arreglo[it].toString()+"&"
            }

            flujoSalida.write(data)
            flujoSalida.flush()    //commit
            flujoSalida.close()

            Toast.makeText(this,"Archivo Guardado",Toast.LENGTH_LONG).show()


        }catch (error : IOException){
            AlertDialog.Builder(this)
                .setMessage(error.message.toString())
                .setTitle("Alerta")
                .setPositiveButton("OK"){d,i->
                    d.dismiss()
                }.show()
        }
    }//fun Guardar




}
