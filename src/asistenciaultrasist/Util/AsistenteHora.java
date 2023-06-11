/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asistenciaultrasist.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author EliteBook8460p
 */
public class AsistenteHora {
    
    public Date horaSistema;
    public String horaProporcionada;
    
    public AsistenteHora() {
        this.horaSistema = horaSistema;
        this.horaProporcionada = horaProporcionada;
    }
    
    public String getHoraActual(){
        DateFormat getInicio = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");
        String horaGlobal = getInicio.format(new Date());
        return horaGlobal;
    }
    
    public String getRestarHora(Date horaEntrada){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaEntrada);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date newDate = calendar.getTime();
        DateFormat getInicio = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");
        String horaGlobal = getInicio.format(newDate);
        return horaGlobal;
    }
    
    public String getSumarHora(Date horaEntrada){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaEntrada);
        calendar.add(Calendar.HOUR_OF_DAY, +1);
        Date newDate = calendar.getTime();
        DateFormat getInicio = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");
        String horaGlobal = getInicio.format(newDate);
        return horaGlobal;
    }
    
    public String getFormatoTextWord(Date horaEntrada){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaEntrada);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date newDate = calendar.getTime();
        DateFormat getInicio = new SimpleDateFormat("HH:mm");
        String horaGlobal = getInicio.format(newDate);
        return horaGlobal;
    }
}
