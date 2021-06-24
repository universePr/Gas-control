package com.cent.testchart.constants;

import static com.cent.testchart.database.Helper.COLUMN_ID;
import static com.cent.testchart.database.Helper.COLUMN_TIME;

public class Constants {
    public static final String CARBON_MONOXID = "carbon_monoxide";
    public static final String LPG = "lpg";
    public static final String SMOKE = "smoke";
    // Text
    public static final String carbon_monoxide = "Carbon Monoxide";
    public static final String lpg = "LPG";
    public static final String smoke = "Smoke";
    // Tags
    public static String tag_co = "carbon_monoxide";
    public static String tag_lpg = "lpg";
    public static String tag_smoke = "smoke";
    // Database
    public static final String desc_order = " ORDER BY "+COLUMN_ID+" DESC ";
    public static final String limit_on_data = " LIMIT 0,5040;";// 5040 recorde for 7 day
    public static final String limit_like_data = "and "+COLUMN_TIME+" like";
    // Shared Preferences
    public static final String MyPREFERENCES = "PhoneNumber" ;
    public static final String Phone = "phoneKey";
    public static final String defPhone = "+980000000000";

    //Warning ppm
    public static final int WARNING_CO_PPM = 225;
    public static final int WARNING_LPG_PPM = 225;
    public static final int WARNING_SMOKE_PPM = 225;

    //Max
    public static final int max_CO_PPM = 300;
    public static final int max_LPG_PPM = 300;
    public static final int max_SMOKE_PPM = 300;

}
