package android.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.security.KeyChain;
import android.util.AttributeSet;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AliasActivity extends Activity {
    public final String ALIAS_META_DATA = "android.app.alias";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XmlResourceParser parser = null;
        try {
            parser = getPackageManager().getActivityInfo(getComponentName(), 128).loadXmlMetaData(getPackageManager(), "android.app.alias");
            if (parser != null) {
                Intent intent = parseAlias(parser);
                if (intent != null) {
                    startActivity(intent);
                    finish();
                    if (parser != null) {
                        parser.close();
                        return;
                    }
                    return;
                }
                throw new RuntimeException("No <intent> tag found in alias description");
            }
            throw new RuntimeException("Alias requires a meta-data field android.app.alias");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Error parsing alias", e);
        } catch (XmlPullParserException e2) {
            throw new RuntimeException("Error parsing alias", e2);
        } catch (IOException e3) {
            throw new RuntimeException("Error parsing alias", e3);
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
            throw th;
        }
    }

    private Intent parseAlias(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        Intent intent = null;
        do {
            int next = parser.next();
            type = next;
            if (next == 1 || type == 2) {
                String nodeName = parser.getName();
            }
            int next2 = parser.next();
            type = next2;
            break;
        } while (type == 2);
        String nodeName2 = parser.getName();
        if (KeyChain.EXTRA_ALIAS.equals(nodeName2)) {
            int outerDepth = parser.getDepth();
            while (true) {
                int next3 = parser.next();
                int type2 = next3;
                if (next3 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                    return intent;
                }
                if (!(type2 == 3 || type2 == 4)) {
                    if ("intent".equals(parser.getName())) {
                        Intent gotIntent = Intent.parseIntent(getResources(), parser, attrs);
                        if (intent == null) {
                            intent = gotIntent;
                        }
                    } else {
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
            return intent;
        }
        throw new RuntimeException("Alias meta-data must start with <alias> tag; found" + nodeName2 + " at " + parser.getPositionDescription());
    }
}
