package android.provider;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Parcel;
import android.speech.tts.TextToSpeech;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsConferenceState;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.SeempLog;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.SmsApplication;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Telephony {
    private static final String TAG = "Telephony";

    public interface BaseMmsColumns extends BaseColumns {
        @Deprecated
        public static final String ADAPTATION_ALLOWED = "adp_a";
        @Deprecated
        public static final String APPLIC_ID = "apl_id";
        @Deprecated
        public static final String AUX_APPLIC_ID = "aux_apl_id";
        @Deprecated
        public static final String CANCEL_ID = "cl_id";
        @Deprecated
        public static final String CANCEL_STATUS = "cl_st";
        public static final String CONTENT_CLASS = "ct_cls";
        public static final String CONTENT_LOCATION = "ct_l";
        public static final String CONTENT_TYPE = "ct_t";
        public static final String CREATOR = "creator";
        public static final String DATE = "date";
        public static final String DATE_SENT = "date_sent";
        public static final String DELIVERY_REPORT = "d_rpt";
        public static final String DELIVERY_TIME = "d_tm";
        @Deprecated
        public static final String DELIVERY_TIME_TOKEN = "d_tm_tok";
        @Deprecated
        public static final String DISTRIBUTION_INDICATOR = "d_ind";
        @Deprecated
        public static final String DRM_CONTENT = "drm_c";
        @Deprecated
        public static final String ELEMENT_DESCRIPTOR = "e_des";
        public static final String EXPIRY = "exp";
        @Deprecated
        public static final String LIMIT = "limit";
        public static final String LOCKED = "locked";
        @Deprecated
        public static final String MBOX_QUOTAS = "mb_qt";
        @Deprecated
        public static final String MBOX_QUOTAS_TOKEN = "mb_qt_tok";
        @Deprecated
        public static final String MBOX_TOTALS = "mb_t";
        @Deprecated
        public static final String MBOX_TOTALS_TOKEN = "mb_t_tok";
        public static final String MESSAGE_BOX = "msg_box";
        public static final int MESSAGE_BOX_ALL = 0;
        public static final int MESSAGE_BOX_DRAFTS = 3;
        public static final int MESSAGE_BOX_FAILED = 5;
        public static final int MESSAGE_BOX_INBOX = 1;
        public static final int MESSAGE_BOX_OUTBOX = 4;
        public static final int MESSAGE_BOX_SENT = 2;
        public static final String MESSAGE_CLASS = "m_cls";
        @Deprecated
        public static final String MESSAGE_COUNT = "m_cnt";
        public static final String MESSAGE_ID = "m_id";
        public static final String MESSAGE_SIZE = "m_size";
        public static final String MESSAGE_TYPE = "m_type";
        public static final String MMS_VERSION = "v";
        @Deprecated
        public static final String MM_FLAGS = "mm_flg";
        @Deprecated
        public static final String MM_FLAGS_TOKEN = "mm_flg_tok";
        @Deprecated
        public static final String MM_STATE = "mm_st";
        @Deprecated
        public static final String PREVIOUSLY_SENT_BY = "p_s_by";
        @Deprecated
        public static final String PREVIOUSLY_SENT_DATE = "p_s_d";
        public static final String PRIORITY = "pri";
        @Deprecated
        public static final String QUOTAS = "qt";
        public static final String READ = "read";
        public static final String READ_REPORT = "rr";
        public static final String READ_STATUS = "read_status";
        @Deprecated
        public static final String RECOMMENDED_RETRIEVAL_MODE = "r_r_mod";
        @Deprecated
        public static final String RECOMMENDED_RETRIEVAL_MODE_TEXT = "r_r_mod_txt";
        @Deprecated
        public static final String REPLACE_ID = "repl_id";
        @Deprecated
        public static final String REPLY_APPLIC_ID = "r_apl_id";
        @Deprecated
        public static final String REPLY_CHARGING = "r_chg";
        @Deprecated
        public static final String REPLY_CHARGING_DEADLINE = "r_chg_dl";
        @Deprecated
        public static final String REPLY_CHARGING_DEADLINE_TOKEN = "r_chg_dl_tok";
        @Deprecated
        public static final String REPLY_CHARGING_ID = "r_chg_id";
        @Deprecated
        public static final String REPLY_CHARGING_SIZE = "r_chg_sz";
        public static final String REPORT_ALLOWED = "rpt_a";
        public static final String RESPONSE_STATUS = "resp_st";
        public static final String RESPONSE_TEXT = "resp_txt";
        public static final String RETRIEVE_STATUS = "retr_st";
        public static final String RETRIEVE_TEXT = "retr_txt";
        public static final String RETRIEVE_TEXT_CHARSET = "retr_txt_cs";
        public static final String SEEN = "seen";
        @Deprecated
        public static final String SENDER_VISIBILITY = "s_vis";
        @Deprecated
        public static final String START = "start";
        public static final String STATUS = "st";
        @Deprecated
        public static final String STATUS_TEXT = "st_txt";
        @Deprecated
        public static final String STORE = "store";
        @Deprecated
        public static final String STORED = "stored";
        @Deprecated
        public static final String STORE_STATUS = "store_st";
        @Deprecated
        public static final String STORE_STATUS_TEXT = "store_st_txt";
        public static final String SUBJECT = "sub";
        public static final String SUBJECT_CHARSET = "sub_cs";
        public static final String SUBSCRIPTION_ID = "sub_id";
        public static final String TEXT_ONLY = "text_only";
        public static final String THREAD_ID = "thread_id";
        @Deprecated
        public static final String TOTALS = "totals";
        public static final String TRANSACTION_ID = "tr_id";
    }

    public interface CanonicalAddressesColumns extends BaseColumns {
        public static final String ADDRESS = "address";
    }

    public interface CarrierColumns extends BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://carrier_information/carrier");
        public static final String EXPIRATION_TIME = "expiration_time";
        public static final String KEY_IDENTIFIER = "key_identifier";
        public static final String KEY_TYPE = "key_type";
        public static final String LAST_MODIFIED = "last_modified";
        public static final String MCC = "mcc";
        public static final String MNC = "mnc";
        public static final String MVNO_MATCH_DATA = "mvno_match_data";
        public static final String MVNO_TYPE = "mvno_type";
        public static final String PUBLIC_KEY = "public_key";
    }

    public interface TextBasedSmsChangesColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://sms-changes");
        public static final String ID = "_id";
        public static final String NEW_READ_STATUS = "new_read_status";
        public static final String ORIG_ROW_ID = "orig_rowid";
        public static final String SUB_ID = "sub_id";
        public static final String TYPE = "type";
        public static final int TYPE_DELETE = 1;
        public static final int TYPE_UPDATE = 0;
    }

    public interface TextBasedSmsColumns {
        public static final String ADDRESS = "address";
        public static final String BODY = "body";
        public static final String CREATOR = "creator";
        public static final String DATE = "date";
        public static final String DATE_SENT = "date_sent";
        public static final String ERROR_CODE = "error_code";
        public static final String LOCKED = "locked";
        public static final int MESSAGE_TYPE_ALL = 0;
        public static final int MESSAGE_TYPE_DRAFT = 3;
        public static final int MESSAGE_TYPE_FAILED = 5;
        public static final int MESSAGE_TYPE_INBOX = 1;
        public static final int MESSAGE_TYPE_OUTBOX = 4;
        public static final int MESSAGE_TYPE_QUEUED = 6;
        public static final int MESSAGE_TYPE_SENT = 2;
        public static final String MTU = "mtu";
        public static final String PERSON = "person";
        public static final String PRIORITY = "priority";
        public static final String PROTOCOL = "protocol";
        public static final String READ = "read";
        public static final String REPLY_PATH_PRESENT = "reply_path_present";
        public static final String SEEN = "seen";
        public static final String SERVICE_CENTER = "service_center";
        public static final String STATUS = "status";
        public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_FAILED = 64;
        public static final int STATUS_NONE = -1;
        public static final int STATUS_PENDING = 32;
        public static final String SUBJECT = "subject";
        public static final String SUBSCRIPTION_ID = "sub_id";
        public static final String THREAD_ID = "thread_id";
        public static final String TYPE = "type";
    }

    public interface ThreadsColumns extends BaseColumns {
        public static final String ARCHIVED = "archived";
        public static final String ATTACHMENT_INFO = "attachment_info";
        public static final String DATE = "date";
        public static final String ERROR = "error";
        public static final String HAS_ATTACHMENT = "has_attachment";
        public static final String MESSAGE_COUNT = "message_count";
        public static final String NOTIFICATION = "notification";
        public static final String READ = "read";
        public static final String RECIPIENT_IDS = "recipient_ids";
        public static final String SNIPPET = "snippet";
        public static final String SNIPPET_CHARSET = "snippet_cs";
        public static final String TYPE = "type";
    }

    private Telephony() {
    }

    public static final class Sms implements BaseColumns, TextBasedSmsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://sms");
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        private Sms() {
        }

        public static String getDefaultSmsPackage(Context context) {
            ComponentName component = SmsApplication.getDefaultSmsApplication(context, false);
            if (component != null) {
                return component.getPackageName();
            }
            return null;
        }

        public static Cursor query(ContentResolver cr, String[] projection) {
            SeempLog.record(10);
            return cr.query(CONTENT_URI, projection, (String) null, (String[]) null, "date DESC");
        }

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public static Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
            String str;
            SeempLog.record(10);
            Uri uri = CONTENT_URI;
            if (orderBy == null) {
                str = "date DESC";
            } else {
                str = orderBy;
            }
            return cr.query(uri, projection, where, (String[]) null, str);
        }

        @UnsupportedAppUsage
        public static Uri addMessageToUri(ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport) {
            return addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, uri, address, body, subject, date, read, deliveryReport, -1);
        }

        @UnsupportedAppUsage
        public static Uri addMessageToUri(int subId, ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport) {
            return addMessageToUri(subId, resolver, uri, address, body, subject, date, read, deliveryReport, -1);
        }

        @UnsupportedAppUsage
        public static Uri addMessageToUri(ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport, long threadId) {
            return addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, uri, address, body, subject, date, read, deliveryReport, threadId);
        }

        @UnsupportedAppUsage
        public static Uri addMessageToUri(int subId, ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport, long threadId) {
            return addMessageToUri(subId, resolver, uri, address, body, subject, date, read, deliveryReport, threadId, -1);
        }

        public static Uri addMessageToUri(int subId, ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport, long threadId, int priority) {
            ContentValues values = new ContentValues(8);
            Rlog.v(Telephony.TAG, "Telephony addMessageToUri sub id: " + subId);
            values.put("sub_id", Integer.valueOf(subId));
            values.put("address", address);
            if (date != null) {
                values.put("date", date);
            }
            values.put("read", Integer.valueOf(read ? 1 : 0));
            values.put(TextBasedSmsColumns.SUBJECT, subject);
            values.put("body", body);
            values.put("priority", Integer.valueOf(priority));
            if (deliveryReport) {
                values.put("status", (Integer) 32);
            }
            if (threadId != -1) {
                values.put("thread_id", Long.valueOf(threadId));
            }
            return resolver.insert(uri, values);
        }

        @UnsupportedAppUsage
        public static boolean moveMessageToFolder(Context context, Uri uri, int folder, int error) {
            if (uri == null) {
                return false;
            }
            boolean markAsUnread = false;
            boolean markAsRead = false;
            switch (folder) {
                case 1:
                case 3:
                    break;
                case 2:
                case 4:
                    markAsRead = true;
                    break;
                case 5:
                case 6:
                    markAsUnread = true;
                    break;
                default:
                    return false;
            }
            ContentValues values = new ContentValues(3);
            values.put("type", Integer.valueOf(folder));
            if (markAsUnread) {
                values.put("read", (Integer) 0);
            } else if (markAsRead) {
                values.put("read", (Integer) 1);
            }
            values.put(TextBasedSmsColumns.ERROR_CODE, Integer.valueOf(error));
            if (1 == SqliteWrapper.update(context, context.getContentResolver(), uri, values, (String) null, (String[]) null)) {
                return true;
            }
            return false;
        }

        @UnsupportedAppUsage
        public static boolean isOutgoingFolder(int messageType) {
            return messageType == 5 || messageType == 4 || messageType == 2 || messageType == 6;
        }

        public static final class Inbox implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/inbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Inbox() {
            }

            @UnsupportedAppUsage
            public static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date, boolean read) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, read, false);
            }

            @UnsupportedAppUsage
            public static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date, boolean read) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, read, false);
            }
        }

        public static final class Sent implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/sent");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Sent() {
            }

            @UnsupportedAppUsage
            public static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, true, false);
            }

            @UnsupportedAppUsage
            public static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, true, false);
            }
        }

        public static final class Draft implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/draft");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Draft() {
            }

            @UnsupportedAppUsage
            public static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, true, false);
            }

            @UnsupportedAppUsage
            public static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, true, false);
            }
        }

        public static final class Outbox implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/outbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Outbox() {
            }

            @UnsupportedAppUsage
            public static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date, boolean deliveryReport, long threadId) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, true, deliveryReport, threadId);
            }

            public static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date, boolean deliveryReport, long threadId) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, true, deliveryReport, threadId);
            }
        }

        public static final class Conversations implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/conversations");
            public static final String DEFAULT_SORT_ORDER = "date DESC";
            public static final String MESSAGE_COUNT = "msg_count";
            public static final String SNIPPET = "snippet";

            private Conversations() {
            }
        }

        public static final class Intents {
            public static final String ACTION_CHANGE_DEFAULT = "android.provider.Telephony.ACTION_CHANGE_DEFAULT";
            public static final String ACTION_DEFAULT_SMS_PACKAGE_CHANGED = "android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED";
            public static final String ACTION_DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL = "android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL";
            public static final String ACTION_EXTERNAL_PROVIDER_CHANGE = "android.provider.action.EXTERNAL_PROVIDER_CHANGE";
            public static final String ACTION_SMS_MMS_DB_CREATED = "android.provider.action.SMS_MMS_DB_CREATED";
            public static final String ACTION_SMS_MMS_DB_LOST = "android.provider.action.SMS_MMS_DB_LOST";
            public static final String DATA_SMS_RECEIVED_ACTION = "android.intent.action.DATA_SMS_RECEIVED";
            public static final String EXTRA_IS_CORRUPTED = "android.provider.extra.IS_CORRUPTED";
            public static final String EXTRA_IS_DEFAULT_SMS_APP = "android.provider.extra.IS_DEFAULT_SMS_APP";
            public static final String EXTRA_IS_INITIAL_CREATE = "android.provider.extra.IS_INITIAL_CREATE";
            public static final String EXTRA_PACKAGE_NAME = "package";
            public static final String MMS_DOWNLOADED_ACTION = "android.provider.Telephony.MMS_DOWNLOADED";
            public static final int RESULT_SMS_DUPLICATED = 5;
            public static final int RESULT_SMS_GENERIC_ERROR = 2;
            public static final int RESULT_SMS_HANDLED = 1;
            public static final int RESULT_SMS_OUT_OF_MEMORY = 3;
            public static final int RESULT_SMS_UNSUPPORTED = 4;
            @Deprecated
            public static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
            public static final String SIM_FULL_ACTION = "android.provider.Telephony.SIM_FULL";
            public static final String SMS_CARRIER_PROVISION_ACTION = "android.provider.Telephony.SMS_CARRIER_PROVISION";
            public static final String SMS_CB_RECEIVED_ACTION = "android.provider.Telephony.SMS_CB_RECEIVED";
            public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";
            public static final String SMS_EMERGENCY_CB_RECEIVED_ACTION = "android.provider.Telephony.SMS_EMERGENCY_CB_RECEIVED";
            public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
            public static final String SMS_REJECTED_ACTION = "android.provider.Telephony.SMS_REJECTED";
            public static final String SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED_ACTION = "android.provider.Telephony.SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED";
            public static final String WAP_PUSH_DELIVER_ACTION = "android.provider.Telephony.WAP_PUSH_DELIVER";
            public static final String WAP_PUSH_RECEIVED_ACTION = "android.provider.Telephony.WAP_PUSH_RECEIVED";

            private Intents() {
            }

            public static SmsMessage[] getMessagesFromIntent(Intent intent) {
                try {
                    Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
                    if (messages == null) {
                        Rlog.e(Telephony.TAG, "pdus does not exist in the intent");
                        return null;
                    }
                    String format = intent.getStringExtra("format");
                    int subId = intent.getIntExtra(PhoneConstants.SUBSCRIPTION_KEY, SubscriptionManager.getDefaultSmsSubscriptionId());
                    Rlog.v(Telephony.TAG, " getMessagesFromIntent sub_id : " + subId);
                    int pduCount = messages.length;
                    SmsMessage[] msgs = new SmsMessage[pduCount];
                    for (int i = 0; i < pduCount; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) messages[i], format);
                        if (msgs[i] != null) {
                            msgs[i].setSubId(subId);
                        }
                    }
                    return msgs;
                } catch (ClassCastException e) {
                    Rlog.e(Telephony.TAG, "getMessagesFromIntent: " + e);
                    return null;
                }
            }
        }
    }

    public static final class Threads implements ThreadsColumns {
        public static final int BROADCAST_THREAD = 1;
        public static final int COMMON_THREAD = 0;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(MmsSms.CONTENT_URI, "conversations");
        @UnsupportedAppUsage
        private static final String[] ID_PROJECTION = {"_id"};
        public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(CONTENT_URI, "obsolete");
        @UnsupportedAppUsage
        private static final Uri THREAD_ID_CONTENT_URI = Uri.parse("content://mms-sms/threadID");

        private Threads() {
        }

        public static long getOrCreateThreadId(Context context, String recipient) {
            Set<String> recipients = new HashSet<>();
            recipients.add(recipient);
            return getOrCreateThreadId(context, recipients);
        }

        public static long getOrCreateThreadId(Context context, Set<String> recipients) {
            Uri.Builder uriBuilder = THREAD_ID_CONTENT_URI.buildUpon();
            for (String recipient : recipients) {
                if (Mms.isEmailAddress(recipient)) {
                    recipient = Mms.extractAddrSpec(recipient);
                }
                uriBuilder.appendQueryParameter("recipient", recipient);
            }
            Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(), uriBuilder.build(), ID_PROJECTION, (String) null, (String[]) null, (String) null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        return cursor.getLong(0);
                    }
                    Rlog.e(Telephony.TAG, "getOrCreateThreadId returned no rows!");
                    cursor.close();
                } finally {
                    cursor.close();
                }
            }
            Rlog.e(Telephony.TAG, "getOrCreateThreadId failed with " + recipients.size() + " recipients");
            throw new IllegalArgumentException("Unable to find or allocate a thread ID.");
        }
    }

    public interface RcsColumns {
        public static final String AUTHORITY = "rcs";
        public static final Uri CONTENT_AND_AUTHORITY = Uri.parse("content://rcs");
        public static final boolean IS_RCS_TABLE_SCHEMA_CODE_COMPLETE = false;
        public static final long TIMESTAMP_NOT_SET = 0;
        public static final int TRANSACTION_FAILED = Integer.MIN_VALUE;

        public interface Rcs1To1ThreadColumns extends RcsThreadColumns {
            public static final String FALLBACK_THREAD_ID_COLUMN = "rcs_fallback_thread_id";
            public static final Uri RCS_1_TO_1_THREAD_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, RCS_1_TO_1_THREAD_URI_PART);
            public static final String RCS_1_TO_1_THREAD_URI_PART = "p2p_thread";
        }

        public interface RcsEventTypes {
            public static final int ICON_CHANGED_EVENT_TYPE = 8;
            public static final int NAME_CHANGED_EVENT_TYPE = 16;
            public static final int PARTICIPANT_ALIAS_CHANGED_EVENT_TYPE = 1;
            public static final int PARTICIPANT_JOINED_EVENT_TYPE = 2;
            public static final int PARTICIPANT_LEFT_EVENT_TYPE = 4;
        }

        public interface RcsFileTransferColumns {
            public static final String CONTENT_TYPE_COLUMN = "content_type";
            public static final String CONTENT_URI_COLUMN = "content_uri";
            public static final String DURATION_MILLIS_COLUMN = "duration";
            public static final String FILE_SIZE_COLUMN = "file_size";
            public static final String FILE_TRANSFER_ID_COLUMN = "rcs_file_transfer_id";
            public static final Uri FILE_TRANSFER_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, FILE_TRANSFER_URI_PART);
            public static final String FILE_TRANSFER_URI_PART = "file_transfer";
            public static final String HEIGHT_COLUMN = "height";
            public static final String PREVIEW_TYPE_COLUMN = "preview_type";
            public static final String PREVIEW_URI_COLUMN = "preview_uri";
            public static final String SESSION_ID_COLUMN = "session_id";
            public static final String SUCCESSFULLY_TRANSFERRED_BYTES = "transfer_offset";
            public static final String TRANSFER_STATUS_COLUMN = "transfer_status";
            public static final String WIDTH_COLUMN = "width";
        }

        public interface RcsGroupThreadColumns extends RcsThreadColumns {
            public static final String CONFERENCE_URI_COLUMN = "conference_uri";
            public static final String GROUP_ICON_COLUMN = "group_icon";
            public static final String GROUP_NAME_COLUMN = "group_name";
            public static final String OWNER_PARTICIPANT_COLUMN = "owner_participant";
            public static final Uri RCS_GROUP_THREAD_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, RCS_GROUP_THREAD_URI_PART);
            public static final String RCS_GROUP_THREAD_URI_PART = "group_thread";
        }

        public interface RcsIncomingMessageColumns extends RcsMessageColumns {
            public static final String ARRIVAL_TIMESTAMP_COLUMN = "arrival_timestamp";
            public static final Uri INCOMING_MESSAGE_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, INCOMING_MESSAGE_URI_PART);
            public static final String INCOMING_MESSAGE_URI_PART = "incoming_message";
            public static final String SEEN_TIMESTAMP_COLUMN = "seen_timestamp";
            public static final String SENDER_PARTICIPANT_ID_COLUMN = "sender_participant";
        }

        public interface RcsMessageColumns {
            public static final String GLOBAL_ID_COLUMN = "rcs_message_global_id";
            public static final String LATITUDE_COLUMN = "latitude";
            public static final String LONGITUDE_COLUMN = "longitude";
            public static final String MESSAGE_ID_COLUMN = "rcs_message_row_id";
            public static final String MESSAGE_TEXT_COLUMN = "rcs_text";
            public static final String MESSAGE_TYPE_COLUMN = "rcs_message_type";
            public static final String ORIGINATION_TIMESTAMP_COLUMN = "origination_timestamp";
            public static final String STATUS_COLUMN = "status";
            public static final String SUB_ID_COLUMN = "sub_id";
        }

        public interface RcsMessageDeliveryColumns extends RcsOutgoingMessageColumns {
            public static final String DELIVERED_TIMESTAMP_COLUMN = "delivered_timestamp";
            public static final String DELIVERY_URI_PART = "delivery";
            public static final String SEEN_TIMESTAMP_COLUMN = "seen_timestamp";
        }

        public interface RcsOutgoingMessageColumns extends RcsMessageColumns {
            public static final Uri OUTGOING_MESSAGE_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, OUTGOING_MESSAGE_URI_PART);
            public static final String OUTGOING_MESSAGE_URI_PART = "outgoing_message";
        }

        public interface RcsParticipantColumns {
            public static final String CANONICAL_ADDRESS_ID_COLUMN = "canonical_address_id";
            public static final String RCS_ALIAS_COLUMN = "rcs_alias";
            public static final String RCS_PARTICIPANT_ID_COLUMN = "rcs_participant_id";
            public static final Uri RCS_PARTICIPANT_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, RCS_PARTICIPANT_URI_PART);
            public static final String RCS_PARTICIPANT_URI_PART = "participant";
        }

        public interface RcsParticipantEventColumns {
            public static final String ALIAS_CHANGE_EVENT_URI_PART = "alias_change_event";
            public static final String NEW_ALIAS_COLUMN = "new_alias";
        }

        public interface RcsParticipantHelpers extends RcsParticipantColumns {
            public static final String RCS_PARTICIPANT_WITH_ADDRESS_VIEW = "rcs_participant_with_address_view";
            public static final String RCS_PARTICIPANT_WITH_THREAD_VIEW = "rcs_participant_with_thread_view";
        }

        public interface RcsThreadColumns {
            public static final String RCS_THREAD_ID_COLUMN = "rcs_thread_id";
            public static final Uri RCS_THREAD_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, RCS_THREAD_URI_PART);
            public static final String RCS_THREAD_URI_PART = "thread";
        }

        public interface RcsThreadEventColumns {
            public static final String DESTINATION_PARTICIPANT_ID_COLUMN = "destination_participant";
            public static final String EVENT_ID_COLUMN = "event_id";
            public static final String EVENT_TYPE_COLUMN = "event_type";
            public static final String ICON_CHANGED_URI_PART = "icon_changed_event";
            public static final String NAME_CHANGED_URI_PART = "name_changed_event";
            public static final String NEW_ICON_URI_COLUMN = "new_icon_uri";
            public static final String NEW_NAME_COLUMN = "new_name";
            public static final String PARTICIPANT_JOINED_URI_PART = "participant_joined_event";
            public static final String PARTICIPANT_LEFT_URI_PART = "participant_left_event";
            public static final String SOURCE_PARTICIPANT_ID_COLUMN = "source_participant";
            public static final String TIMESTAMP_COLUMN = "origination_timestamp";
        }

        public interface RcsUnifiedEventHelper extends RcsParticipantEventColumns, RcsThreadEventColumns {
            public static final Uri RCS_EVENT_QUERY_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, "event");
            public static final String RCS_EVENT_QUERY_URI_PATH = "event";
        }

        public interface RcsUnifiedMessageColumns extends RcsIncomingMessageColumns, RcsOutgoingMessageColumns {
            public static final String MESSAGE_TYPE_COLUMN = "message_type";
            public static final int MESSAGE_TYPE_INCOMING = 1;
            public static final int MESSAGE_TYPE_OUTGOING = 0;
            public static final String UNIFIED_INCOMING_MESSAGE_VIEW = "unified_incoming_message_view";
            public static final Uri UNIFIED_MESSAGE_URI = Uri.withAppendedPath(RcsColumns.CONTENT_AND_AUTHORITY, "message");
            public static final String UNIFIED_MESSAGE_URI_PART = "message";
            public static final String UNIFIED_OUTGOING_MESSAGE_VIEW = "unified_outgoing_message_view";
        }

        public interface RcsUnifiedThreadColumns extends RcsThreadColumns, Rcs1To1ThreadColumns, RcsGroupThreadColumns {
            public static final int THREAD_TYPE_1_TO_1 = 0;
            public static final String THREAD_TYPE_COLUMN = "thread_type";
            public static final int THREAD_TYPE_GROUP = 1;
        }

        public interface RcsCanonicalAddressHelper {
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x0051, code lost:
                if (r3 != null) goto L_0x0053;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:21:0x0053, code lost:
                if (r2 != null) goto L_0x0055;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
                r3.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
                r5 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:0x005a, code lost:
                r2.addSuppressed(r5);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:0x005e, code lost:
                r3.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:9:0x0031, code lost:
                r4 = move-exception;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            static long getOrCreateCanonicalAddressId(android.content.ContentResolver r6, java.lang.String r7) {
                /*
                    android.net.Uri r0 = android.provider.Telephony.RcsColumns.CONTENT_AND_AUTHORITY
                    android.net.Uri$Builder r0 = r0.buildUpon()
                    java.lang.String r1 = "canonical-address"
                    r0.appendPath(r1)
                    java.lang.String r1 = "address"
                    r0.appendQueryParameter(r1, r7)
                    android.net.Uri r1 = r0.build()
                    r2 = 0
                    android.database.Cursor r3 = r6.query(r1, r2, r2, r2)
                    if (r3 == 0) goto L_0x0035
                    boolean r4 = r3.moveToFirst()     // Catch:{ Throwable -> 0x0033 }
                    if (r4 == 0) goto L_0x0035
                    java.lang.String r4 = "_id"
                    int r4 = r3.getColumnIndex(r4)     // Catch:{ Throwable -> 0x0033 }
                    long r4 = r3.getLong(r4)     // Catch:{ Throwable -> 0x0033 }
                    if (r3 == 0) goto L_0x0030
                    r3.close()
                L_0x0030:
                    return r4
                L_0x0031:
                    r4 = move-exception
                    goto L_0x0051
                L_0x0033:
                    r2 = move-exception
                    goto L_0x0050
                L_0x0035:
                    java.lang.String r4 = "Telephony"
                    java.lang.String r5 = "getOrCreateCanonicalAddressId returned no rows"
                    android.telephony.Rlog.e(r4, r5)     // Catch:{ Throwable -> 0x0033 }
                    if (r3 == 0) goto L_0x0041
                    r3.close()
                L_0x0041:
                    java.lang.String r2 = "Telephony"
                    java.lang.String r3 = "getOrCreateCanonicalAddressId failed"
                    android.telephony.Rlog.e(r2, r3)
                    java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
                    java.lang.String r3 = "Unable to find or allocate a canonical address ID"
                    r2.<init>(r3)
                    throw r2
                L_0x0050:
                    throw r2     // Catch:{ all -> 0x0031 }
                L_0x0051:
                    if (r3 == 0) goto L_0x0061
                    if (r2 == 0) goto L_0x005e
                    r3.close()     // Catch:{ Throwable -> 0x0059 }
                    goto L_0x0061
                L_0x0059:
                    r5 = move-exception
                    r2.addSuppressed(r5)
                    goto L_0x0061
                L_0x005e:
                    r3.close()
                L_0x0061:
                    throw r4
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.Telephony.RcsColumns.RcsCanonicalAddressHelper.getOrCreateCanonicalAddressId(android.content.ContentResolver, java.lang.String):long");
            }
        }
    }

    public static final class Mms implements BaseMmsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://mms");
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        @UnsupportedAppUsage
        public static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern.compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");
        public static final Uri REPORT_REQUEST_URI = Uri.withAppendedPath(CONTENT_URI, "report-request");
        public static final Uri REPORT_STATUS_URI = Uri.withAppendedPath(CONTENT_URI, "report-status");

        private Mms() {
        }

        public static Cursor query(ContentResolver cr, String[] projection) {
            SeempLog.record(10);
            return cr.query(CONTENT_URI, projection, (String) null, (String[]) null, "date DESC");
        }

        public static Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
            String str;
            SeempLog.record(10);
            Uri uri = CONTENT_URI;
            if (orderBy == null) {
                str = "date DESC";
            } else {
                str = orderBy;
            }
            return cr.query(uri, projection, where, (String[]) null, str);
        }

        @UnsupportedAppUsage
        public static String extractAddrSpec(String address) {
            Matcher match = NAME_ADDR_EMAIL_PATTERN.matcher(address);
            if (match.matches()) {
                return match.group(2);
            }
            return address;
        }

        @UnsupportedAppUsage
        public static boolean isEmailAddress(String address) {
            if (TextUtils.isEmpty(address)) {
                return false;
            }
            return Patterns.EMAIL_ADDRESS.matcher(extractAddrSpec(address)).matches();
        }

        @UnsupportedAppUsage
        public static boolean isPhoneNumber(String number) {
            if (TextUtils.isEmpty(number)) {
                return false;
            }
            return Patterns.PHONE.matcher(number).matches();
        }

        public static final class Inbox implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/inbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Inbox() {
            }
        }

        public static final class Sent implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/sent");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Sent() {
            }
        }

        public static final class Draft implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/drafts");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Draft() {
            }
        }

        public static final class Outbox implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/outbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private Outbox() {
            }
        }

        public static final class Addr implements BaseColumns {
            public static final String ADDRESS = "address";
            public static final String CHARSET = "charset";
            public static final String CONTACT_ID = "contact_id";
            public static final String MSG_ID = "msg_id";
            public static final String TYPE = "type";

            private Addr() {
            }
        }

        public static final class Part implements BaseColumns {
            public static final String CHARSET = "chset";
            public static final String CONTENT_DISPOSITION = "cd";
            public static final String CONTENT_ID = "cid";
            public static final String CONTENT_LOCATION = "cl";
            public static final String CONTENT_TYPE = "ct";
            public static final Uri CONTENT_URI = Uri.withAppendedPath(Mms.CONTENT_URI, "part");
            public static final String CT_START = "ctt_s";
            public static final String CT_TYPE = "ctt_t";
            public static final String FILENAME = "fn";
            public static final String MSG_ID = "mid";
            public static final String NAME = "name";
            public static final String SEQ = "seq";
            public static final String TEXT = "text";
            public static final String _DATA = "_data";

            private Part() {
            }
        }

        public static final class Rate {
            public static final Uri CONTENT_URI = Uri.withAppendedPath(Mms.CONTENT_URI, TextToSpeech.Engine.KEY_PARAM_RATE);
            public static final String SENT_TIME = "sent_time";

            private Rate() {
            }
        }

        public static final class Intents {
            public static final String CONTENT_CHANGED_ACTION = "android.intent.action.CONTENT_CHANGED";
            public static final String DELETED_CONTENTS = "deleted_contents";

            private Intents() {
            }
        }
    }

    public static final class MmsSms implements BaseColumns {
        public static final Uri CONTENT_CONVERSATIONS_URI = Uri.parse("content://mms-sms/conversations");
        public static final Uri CONTENT_DRAFT_URI = Uri.parse("content://mms-sms/draft");
        public static final Uri CONTENT_FILTER_BYPHONE_URI = Uri.parse("content://mms-sms/messages/byphone");
        public static final Uri CONTENT_LOCKED_URI = Uri.parse("content://mms-sms/locked");
        public static final Uri CONTENT_UNDELIVERED_URI = Uri.parse("content://mms-sms/undelivered");
        public static final Uri CONTENT_URI = Uri.parse("content://mms-sms/");
        public static final int ERR_TYPE_GENERIC = 1;
        public static final int ERR_TYPE_GENERIC_PERMANENT = 10;
        public static final int ERR_TYPE_MMS_PROTO_PERMANENT = 12;
        public static final int ERR_TYPE_MMS_PROTO_TRANSIENT = 3;
        public static final int ERR_TYPE_SMS_PROTO_PERMANENT = 11;
        public static final int ERR_TYPE_SMS_PROTO_TRANSIENT = 2;
        public static final int ERR_TYPE_TRANSPORT_FAILURE = 4;
        public static final int MMS_PROTO = 1;
        public static final int NO_ERROR = 0;
        public static final Uri SEARCH_URI = Uri.parse("content://mms-sms/search");
        public static final int SMS_PROTO = 0;
        public static final String TYPE_DISCRIMINATOR_COLUMN = "transport_type";

        private MmsSms() {
        }

        public static final class PendingMessages implements BaseColumns {
            public static final Uri CONTENT_URI = Uri.withAppendedPath(MmsSms.CONTENT_URI, ImsConferenceState.STATUS_PENDING);
            public static final String DUE_TIME = "due_time";
            public static final String ERROR_CODE = "err_code";
            public static final String ERROR_TYPE = "err_type";
            public static final String LAST_TRY = "last_try";
            public static final String MSG_ID = "msg_id";
            public static final String MSG_TYPE = "msg_type";
            public static final String PROTO_TYPE = "proto_type";
            public static final String RETRY_INDEX = "retry_index";
            public static final String SUBSCRIPTION_ID = "pending_sub_id";

            private PendingMessages() {
            }
        }

        public static final class WordsTable {
            public static final String ID = "_id";
            public static final String INDEXED_TEXT = "index_text";
            public static final String SOURCE_ROW_ID = "source_id";
            public static final String TABLE_ID = "table_to_use";

            private WordsTable() {
            }
        }
    }

    public static final class Carriers implements BaseColumns {
        public static final String APN = "apn";
        @SystemApi
        public static final String APN_SET_ID = "apn_set_id";
        public static final String AUTH_TYPE = "authtype";
        @Deprecated
        public static final String BEARER = "bearer";
        @Deprecated
        public static final String BEARER_BITMASK = "bearer_bitmask";
        public static final int CARRIER_DELETED = 5;
        public static final int CARRIER_DELETED_BUT_PRESENT_IN_XML = 6;
        @SystemApi
        public static final int CARRIER_EDITED = 4;
        public static final String CARRIER_ENABLED = "carrier_enabled";
        public static final String CARRIER_ID = "carrier_id";
        public static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
        public static final String CURRENT = "current";
        public static final String DEFAULT_SORT_ORDER = "name ASC";
        public static final Uri DPC_URI = Uri.parse("content://telephony/carriers/dpc");
        @SystemApi
        public static final String EDITED_STATUS = "edited";
        public static final String ENFORCE_KEY = "enforced";
        public static final Uri ENFORCE_MANAGED_URI = Uri.parse("content://telephony/carriers/enforce_managed");
        public static final Uri FILTERED_URI = Uri.parse("content://telephony/carriers/filtered");
        @SystemApi
        public static final String MAX_CONNECTIONS = "max_conns";
        public static final String MCC = "mcc";
        public static final String MMSC = "mmsc";
        public static final String MMSPORT = "mmsport";
        public static final String MMSPROXY = "mmsproxy";
        public static final String MNC = "mnc";
        @SystemApi
        public static final String MODEM_PERSIST = "modem_cognitive";
        @SystemApi
        public static final String MTU = "mtu";
        public static final String MVNO_MATCH_DATA = "mvno_match_data";
        public static final String MVNO_TYPE = "mvno_type";
        public static final String NAME = "name";
        public static final String NETWORK_TYPE_BITMASK = "network_type_bitmask";
        @SystemApi
        public static final int NO_APN_SET_ID = 0;
        public static final String NUMERIC = "numeric";
        public static final String OWNED_BY = "owned_by";
        public static final int OWNED_BY_DPC = 0;
        public static final int OWNED_BY_OTHERS = 1;
        public static final String PASSWORD = "password";
        public static final String PORT = "port";
        public static final String PROFILE_ID = "profile_id";
        public static final String PROTOCOL = "protocol";
        public static final String PROXY = "proxy";
        public static final String ROAMING_PROTOCOL = "roaming_protocol";
        public static final String SERVER = "server";
        public static final Uri SIM_APN_URI = Uri.parse("content://telephony/carriers/sim_apn_list");
        public static final String SKIP_464XLAT = "skip_464xlat";
        public static final int SKIP_464XLAT_DEFAULT = -1;
        public static final int SKIP_464XLAT_DISABLE = 0;
        public static final int SKIP_464XLAT_ENABLE = 1;
        public static final String SUBSCRIPTION_ID = "sub_id";
        @SystemApi
        public static final String TIME_LIMIT_FOR_MAX_CONNECTIONS = "max_conns_time";
        public static final String TYPE = "type";
        @SystemApi
        public static final int UNEDITED = 0;
        public static final String USER = "user";
        @SystemApi
        public static final int USER_DELETED = 2;
        public static final int USER_DELETED_BUT_PRESENT_IN_XML = 3;
        @SystemApi
        public static final String USER_EDITABLE = "user_editable";
        @SystemApi
        public static final int USER_EDITED = 1;
        @SystemApi
        public static final String USER_VISIBLE = "user_visible";
        @SystemApi
        public static final String WAIT_TIME_RETRY = "wait_time";

        @Retention(RetentionPolicy.SOURCE)
        public @interface EditStatus {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface Skip464XlatStatus {
        }

        private Carriers() {
        }
    }

    public static final class CellBroadcasts implements BaseColumns {
        public static final String CID = "cid";
        public static final String CMAS_CATEGORY = "cmas_category";
        public static final String CMAS_CERTAINTY = "cmas_certainty";
        public static final String CMAS_MESSAGE_CLASS = "cmas_message_class";
        public static final String CMAS_RESPONSE_TYPE = "cmas_response_type";
        public static final String CMAS_SEVERITY = "cmas_severity";
        public static final String CMAS_URGENCY = "cmas_urgency";
        public static final Uri CONTENT_URI = Uri.parse("content://cellbroadcasts");
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        public static final String DELIVERY_TIME = "date";
        public static final String ETWS_WARNING_TYPE = "etws_warning_type";
        public static final String GEOGRAPHICAL_SCOPE = "geo_scope";
        public static final String LAC = "lac";
        public static final String LANGUAGE_CODE = "language";
        public static final String MESSAGE_BODY = "body";
        public static final String MESSAGE_FORMAT = "format";
        public static final String MESSAGE_PRIORITY = "priority";
        public static final String MESSAGE_READ = "read";
        public static final String PLMN = "plmn";
        public static final String[] QUERY_COLUMNS = {"_id", GEOGRAPHICAL_SCOPE, "plmn", LAC, "cid", "serial_number", SERVICE_CATEGORY, "language", "body", "date", "read", "format", "priority", ETWS_WARNING_TYPE, CMAS_MESSAGE_CLASS, CMAS_CATEGORY, CMAS_RESPONSE_TYPE, CMAS_SEVERITY, CMAS_URGENCY, CMAS_CERTAINTY};
        public static final String SERIAL_NUMBER = "serial_number";
        public static final String SERVICE_CATEGORY = "service_category";
        public static final String V1_MESSAGE_CODE = "message_code";
        public static final String V1_MESSAGE_IDENTIFIER = "message_id";

        private CellBroadcasts() {
        }
    }

    public static final class ServiceStateTable {
        public static final String AUTHORITY = "service-state";
        public static final String CDMA_DEFAULT_ROAMING_INDICATOR = "cdma_default_roaming_indicator";
        public static final String CDMA_ERI_ICON_INDEX = "cdma_eri_icon_index";
        public static final String CDMA_ERI_ICON_MODE = "cdma_eri_icon_mode";
        public static final String CDMA_ROAMING_INDICATOR = "cdma_roaming_indicator";
        public static final Uri CONTENT_URI = Uri.parse("content://service-state/");
        public static final String CSS_INDICATOR = "css_indicator";
        public static final String DATA_OPERATOR_ALPHA_LONG = "data_operator_alpha_long";
        public static final String DATA_OPERATOR_ALPHA_SHORT = "data_operator_alpha_short";
        public static final String DATA_OPERATOR_NUMERIC = "data_operator_numeric";
        public static final String DATA_REG_STATE = "data_reg_state";
        public static final String DATA_ROAMING_TYPE = "data_roaming_type";
        public static final String IS_DATA_ROAMING_FROM_REGISTRATION = "is_data_roaming_from_registration";
        public static final String IS_EMERGENCY_ONLY = "is_emergency_only";
        public static final String IS_MANUAL_NETWORK_SELECTION = "is_manual_network_selection";
        public static final String IS_USING_CARRIER_AGGREGATION = "is_using_carrier_aggregation";
        public static final String NETWORK_ID = "network_id";
        public static final String OPERATOR_ALPHA_LONG_RAW = "operator_alpha_long_raw";
        public static final String OPERATOR_ALPHA_SHORT_RAW = "operator_alpha_short_raw";
        public static final String RIL_DATA_RADIO_TECHNOLOGY = "ril_data_radio_technology";
        public static final String RIL_VOICE_RADIO_TECHNOLOGY = "ril_voice_radio_technology";
        public static final String SERVICE_STATE = "service_state";
        public static final String SYSTEM_ID = "system_id";
        public static final String VOICE_OPERATOR_ALPHA_LONG = "voice_operator_alpha_long";
        public static final String VOICE_OPERATOR_ALPHA_SHORT = "voice_operator_alpha_short";
        public static final String VOICE_OPERATOR_NUMERIC = "voice_operator_numeric";
        public static final String VOICE_REG_STATE = "voice_reg_state";
        public static final String VOICE_ROAMING_TYPE = "voice_roaming_type";

        private ServiceStateTable() {
        }

        public static Uri getUriForSubscriptionIdAndField(int subscriptionId, String field) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(subscriptionId)).appendEncodedPath(field).build();
        }

        public static Uri getUriForSubscriptionId(int subscriptionId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(subscriptionId)).build();
        }

        public static ContentValues getContentValuesForServiceState(ServiceState state) {
            ContentValues values = new ContentValues();
            Parcel p = Parcel.obtain();
            state.writeToParcel(p, 0);
            values.put(SERVICE_STATE, p.marshall());
            return values;
        }
    }

    public static final class CarrierId implements BaseColumns {
        public static final String AUTHORITY = "carrier_id";
        public static final String CARRIER_ID = "carrier_id";
        public static final String CARRIER_NAME = "carrier_name";
        public static final Uri CONTENT_URI = Uri.parse("content://carrier_id");
        public static final String PARENT_CARRIER_ID = "parent_carrier_id";
        public static final String SPECIFIC_CARRIER_ID = "specific_carrier_id";
        public static final String SPECIFIC_CARRIER_ID_NAME = "specific_carrier_id_name";

        public static final class All implements BaseColumns {
            public static final String APN = "apn";
            public static final Uri CONTENT_URI = Uri.parse("content://carrier_id/all");
            public static final String GID1 = "gid1";
            public static final String GID2 = "gid2";
            public static final String ICCID_PREFIX = "iccid_prefix";
            public static final String IMSI_PREFIX_XPATTERN = "imsi_prefix_xpattern";
            public static final String MCCMNC = "mccmnc";
            public static final String PLMN = "plmn";
            public static final String PRIVILEGE_ACCESS_RULE = "privilege_access_rule";
            public static final String SPN = "spn";
        }

        private CarrierId() {
        }

        public static Uri getUriForSubscriptionId(int subscriptionId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(subscriptionId)).build();
        }

        public static Uri getSpecificCarrierIdUriForSubscriptionId(int subscriptionId) {
            return Uri.withAppendedPath(Uri.withAppendedPath(CONTENT_URI, "specific"), String.valueOf(subscriptionId));
        }
    }
}
