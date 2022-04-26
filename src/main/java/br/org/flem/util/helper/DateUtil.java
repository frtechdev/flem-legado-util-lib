/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.util.helper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 *
 * @author tscortes
 */
public class DateUtil {

	  private DateUtil() {
		    throw new IllegalStateException("Utility class");
	  }
    /**
     * Verificar se o dia da data é sabado ou domigo
     *
     * @author Thiago Côrtes
     * @param date Date
     * @return Boolean
     */
    public static Boolean isFds(Date date) {
        Calendar data = Calendar.getInstance();
        data.setTime(date);
        return (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
    }

    /**
     * Verifica se o dia passado é um final de semana
     *
     * @param dia
     * @return Boolean
     */
    public static boolean isFds(LocalDate dia) {
        Date date = Date.from(dia.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return isFds(date);
    }

    /**
     * Obtém Dia da semana abreviado
     *
     * @author Thiago Côrtes
     * @param data Date
     * @return String
     */
    public static String getDayOfTheWeek(Date data) {
        Calendar dataInfo = Calendar.getInstance();
        dataInfo.setTime(data);
        switch (dataInfo.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return "Dom";
            case Calendar.SATURDAY:
                return "Sab";
            case Calendar.MONDAY:
                return "Seg";
            case Calendar.TUESDAY:
                return "Ter";
            case Calendar.WEDNESDAY:
                return "Qua";
            case Calendar.THURSDAY:
                return "Qui";
            case Calendar.FRIDAY:
                return "Sex";
            default:
                return "";
        }

    }

    /**
     * Obtém Dia da semana abreviado
     *
     * @author Thiago Côrtes
     * @param localDate LocalDate
     * @return String
     */
    public static String getDayOfTheWeek(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return getDayOfTheWeek(date);
    }

    /**
     *
     * @param data
     * @return
     */
    public static LocalDate convertToLocalDate(Date data) {
        return data == null ? null : data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * converter variavel LocalDate para util.Date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     *
     * @param startLocalDate
     * @param endLocalDate
     * @return
     */
    public static boolean isEqualsAndAfter(LocalDate startLocalDate, LocalDate endLocalDate) {
        if (startLocalDate == null || endLocalDate == null) {
            return false;
        }
        return startLocalDate.isEqual(endLocalDate) || startLocalDate.isAfter(endLocalDate);

    }

    /**
     *
     * @param localDate
     * @param localDateRef
     * @return
     */
    public static boolean isEqualsAndBefore(LocalDate localDate, LocalDate localDateRef) {
        if (localDate == null || localDateRef == null) {
            return false;
        }
        return localDate.isEqual(localDateRef) || localDate.isBefore(localDateRef);
    }

    /**
     *
     * @param localTime
     * @param localTimeRef
     * @return
     */
    public static Boolean isEqualsAndAfter(LocalTime localTime, LocalTime localTimeRef) {
        if (localTime == null || localTimeRef == null) {
            return Boolean.FALSE;
        }
        return localTime.compareTo(localTimeRef) >= 0;

    }

    /**
     *
     * @param localTime
     * @param localTimeRef
     * @return
     */
    public static Boolean isEqualsAndBefore(LocalTime localTime, LocalTime localTimeRef) {
        if (localTime == null || localTimeRef == null) {
            return Boolean.FALSE;
        }
        return localTime.compareTo(localTimeRef) <= 0;
    }

    /**
     * convete variável LocalTime para sql.Time
     *
     * @param localTime LocalTime
     * @return Time
     */
    public static Time convertToTime(LocalTime localTime) {
        return Time.valueOf(localTime);
    }

    /**
     * Calcula quantidade de dias entre duas datas
     *
     * @param dataFim LocalDate
     * @param dataInicio LocalDate
     * @return dias entre duas datas
     */
    public static Long getDaysBetweenDates(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null) {
            return ChronoUnit.DAYS.between(dataInicio, dataFim);
        }
        return 0l;
    }

    /**
     * Calcula quantidade de dias entre duas datas
     *
     * @param dataInicio
     * @param dataFim
     * @return dias entre duas datas
     */
    public static long getDaysBetweenDates(Date dataInicio, Date dataFim) {
        if (dataInicio != null && dataFim != null) {
            LocalDate inicio = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fim = dataFim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            return getDaysBetweenDates(inicio, fim);
        }
        return 0l;
    }

    /**
     * Calcula quantidade de minutos entre duas datas
     *
     * @param horaInicio
     * @param horaFim
     * @return total de mminutos entre dois localtime
     */
    public static long getMinutesBetweenDates(LocalTime horaInicio, LocalTime horaFim) {
        return ChronoUnit.MINUTES.between(horaInicio, horaFim);
    }

    /**
     * Calcula quantidade de minutos entre duas datas
     *
     * @param horaInicio
     * @param horaFim
     * @return total de mminutos entre dois localtime
     */
    public static long getMinutesBetweenDates(Time horaInicio, Time horaFim) {
        if (horaInicio != null && horaFim != null) {
            return ChronoUnit.MINUTES.between(horaInicio.toLocalTime(), horaFim.toLocalTime());
        }
        return 0l;
    }
    /**
     * 
     * @param data
     * @param pattern
     * @return 
     */
    public static LocalDate convertToLocalDate(String data, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(data, formatter);
    }
    /**
     * 
     * @param data
     * @param pattern
     * @return 
     */
    public static String formatarData(LocalDateTime data, String pattern) {
        Date date = java.sql.Timestamp.valueOf(data);
        return formatarData(date, pattern);
    }
    /**
     * 
     * @param data
     * @param pattern
     * @return 
     */
    public static String formatarData(LocalDate data, String pattern) {
        Date date = Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return formatarData(date, pattern);
    }
    /**
     * 
     * @param data
     * @param pattern
     * @return 
     */
    public static String formatarData(Date data, String pattern) {
        String strRetorno = null;
        if (data != null) {
            strRetorno = new SimpleDateFormat(pattern).format(data);
        }
        return strRetorno;
    }

    /**
     * Método que adiciona dias a uma data
     *
     * @author Thiago Côrtes
     * @param dt Date
     * @param days int
     * @return Data com dias adicionados
     */
    public static Date addDay(Date dt, int days) {
        Calendar calendarData = Calendar.getInstance();
        calendarData.setTime(dt);
        calendarData.add(Calendar.DATE, days);

        return calendarData.getTime();
    }

    /**
     * Recebe tempo em minutos e retorna hora formatada HH:mm
     *
     * @author Thiago Côrtes
     * @param time Long - parametro em minutos (hora * 60 + minutos)
     * @return String
     */
    public static String obterHoraEMinutoFormatada(Long time) {
        if (time != null && time != 0l) {
            String hora = "" + Math.abs(time) / 60;
            if (hora.length() == 1) {
                hora = 0 + hora;
            }
            String minuto = "" + Math.abs(time) % 60;
            if (minuto.length() == 1) {
                minuto = 0 + minuto;
            }
            return hora + ":" + minuto;
        }
        return "00:00";
    }

    /**
     * Recebe tempo em hora formatada HH:mm e retorna tempo em minutos
     *
     * @author Thiago Côrtes
     * @param horaEMinuto String
     * @return minutos Long - ex: 12:52 ( 12 * 60 + 52)
     */
    public static Integer obterHoraEMinutoEmMinutos(String horaEMinuto) {
        if (!StringUtils.isEmpty(horaEMinuto) && horaEMinuto.contains(":")) {
            String[] horas = horaEMinuto.split(":");
            return fromHourToMinutes(LocalTime.of(Integer.parseInt(horas[0]), Integer.parseInt(horas[1])));
        }
        return 0;
    }

    /**
     * Retorna um valor numerico referente a hora e minutos
     * @param time
     * @return
     */
    public static Integer fromHourToMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    /**
     * Retorna verdadeiro caso a startlLocalDate for menor que endLocalDateRef
     * @param startlLocalDate
     * @param endLocalDateRef
     * @return
     */
    public static Boolean isLessThan(LocalDate startlLocalDate, LocalDate endLocalDateRef) {
        if (endLocalDateRef == null || startlLocalDate == null) {
            return Boolean.FALSE;
        }
        return startlLocalDate.isBefore(endLocalDateRef);
    }
    
    /**
     * Retorna verdadeiro caso a startlLocalDate for maior que endLocalDateRef
     * @param startlLocalDate
     * @param endLocalDateRef
     * @return
     */
    public static boolean isAfter(LocalDateTime startlLocalDate, LocalDateTime endLocalDateRef) {
        if (endLocalDateRef == null || startlLocalDate == null) {
            return false;
        }
        return startlLocalDate.isAfter(endLocalDateRef);
    }
    
    
    /**
     * Retorna verdadeiro caso a startlLocalDate for maior que endLocalDateRef
     * @param start
     * @param end
     * @return
     */
    public static Boolean isAfter(ChronoLocalDateTime<?> start, ChronoLocalDateTime<?> end) {
        if (end == null || start == null) {
            return Boolean.FALSE;
        }
        return start.isAfter(end);
    }
    
    /**
     * Retorna verdadeiro caso a startlLocalDate for menor que endLocalDateRef
     * @param start
     * @param end
     * @return
     */
    public static Boolean isBefore(ChronoLocalDateTime<?> start, ChronoLocalDateTime<?> end) {
        return isAfter(end, start);
    }
    
    public static boolean isEquals(Temporal start, Temporal end) {
        return end.equals(start);
    }
    
    

}
