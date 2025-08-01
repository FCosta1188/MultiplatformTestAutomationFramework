package eu.nets.test.core.enums;

import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.util.EnvUtil;
import eu.nets.test.util.PropertiesUtil;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum MpaWidget {
    //BUTTONs-------------------------------
    //Home
    OVERVIEW_DASHBOARD_BUTTON(
            "FrameLayout", "action_dashboard",
            "Button", "overview_button", "1", "OVERVIEW",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    NEXI_SOFTPOS_DASHBOARD_BUTTON(
            "FrameLayout", "action_softpay_enroll_virtual_terminal",
            "Button", "taptopay_button", null, "NEXI SOFTPOS",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    NETS_SOFTPOS_DASHBOARD_BUTTON(
            "FrameLayout", "action_softpay_enroll_virtual_terminal",
            "Button", "taptopay_button", null, "NETS SOFTPOS",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TERMINALS_DASHBOARD_BUTTON(
            "FrameLayout", "action_terminals",
            "Button", "terminal_button", null, "TERMINALS",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SUPPORT_DASHBOARD_BUTTON(
            "FrameLayout", "action_support",
            "Button", "support_button", null, "SUPPORT",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    //--------------------------

    LOGIN_BUTTON(
            "Button", "getStartedButton",
            "Button", "Log in", null, "Log in",
            MPA_ID(),
            "new_tut_signin_btn", "LOG IN",
            null, null
    ),
    CONTINUE_BUTTON(
            "Button", "continueButton",
            "Button", "enrollment_email_button_next", null, "Continue",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "CONTINUE",
            null, null
    ),
    RESEND_CODE_BUTTON(
            "Button", "resendCodeButton",
            "Button", "Resend code", null, "Resend code",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CODE_SENT_OK_BUTTON(
            "Button", "positiveButton",
            "Button", "OK", null, "OK",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SEE_DATA_BUTTON(
            "Button", "enrlContinueBtn",
            "Button", "setup_button_finish", null, "SEE DATA",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TERMINAL_SETUP_SKIP_BUTTON(
            "Button", "select_terminal_close_button",
            "Button", "I DON'T NEED TO SETUP TERMINALS", null, "I DON'T NEED TO SETUP TERMINALS",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SKIP_GET_STARTED_CLOSE_BUTTON(
            "Button", "cancel_icon",
            "Button", "close_decline", null, "close_decline",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    NOTIFICATION_BUTTON(
            "ImageView", "notificationIcon",
            "Button", "notification-icon-small", null, "notification-icon-small",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PROFILE_BUTTON(
            "ImageView", "menu_icon",
            "Button", "profile-icon-small", null, "profile-icon-small",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CALL_US_BUTTON(
            "ImageView", "call_us_icon",
            "Button", "callUs support", null, "callUs support",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CHAT_WITH_US_BUTTON(
            "ImageView", "chat_icon",
            "Button", "chatBot support", null, "chatBot support",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    GET_HELP_BUTTON(
            "Button", "getHelpButton",
            "Button", "enrollment_pin_button_help", null, "Get help signing in",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SELECT_COUNTRY_CONTINUE_BUTTON(
            "Button", "selectCountryContinueBtn",
            "Button", "Continue", null, "Continue",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    INSERT_VAT_CONTINUE_BUTTON(
            "Button", "screen2ContinueBtn",
            "Button", "Continue", null, "Continue",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    REQUEST_ACCESS_BUTTON(
            "Button", "requestAccessBtn",
            "Button", "Request access", null, "Request access",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    OVERVIEW_TRANSACTION_BUTTON(
            "TextView", "tv_label",
            "Button", "Transactions", "Transactions", "Transactions",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    OVERVIEW_ACCOUNTING_BUTTON(
            "TextView", "tv_label",
            "Button", "Accounting", "Accounting", "Accounting",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    OVERVIEW_ANALYTICS_BUTTON(
            "TextView", "tv_label",
            "Button", "Analytics", "Analytics", "Analytics",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TERMINALS_DETAIL_BUTTON(
            "ViewGroup", "terminal",
            "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TERMINAL_CHAT_BUTTON(
            "LinearLayout",
            "terminalChat",
            "StaticText",
            "Need help with your terminal? Chat with us",
            "Need help with your terminal? Chat with us",
            "Need help with your terminal? Chat with us",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null
    ),

    //Transactions
    TRANSACTIONS_EMPTY_STATE_TITLE(
            "TextView", "empty_text_title",
            "StaticText", "No transactions yet", "No transactions yet", "No transactions yet",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TRANSACTIONS_EMPTY_STATE_TEXT(
            "TextView", "empty_text",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Accounting
    ACCOUNTING_TITLE(
            "TextView", "toolbarTitle",
            "StaticText", "Accounting", "Accounting", "Accounting",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_TAB(
            "TextView", "",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_RECORD_INVOICE_NO(
            "TextView", "invoiceNumber",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_RECORD_INVOICE_DATE(
            "TextView", "invoiceDate",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_STATEMENTS_FILTER_BUTTON(
            "ImageView", "img_filter",
            "Button", "Filter", null, "Filter",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_SETTLEMENTS_INVOICES_FILTER_BUTTON(
            "ImageView", "img_filter",
            "Button", "settings", null, "settings",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_FILTER(
            "TextView", "filterType",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_STATEMENT_NO_INPUT(
            "EditText", "editText",
            "TextField", null, "Statement no", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_DATE_FILTER_YEAR_SELECTOR(
            "Button", "month_navigation_fragment_toggle",
            "Button", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_DATE_FILTER_MONTH_NAV_PREV(
            "Button", "month_navigation_previous",
            "Button", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_DATE_FILTER_MONTH_NAV_NEXT(
            "Button", "month_navigation_next",
            "Button", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_DATE_FILTER_OK_BUTTON(
            "Button", "confirm_button",
            "Button", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_DATE_FILTER_APPLY_BUTTON(
            "", "",
            "Button", "Apply", null, "Apply",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_APPLY_BUTTON(
            "Button", "applyButton",
            "Button", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_DONE_BUTTON(
            "Button", "doneButton",
            "Button", null, null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_CLEAR_ALL_BUTTON(
            "TextView", "rightText",
            "StaticText", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_CLEAR_FILTER_BUTTON(
            "Button", "btnClearFilters",
            "Button", "Clear filters", null, "Clear filters",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_RESET_FILTERS_BUTTON(
            "TextView", "retryBtn",
            "Button", "Reset filters", null, "Reset filters",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE(
            "TextView", "empty_text_title",
            "StaticText", "No Payouts yet", "No Payouts yet", "No Payouts yet",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT(
            "TextView", "empty_text",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_INVOICES_EMPTY_STATE_TITLE(
            "TextView", "empty_text_title",
            "StaticText", "No Invoices yet", "No Invoices yet", "No Invoices yet",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_INVOICES_EMPTY_STATE_TEXT(
            "TextView", "empty_text",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_SHARE_BUTTON(
            "ImageView", "shareIcon",
            "Button", "share", null, "share",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_BACK_BUTTON(
            "ImageView", "back_icon",
            "Button", "Back", null, "Back",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCOUNTING_FILTERS_CLOSE_BUTTON(
            "ImageView", "cancel_icon",
            "Image", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_GET_IN_CONTACT_BUTTON(
            "Button", "learn_more",
            "Button", "GET IN CONTACT", null, "GET IN CONTACT",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_GET_IN_CONTACT_CLOSE_BUTTON(
            "ImageView", "financing_advert_dismiss_btn",
            "Button", "roundClose", null, "roundClose",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_WEBVIEW_LOGO_NEXI(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "Image", "nexi logo", null, "nexi logo",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_WEBVIEW_LOGO_NETS(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "Image", null, null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_WEBVIEW_NETS_ACCOUNT(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "StaticText", "Sign in for your Nets Account", "Sign in for your Nets Account", "Sign in for your Nets Account",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_WEBVIEW_BACK_BUTTON(
            "ImageView", "back_icon",
            "Button", "Back", null, "Back",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SETTLEMENTS_WEBVIEW_BACK_BUTTON_IOS_BUG(
            "ImageView", "back_icon",
            "Button", "“ ”", null, "“ ”",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //WebView
    WEBVIEW(
            "android.webkit.WebView", "webView",
            "WebView", null, null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Analytics Elements (graphs and tabs)
    ANALYTICS_SCREEN_TITLE(
            "TextView", "toolbarTitle",
            "StaticText", "Analytics", "Analytics", "Analytics",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ANALYTICS_CLOSE_ICON(
            "ImageView", "img_close",
            "Button", "close large", "close large", "close large",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ANALYTICS_SECTION_TAB(
            "RelativeLayout", "layoutAnalyticsTab",
            "Other", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TURNOVER_GRAPH(
            "FrameLayout", "turnoverCardView",
            "Cell", "turnover_card", "turnover_card", "turnover_card",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACTIVITY_GRAPH(
            "FrameLayout", "activityCardView",
            "Cell", "activity_card", "activity_card", "activity_card",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    INTERNATIONAL_CUSTOMERS_GRAPH(
            "FrameLayout", "internationalCustomersCardView",
            "Cell", "tourist_card", "tourist_card", "tourist_card",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    AVERAGE_PURCHASE_GRAPH(
            "FrameLayout", "averagePurchaseCardView",
            "Cell", "average_card", "average_card", "average_card",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    RECURRING_CUSTOMERS_GRAPH(
            "FrameLayout", "recurringCustomersCardView",
            "Cell", "recurring_card", "recurring_card", "recurring_card",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Sidemenu
    SIDEMENU_MYINFO(
            "LinearLayoutCompat", "my_info",
            "Image", "sidemenu_myinfo", null, "sidemenu_myinfo",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SIDEMENU_LANGUAGE(
            "LinearLayoutCompat", "language",
            "Image", "sidemenu_language", null, "sidemenu_language",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SIDEMENU_SECURITY(
            "LinearLayoutCompat", "security",
            "Image", "sidemenu_security", null, "sidemenu_security",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //MyInfo
    MYINFO_PERSONAL_INFORMATION(
            "TextView", "header",
            "StaticText", "Personal information", "Personal information", "Personal information",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_FULLNAME(
            "TextView", "accNameLabel",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_EMAIL(
            "TextView", "accEmailLabel",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_PHONE(
            "TextView", "accPhoneLabel",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_COMPANY_INFORMATION(
            "TextView", "header",
            "StaticText", "Company Information", "Company Information", "Company Information",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_COMPANIES_INFORMATION(
            "TextView", "header",
            "StaticText", "Companies information", "Companies information", "Companies information",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_ORG(
            "TextView", "accInfoValue",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_VATNO_LABEL(
            "TextView", "accCvrLabel",
            "StaticText", "VAT NO.", "VAT NO.", "VAT NO.",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_VATNO(
            "TextView", "accCvrValue",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_ADDRESS_LABEL(
            "TextView", "accAddressLabel",
            "StaticText", "Address", "Address", "Address",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    MYINFO_ADDRESS(
            "TextView", "accAddressValue",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Language
    LANGUAGE_PAGE_TITLE(
            "", "",
            "NavigationBar", "Language", null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ORIGINAL_LANGUAGE_LABEL(
            "TextView", "label",
            "StaticText", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    APP_LANGUAGE_LABEL(
            "TextView", "label_in_language",
            "StaticText", "Language", null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    LANGUAGE_LIST_CONTAINER(
            "GridView", "recycler_view",
            "Table", "change_lamguage_table_view", null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CHANGE_LANGUAGE_CONFIRM_BUTTON(
            "Button", "button1",
            "Button", "", null, null,
            "android",
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    //Security
    SECURITY_TITLE(
            "TextView", "toolbarTitle",
            "StaticText", "Security", "Security", "Security",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Inputs
    EMAIL_INPUT(
            "EditText", "emailField",
            "TextField", "enrollment_email_text_field_email", "E-mail registered with us", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    OTP_INPUT(
            "EditText", "pinField",
            "TextField", null, null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    VAT_INPUT(
            "EditText", "enrlCVREd",
            "TextField", null, "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //PIN
    PIN_TITLE(
            "TextView", "lockTitle",
            "StaticText", "modify_pin_view_label_title", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_DESCRIPTION(
            "TextView", "lockDescription",
            "", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_BACK_BUTTON(
            "ImageButton", "",
            "", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_INFO_BUTTON(
            "ImageView", "infoIcon",
            "Button", "Info", null, "Info",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_INFO_CLOSE_BUTTON(
            "ImageView", "cancel_icon",
            "Button", "close large", null, "close large",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_INFO_TEXT(
            "", "",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_INFO_RULES_TITLE(
            "TextView", "pinRulesTitle",
            "", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_INFO_RULES(
            "TextView", "pinRules",
            "", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    PIN_INPUT(
            "EditText", "lockPinView",
            "SecureTextField", null, null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    SET_PIN_INPUT(
            "EditText", "lockPinView",
            "SecureTextField", "modify_pin_view_text_field_pin", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CHANGE_CODE_BUTTON(
            "TextView", "",
            "StaticText", "Change code", "Change code", "Change code",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CHANGE_CODE_TITLE(
            "", "",
            "StaticText", "Change code", "Change code", "Change code",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    FORGOT_CODE_BUTTON(
            "TextView", "lockIForgotPinCode",
            "Button", "require_pin_view_button_forgot_pin", null, "I forgot my code",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Selectors: checkboxes, switches, toggles, ...
    SELECT_ALL_LOCATIONS_CHECKBOX(
            "CheckBox", "txt_select_all",
            "StaticText", "Select all", "Select all", "Select all",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    LOCATIONS_ORG_TOGGLE(
            "TextView", "organizationToggle",
            "Other", "", null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACCEPT_TERMS_SWITCH(
            "Switch", "setAcceptTermsSwitch",
            "Switch", "setup_switch_accept", "1", null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Containers
    LOCATIONS(
            "RecyclerView", "merchantHierarchy",
            "Table", "setup_table_view", null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    COUNTRY_SPINNER("Spinner",
            "countrySpinner",
            "Button",
            "arrowtriangle.down.fill", "WIP", "WIP",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),

    //Viewers: text, labels, ...
    SECTION_TITLE(
            "TextView", "toolbarTitle",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    TITLE(
            "TextView", "title",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    LABEL("TextView", "label", "StaticText", "iosAxeId_PLACEHOLDER", "WIP", "WIP", MPA_ID(), "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER", null, null),
    TEXT_HEADER("TextView", "text_header", "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    SPINNER_COUNTRY_NAME(
            "TextView", "txt_country_name",
            "Button", "", null, "",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    SPINNER_COUNTRY_SCROLLBAR(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "Other", "Vertical scroll bar, 2 pages", "0%", "Vertical scroll bar, 2 pages",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    ORG_NAME(
            "TextView", "name",
            "Other", "", null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    ACTION_SUPPORT(
            "TextView", "action_support",
            "StaticText", "", "", "",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    TERMINAL_LOCATION("TextView", "terminal_location",
            "Image", "Arrow_Right", "Arrow_Right", "Arrow_Right",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    EDIT_RECEIPT_TITLE("TextView", "terminal_location",
            "StaticText", "Edit the receipt", "Edit the receipt", "Edit the receipt",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    EDIT_RECEIPT_SUBTITLE("TextView", "terminal_id",
            "StaticText", "The printed receipt", "The printed receipt", "The printed receipt",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    AUTOMATIC_TIP_TITLE("TextView", "terminal_location",
            "StaticText", "Automatic tip function", "Automatic tip function", "Automatic tip function",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    AUTOMATIC_TIP_SUBTITLE("TextView", "terminal_id",
            "StaticText", "Set it up", "Set it up", "Set it up",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    TERMINAL_ID("TextView", "terminal_id",
            "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),

    //Android alerts, notifications, popups
    ALERT_POPUP("TextView", "alertTitle", "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            "android", "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    ALERT_APPINFO_BUTTON("Button", "aerr_app_info", "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            "android", "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    ALERT_WAIT_BUTTON("Button", "aerr_wait", "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            "android", "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    ALERT_CLOSE_BUTTON("Button", "aerr_close", "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            "android", "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    ALLOW_NOTIFICATIONS_BUTTON(
            "Button", "permission_allow_button",
            "Button", "Allow", "", "",
            ANDROID_PERMISSION_CONTROLLER_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //MPA Card Articles
    CARD_CAROUSEL(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "CollectionView", null, null, null,
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CLOSE_CARD_BUTTON(
            "ImageButton", "imgClose",
            "Image", "Article_card_close_button", null, "Article_card_close_button",
            //after refactor: "Button", "Icon close", null, "Icon close", // (//XCUIElementTypeButton[@name="Icon close"])[1]
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_LABEL(
            "TextView", "section",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ICON(
            "ImageView", "section_icon",
            "Image", "article_PLACEHOLDER", null, "article_PLACEHOLDER",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE(
            "RecyclerView", "rv_article",
            "ScrollView", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    // ARTICLE_CONTAINER("LinearLayout", "container", ...)
    CARD_ARTICLE_BACK_BUTTON(
            "ImageButton", "back_button",
            "Button", "Back", null, "Back",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE_TITLE(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE_DESCRIPTION(
            "TextView", "description",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE_PARAGRAPH_TITLE(
            "TextView", "paragraph_title",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE_PARAGRAPH_DESCRIPTION(
            "TextView", "paragraph_description",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE_PARAGRAPH_HYPERLINK(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "Button", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    CARD_ARTICLE_SOFTPOS_ACTIVATEYOURLICENCE_BUTTON(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "Button", "", null, "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Nexi/Nets Softpos
    DEVICE_NOT_SUPPORTED_HEADER(
            "TextView", "header",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    DEVICE_NOT_SUPPORTED_TITLE(
            "TextView", "title",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    DEVICE_NOT_SUPPORTED_DESCRIPTION(
            "TextView", "description",
            "StaticText", "", "", "",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),

    //Other
    NAV_BAR_ACTIVE_ITEM("TextView", "navigation_bar_item_large_label_view", "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            MPA_ID(), "lokaliseKey_PLACEHOLDER",
            "enText_PLACEHOLDER",
            null,
            null),
    NAV_BAR_INACTIVE_ITEM(
            "TextView", "navigation_bar_item_small_label_view",
            "iosType_PLACEHOLDER", "iosAxeId_PLACEHOLDER", "WIP", "WIP",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    IOS_SOFTWARE_KEYBOARD_DONE_BUTTON(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "Button", "Done", null, "Done",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    ),
    IOS_UPDATE_APP_BUTTON(
            "androidType_PLACEHOLDER", "androidId_PLACEHOLDER",
            "StaticText", "UPDATE", "UPDATE", "UPDATE",
            MPA_ID(),
            "lokaliseKey_PLACEHOLDER", "enText_PLACEHOLDER",
            null, null
    );

    private final String androidType;
    private final String androidId;
    private final String androidResourceId;
    private final String iosType;
    private final String iosAccessibilityId;
    private final String iosValue;
    private final String iosLabel;
    private final String enText;
    private final String lokaliseKey;
    private final String appOrBundleId;
    private final Color color;
    private final Path img;

    MpaWidget(String androidType, String androidId,
              String iosType, String iosAccessibilityId, String iosValue, String iosLabel,
              String appOrBundleId,
              String lokaliseKey, String enText,
              Color color, Path img
    ) {
        this.androidId = androidId;
        this.iosAccessibilityId = iosAccessibilityId; // equivalent to xpath fullName attribute in iOS
        this.iosValue = iosValue;
        this.iosLabel = iosLabel;
        this.appOrBundleId = appOrBundleId; // Android appId/package or iOS bundleId
        this.lokaliseKey = lokaliseKey;
        this.enText = enText;
        this.color = color;
        this.img = img;

        if (EnvUtil.isAndroid()) {
            if(androidType.contains(".")) {
                this.androidType = androidType;
            } else {
                this.androidType = "android.widget." + androidType;
            }
            this.androidResourceId = String.format("%s:id/%s", this.appOrBundleId, this.androidId);
            this.iosType = "";
        } else if (EnvUtil.isIos()) {
            this.iosType = "XCUIElementType" + iosType;
            this.androidType = "";
            this.androidResourceId = "";
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public String getAndroidType() {
        return androidType;
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getAndroidResourceId() {
        return this.androidResourceId;
    }

    public String getIosType() {
        return iosType;
    }

    public String getIosAccessibilityId() {
        return iosAccessibilityId;
    }

    public String getEnText() {
        return enText;
    }

    public String getLokaliseKey() {
        return lokaliseKey;
    }

    public String getAppOrBundleId() {
        return appOrBundleId;
    }

    public Color getColor() {
        return color;
    }

    public Path getImg() {
        return img;
    }

    public static Path IMG_DIR() {
        return Paths.get(PathKey.E2E_MODULE_ROOT.toString(), PathKey.IMG.resolve().asString());
    }

    public static String MPA_ID() {
        if (EnvUtil.isAndroid()) {
            return PropertiesUtil.ENV.getProperty("mpaAppId");
        } else if (EnvUtil.isIos()) {
            return PropertiesUtil.ENV.getProperty("mpaBundleId");
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public static String ANDROID_PERMISSION_CONTROLLER_ID() {
        return PropertiesUtil.ENV.getProperty("permissionControllerPackage");
    }

    public By byAndroidResourceId() {
        return By.id(getAndroidResourceId());
    }

    public By byAndroidXpathWithResourceId() {
        return By.xpath(String.format("//%s[@resource-id=\"%s\"]", androidType, getAndroidResourceId()));
    }

    public By byAndroidXpathWithResourceIdAndIndex(int index, boolean groupSelector) {
        if (groupSelector) {
            return By.xpath(String.format("(//%s[@resource-id=\"%s\"])[%s]", androidType, getAndroidResourceId(), index));
        } else {
            return By.xpath(String.format("//%s[@resource-id=\"%s\"][%s]", androidType, getAndroidResourceId(), index));
        }
    }

    public By byAndroidXpathWithResourceIdAndAttribute(String attribute, String attributeValue) {
        return By.xpath(String.format("//%s[@resource-id=\"%s\" and %s]", androidType, getAndroidResourceId(), buildPredicate(attribute, attributeValue)));
    }

    public By byAndroidXpathWithAttribute(String attribute, String attributeValue) {
        return By.xpath(String.format("//%s[@%s=\"%s\"]", androidType, attribute, attributeValue));
    }

    public By byIosAccessibilityId() {
        return AppiumBy.accessibilityId(iosAccessibilityId);
    }

    public By byIosXpathWithName() {
        return By.xpath(String.format("//%s[@name=\"%s\"]", iosType, getIosAccessibilityId()));
    }

    public By byIosXpathWithName(String name) {
        return By.xpath(String.format("//%s[@name=\"%s\"]", iosType, name));
    }

    public By byIosXpathWithValue() {
        return By.xpath(String.format("//%s[@value=\"%s\"]", iosType, iosValue));
    }

    public By byIosXpathWithValue(String value) {
        return By.xpath(String.format("//%s[@value=\"%s\"]", iosType, value));
    }

    public By byIosXpathWithLabel() {
        return By.xpath(String.format("//%s[@label=\"%s\"]", iosType, iosLabel));
    }

    public By byIosXpathWithLabel(String label) {
        return By.xpath(String.format("//%s[@label=\"%s\"]", iosType, label));
    }

    public By byIosXpathWithIndex(int index) {
        return By.xpath(String.format("//%s[@index=\"%d\"]", iosType, index));
    }

    public By byIosXpathWithNameAndIndex(String name, int index) {
        return By.xpath(String.format("(//%s[@name=\"%s\"])[%d]", iosType, name, index));
    }

    public By byIosXpathWithNameAndAttribute(String attribute, String attributeValue) {
        return By.xpath(String.format("//%s[@name=\"%s\"  and %s]", iosType, getIosAccessibilityId(), buildPredicate(attribute, attributeValue)));
    }

    public By byXpath(String xpath) {
        return By.xpath(xpath);
    }

    private String buildPredicate(String attribute, String attributeValue) {
        if (attributeValue.contains("\"") && attributeValue.contains("'")) {
            return String.format("@%s=%s", attribute, buildConcatString(attributeValue));
        } else if (attributeValue.contains("\"")) {
            return String.format("@%s='%s'", attribute, attributeValue);
        } else {
            return String.format("@%s=\"%s\"", attribute, attributeValue);
        }
    }

    private String buildConcatString(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("concat(");
        boolean first = true;
        for (int i = 0; i < text.length(); i++) {
            String part;
            char c = text.charAt(i);
            if (c == '\'') {
                part = "\"'\"";
            } else if (c == '"') {
                part = "'\"'";
            } else {
                part = "'" + c + "'";
            }
            if (!first) {
                sb.append(", ");
            }
            sb.append(part);
            first = false;
        }
        sb.append(")");
        return sb.toString();
    }
}
