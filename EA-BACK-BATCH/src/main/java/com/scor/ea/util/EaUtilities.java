package com.scor.ea.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class EaUtilities {

	public final static String USERNAME_TOKEN = "UsernameToken";

	public final static String PASSWORD_DIGEST = "PasswordDigest";

	public final static String NONCE_CREATED = "Nonce Created";

	public static XMLGregorianCalendar xmlGregorianDateConverter(String stringDate)
			throws ParseException, DatatypeConfigurationException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date lastModificationDate = formatter.parse(stringDate);
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(lastModificationDate);
		XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		return date;
	}
	
	public static String getDate() {
		Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);
        return today;
	}
}
