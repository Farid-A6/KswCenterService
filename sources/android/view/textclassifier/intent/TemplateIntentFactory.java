package android.view.textclassifier.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.textclassifier.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.google.android.textclassifier.NamedVariant;
import com.google.android.textclassifier.RemoteActionTemplate;
import java.util.ArrayList;
import java.util.List;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class TemplateIntentFactory {
    private static final String TAG = "androidtc";

    public List<LabeledIntent> create(RemoteActionTemplate[] remoteActionTemplates) {
        int i;
        if (remoteActionTemplates.length == 0) {
            return new ArrayList();
        }
        List<LabeledIntent> labeledIntents = new ArrayList<>();
        for (RemoteActionTemplate remoteActionTemplate : remoteActionTemplates) {
            if (!isValidTemplate(remoteActionTemplate)) {
                Log.w("androidtc", "Invalid RemoteActionTemplate skipped.");
            } else {
                String str = remoteActionTemplate.titleWithoutEntity;
                String str2 = remoteActionTemplate.titleWithEntity;
                String str3 = remoteActionTemplate.description;
                String str4 = remoteActionTemplate.descriptionWithAppName;
                Intent createIntent = createIntent(remoteActionTemplate);
                if (remoteActionTemplate.requestCode == null) {
                    i = 0;
                } else {
                    i = remoteActionTemplate.requestCode.intValue();
                }
                labeledIntents.add(new LabeledIntent(str, str2, str3, str4, createIntent, i));
            }
        }
        return labeledIntents;
    }

    private static boolean isValidTemplate(RemoteActionTemplate remoteActionTemplate) {
        if (remoteActionTemplate == null) {
            Log.w("androidtc", "Invalid RemoteActionTemplate: is null");
            return false;
        } else if (TextUtils.isEmpty(remoteActionTemplate.titleWithEntity) && TextUtils.isEmpty(remoteActionTemplate.titleWithoutEntity)) {
            Log.w("androidtc", "Invalid RemoteActionTemplate: title is null");
            return false;
        } else if (TextUtils.isEmpty(remoteActionTemplate.description)) {
            Log.w("androidtc", "Invalid RemoteActionTemplate: description is null");
            return false;
        } else if (!TextUtils.isEmpty(remoteActionTemplate.packageName)) {
            Log.w("androidtc", "Invalid RemoteActionTemplate: package name is set");
            return false;
        } else if (!TextUtils.isEmpty(remoteActionTemplate.action)) {
            return true;
        } else {
            Log.w("androidtc", "Invalid RemoteActionTemplate: intent action not set");
            return false;
        }
    }

    private static Intent createIntent(RemoteActionTemplate remoteActionTemplate) {
        Intent intent = new Intent(remoteActionTemplate.action);
        String type = null;
        Uri uri = TextUtils.isEmpty(remoteActionTemplate.data) ? null : Uri.parse(remoteActionTemplate.data).normalizeScheme();
        if (!TextUtils.isEmpty(remoteActionTemplate.type)) {
            type = Intent.normalizeMimeType(remoteActionTemplate.type);
        }
        intent.setDataAndType(uri, type);
        intent.setFlags(remoteActionTemplate.flags == null ? 0 : remoteActionTemplate.flags.intValue());
        if (remoteActionTemplate.category != null) {
            for (String category : remoteActionTemplate.category) {
                if (category != null) {
                    intent.addCategory(category);
                }
            }
        }
        intent.putExtras(nameVariantsToBundle(remoteActionTemplate.extras));
        return intent;
    }

    public static Bundle nameVariantsToBundle(NamedVariant[] namedVariants) {
        if (namedVariants == null) {
            return Bundle.EMPTY;
        }
        Bundle bundle = new Bundle();
        for (NamedVariant namedVariant : namedVariants) {
            if (namedVariant != null) {
                switch (namedVariant.getType()) {
                    case 1:
                        bundle.putInt(namedVariant.getName(), namedVariant.getInt());
                        break;
                    case 2:
                        bundle.putLong(namedVariant.getName(), namedVariant.getLong());
                        break;
                    case 3:
                        bundle.putFloat(namedVariant.getName(), namedVariant.getFloat());
                        break;
                    case 4:
                        bundle.putDouble(namedVariant.getName(), namedVariant.getDouble());
                        break;
                    case 5:
                        bundle.putBoolean(namedVariant.getName(), namedVariant.getBool());
                        break;
                    case 6:
                        bundle.putString(namedVariant.getName(), namedVariant.getString());
                        break;
                    default:
                        Log.w("androidtc", "Unsupported type found in nameVariantsToBundle : " + namedVariant.getType());
                        break;
                }
            }
        }
        return bundle;
    }
}
