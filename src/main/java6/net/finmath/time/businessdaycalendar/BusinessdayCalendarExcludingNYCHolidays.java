package net.finmath.time.businessdaycalendar;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * A business day calendar, where every day is a business day, except for weekends and New York holidays
 * 
 * @author Niklas Rodi
 */
public class BusinessdayCalendarExcludingNYCHolidays extends BusinessdayCalendarExcludingGivenHolidays {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3512269252486431367L;
	/*
	 * Details of this calendar.
	 * If you like to create a similar calendar, just duplicate this class and
	 * modify the following two lines.
	 */
	private static final String NAME = "New York";
	private static final String[] HOLIDAYLISTASSTRINGS = new String[] { "17/01/2000", "21/02/2000", "29/05/2000",
			"04/07/2000", "04/09/2000", "09/10/2000", "23/11/2000", "25/12/2000", "01/01/2001", "15/01/2001",
			"19/02/2001", "28/05/2001", "04/07/2001", "03/09/2001", "08/10/2001", "12/11/2001", "22/11/2001",
			"25/12/2001", "01/01/2002", "21/01/2002", "18/02/2002", "27/05/2002", "04/07/2002", "02/09/2002",
			"14/10/2002", "11/11/2002", "28/11/2002", "25/12/2002", "01/01/2003", "20/01/2003", "17/02/2003",
			"26/05/2003", "04/07/2003", "01/09/2003", "13/10/2003", "11/11/2003", "27/11/2003", "25/12/2003",
			"01/01/2004", "19/01/2004", "16/02/2004", "31/05/2004", "05/07/2004", "06/09/2004", "11/10/2004",
			"11/11/2004", "25/11/2004", "17/01/2005", "21/02/2005", "30/05/2005", "04/07/2005", "05/09/2005",
			"10/10/2005", "11/11/2005", "24/11/2005", "26/12/2005", "02/01/2006", "16/01/2006", "20/02/2006",
			"29/05/2006", "04/07/2006", "04/09/2006", "09/10/2006", "23/11/2006", "25/12/2006", "01/01/2007",
			"15/01/2007", "19/02/2007", "28/05/2007", "04/07/2007", "03/09/2007", "08/10/2007", "12/11/2007",
			"22/11/2007", "25/12/2007", "01/01/2008", "21/01/2008", "18/02/2008", "26/05/2008", "04/07/2008",
			"01/09/2008", "13/10/2008", "11/11/2008", "27/11/2008", "25/12/2008", "01/01/2009", "19/01/2009",
			"16/02/2009", "25/05/2009", "07/09/2009", "12/10/2009", "11/11/2009", "26/11/2009", "25/12/2009",
			"01/01/2010", "18/01/2010", "15/02/2010", "31/05/2010", "05/07/2010", "06/09/2010", "11/10/2010",
			"11/11/2010", "25/11/2010", "17/01/2011", "21/02/2011", "30/05/2011", "04/07/2011", "05/09/2011",
			"10/10/2011", "11/11/2011", "24/11/2011", "26/12/2011", "02/01/2012", "16/01/2012", "20/02/2012",
			"28/05/2012", "04/07/2012", "03/09/2012", "08/10/2012", "12/11/2012", "22/11/2012", "25/12/2012",
			"01/01/2013", "21/01/2013", "18/02/2013", "27/05/2013", "04/07/2013", "02/09/2013", "14/10/2013",
			"11/11/2013", "28/11/2013", "25/12/2013", "01/01/2014", "20/01/2014", "17/02/2014", "26/05/2014",
			"04/07/2014", "01/09/2014", "13/10/2014", "11/11/2014", "27/11/2014", "25/12/2014", "01/01/2015",
			"19/01/2015", "16/02/2015", "25/05/2015", "07/09/2015", "12/10/2015", "11/11/2015", "26/11/2015",
			"25/12/2015", "01/01/2016", "18/01/2016", "15/02/2016", "30/05/2016", "04/07/2016", "05/09/2016",
			"10/10/2016", "11/11/2016", "24/11/2016", "26/12/2016", "02/01/2017", "16/01/2017", "20/02/2017",
			"29/05/2017", "04/07/2017", "04/09/2017", "09/10/2017", "23/11/2017", "25/12/2017", "01/01/2018",
			"15/01/2018", "19/02/2018", "28/05/2018", "04/07/2018", "03/09/2018", "08/10/2018", "12/11/2018",
			"22/11/2018", "25/12/2018", "01/01/2019", "21/01/2019", "18/02/2019", "27/05/2019", "04/07/2019",
			"02/09/2019", "14/10/2019", "11/11/2019", "28/11/2019", "25/12/2019", "01/01/2020", "20/01/2020",
			"17/02/2020", "25/05/2020", "07/09/2020", "12/10/2020", "11/11/2020", "26/11/2020", "25/12/2020",
			"01/01/2021", "18/01/2021", "15/02/2021", "31/05/2021", "05/07/2021", "06/09/2021", "11/10/2021",
			"11/11/2021", "25/11/2021", "17/01/2022", "21/02/2022", "30/05/2022", "04/07/2022", "05/09/2022",
			"10/10/2022", "11/11/2022", "24/11/2022", "26/12/2022", "02/01/2023", "16/01/2023", "20/02/2023",
			"29/05/2023", "04/07/2023", "04/09/2023", "09/10/2023", "23/11/2023", "25/12/2023", "01/01/2024",
			"15/01/2024", "19/02/2024", "27/05/2024", "04/07/2024", "02/09/2024", "14/10/2024", "11/11/2024",
			"28/11/2024", "25/12/2024", "01/01/2025", "20/01/2025", "17/02/2025", "26/05/2025", "04/07/2025",
			"01/09/2025", "13/10/2025", "11/11/2025", "27/11/2025", "25/12/2025", "01/01/2026", "19/01/2026",
			"16/02/2026", "25/05/2026", "07/09/2026", "12/10/2026", "11/11/2026", "26/11/2026", "25/12/2026",
			"01/01/2027", "18/01/2027", "15/02/2027", "31/05/2027", "05/07/2027", "06/09/2027", "11/10/2027",
			"11/11/2027", "25/11/2027", "17/01/2028", "21/02/2028", "29/05/2028", "04/07/2028", "04/09/2028",
			"09/10/2028", "23/11/2028", "25/12/2028", "01/01/2029", "15/01/2029", "19/02/2029", "28/05/2029",
			"04/07/2029", "03/09/2029", "08/10/2029", "12/11/2029", "22/11/2029", "25/12/2029", "01/01/2030",
			"21/01/2030", "18/02/2030", "27/05/2030", "04/07/2030", "02/09/2030", "14/10/2030", "11/11/2030",
			"28/11/2030", "25/12/2030", "01/01/2031", "20/01/2031", "17/02/2031", "26/05/2031", "04/07/2031",
			"01/09/2031", "13/10/2031", "11/11/2031", "27/11/2031", "25/12/2031", "01/01/2032", "19/01/2032",
			"16/02/2032", "31/05/2032", "05/07/2032", "06/09/2032", "11/10/2032", "11/11/2032", "25/11/2032",
			"17/01/2033", "21/02/2033", "30/05/2033", "04/07/2033", "05/09/2033", "10/10/2033", "11/11/2033",
			"24/11/2033", "26/12/2033", "02/01/2034", "16/01/2034", "20/02/2034", "29/05/2034", "04/07/2034",
			"04/09/2034", "09/10/2034", "23/11/2034", "25/12/2034", "01/01/2035", "15/01/2035", "19/02/2035",
			"28/05/2035", "04/07/2035", "03/09/2035", "08/10/2035", "12/11/2035", "22/11/2035", "25/12/2035",
			"01/01/2036", "21/01/2036", "18/02/2036", "26/05/2036", "04/07/2036", "01/09/2036", "13/10/2036",
			"11/11/2036", "27/11/2036", "25/12/2036", "01/01/2037", "19/01/2037", "16/02/2037", "25/05/2037",
			"07/09/2037", "12/10/2037", "11/11/2037", "26/11/2037", "25/12/2037", "01/01/2038", "18/01/2038",
			"15/02/2038", "31/05/2038", "05/07/2038", "06/09/2038", "11/10/2038", "11/11/2038", "25/11/2038",
			"17/01/2039", "21/02/2039", "30/05/2039", "04/07/2039", "05/09/2039", "10/10/2039", "11/11/2039",
			"24/11/2039", "26/12/2039", "02/01/2040", "16/01/2040", "20/02/2040", "28/05/2040", "04/07/2040",
			"03/09/2040", "08/10/2040", "12/11/2040", "22/11/2040", "25/12/2040", "01/01/2041", "21/01/2041",
			"18/02/2041", "27/05/2041", "04/07/2041", "02/09/2041", "14/10/2041", "11/11/2041", "28/11/2041",
			"25/12/2041", "01/01/2042", "20/01/2042", "17/02/2042", "26/05/2042", "04/07/2042", "01/09/2042",
			"13/10/2042", "11/11/2042", "27/11/2042", "25/12/2042", "01/01/2043", "19/01/2043", "16/02/2043",
			"25/05/2043", "07/09/2043", "12/10/2043", "11/11/2043", "26/11/2043", "25/12/2043", "01/01/2044",
			"18/01/2044", "15/02/2044", "30/05/2044", "04/07/2044", "05/09/2044", "10/10/2044", "11/11/2044",
			"24/11/2044", "26/12/2044", "02/01/2045", "16/01/2045", "20/02/2045", "29/05/2045", "04/07/2045",
			"04/09/2045", "09/10/2045", "23/11/2045", "25/12/2045", "01/01/2046", "15/01/2046", "19/02/2046",
			"28/05/2046", "04/07/2046", "03/09/2046", "08/10/2046", "12/11/2046", "22/11/2046", "25/12/2046",
			"01/01/2047", "21/01/2047", "18/02/2047", "27/05/2047", "04/07/2047", "02/09/2047", "14/10/2047",
			"11/11/2047", "28/11/2047", "25/12/2047", "01/01/2048", "20/01/2048", "17/02/2048", "25/05/2048",
			"07/09/2048", "12/10/2048", "11/11/2048", "26/11/2048", "25/12/2048", "01/01/2049", "18/01/2049",
			"15/02/2049", "31/05/2049", "05/07/2049", "06/09/2049", "11/10/2049", "11/11/2049", "25/11/2049",
			"17/01/2050", "21/02/2050", "30/05/2050", "04/07/2050", "05/09/2050", "10/10/2050", "11/11/2050",
			"24/11/2050", "26/12/2050", "02/01/2051", "16/01/2051", "20/02/2051", "29/05/2051", "04/07/2051",
			"04/09/2051", "09/10/2051", "23/11/2051", "25/12/2051", "01/01/2052", "15/01/2052", "19/02/2052",
			"27/05/2052", "04/07/2052", "02/09/2052", "14/10/2052", "11/11/2052", "28/11/2052", "25/12/2052",
			"01/01/2053", "20/01/2053", "17/02/2053", "26/05/2053", "04/07/2053", "01/09/2053", "13/10/2053",
			"11/11/2053", "27/11/2053", "25/12/2053", "01/01/2054", "19/01/2054", "16/02/2054", "25/05/2054",
			"07/09/2054", "12/10/2054", "11/11/2054", "26/11/2054", "25/12/2054", "01/01/2055", "18/01/2055",
			"15/02/2055", "31/05/2055", "05/07/2055", "06/09/2055", "11/10/2055", "11/11/2055", "25/11/2055",
			"17/01/2056", "21/02/2056", "29/05/2056", "04/07/2056", "04/09/2056", "09/10/2056", "23/11/2056",
			"25/12/2056", "01/01/2057", "15/01/2057", "19/02/2057", "28/05/2057", "04/07/2057", "03/09/2057",
			"08/10/2057", "12/11/2057", "22/11/2057", "25/12/2057" };

	/*
	 * Static initializer for holidays set from dd/MM/yyyy spec
	 */
	private static final Set<LocalDate> HOLIDAYS;
	static {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Set<LocalDate> holidaysSet = new TreeSet<LocalDate>();
		for(String holidayAsString : HOLIDAYLISTASSTRINGS) {
			holidaysSet.add(LocalDate.parse(holidayAsString, formatter));
		}
		HOLIDAYS = Collections.unmodifiableSet(holidaysSet);
	}

	/**
	 * Create NEW YORK business day calendar.
	 */
	public BusinessdayCalendarExcludingNYCHolidays() {
		this(null);
	}

	/**
	 * Create NEW YORK business day calendar using a given business day calendar as basis.
	 * 
	 * @param baseCalendar Calendar of business days.
	 */
	public BusinessdayCalendarExcludingNYCHolidays(BusinessdayCalendarInterface baseCalendar) {
		super(NAME, baseCalendar, true);
	}

	public Set<LocalDate> getHolidays() { return HOLIDAYS; }
}
