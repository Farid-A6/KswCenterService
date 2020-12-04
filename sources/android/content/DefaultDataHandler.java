package android.content;

import android.net.Uri;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DefaultDataHandler implements ContentInsertHandler {
    private static final String ARG = "arg";
    private static final String COL = "col";
    private static final String DEL = "del";
    private static final String POSTFIX = "postfix";
    private static final String ROW = "row";
    private static final String SELECT = "select";
    private static final String URI_STR = "uri";
    private ContentResolver mContentResolver;
    private Stack<Uri> mUris = new Stack<>();
    private ContentValues mValues;

    public void insert(ContentResolver contentResolver, InputStream in) throws IOException, SAXException {
        this.mContentResolver = contentResolver;
        Xml.parse(in, Xml.Encoding.UTF_8, this);
    }

    public void insert(ContentResolver contentResolver, String in) throws SAXException {
        this.mContentResolver = contentResolver;
        Xml.parse(in, (ContentHandler) this);
    }

    private void parseRow(Attributes atts) throws SAXException {
        Uri uri;
        Uri uri2;
        String uriStr = atts.getValue("uri");
        if (uriStr != null) {
            uri = Uri.parse(uriStr);
            if (uri == null) {
                throw new SAXException("attribute " + atts.getValue("uri") + " parsing failure");
            }
        } else if (this.mUris.size() > 0) {
            String postfix = atts.getValue(POSTFIX);
            if (postfix != null) {
                uri2 = Uri.withAppendedPath((Uri) this.mUris.lastElement(), postfix);
            } else {
                uri2 = (Uri) this.mUris.lastElement();
            }
            uri = uri2;
        } else {
            throw new SAXException("attribute parsing failure");
        }
        this.mUris.push(uri);
    }

    private Uri insertRow() {
        Uri u = this.mContentResolver.insert((Uri) this.mUris.lastElement(), this.mValues);
        this.mValues = null;
        return u;
    }

    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        if (!ROW.equals(localName)) {
            if (COL.equals(localName)) {
                int attrLen = atts.getLength();
                if (attrLen == 2) {
                    String key = atts.getValue(0);
                    String value = atts.getValue(1);
                    if (key == null || key.length() <= 0 || value == null || value.length() <= 0) {
                        throw new SAXException("illegal attributes value");
                    }
                    if (this.mValues == null) {
                        this.mValues = new ContentValues();
                    }
                    this.mValues.put(key, value);
                    return;
                }
                throw new SAXException("illegal attributes number " + attrLen);
            } else if (DEL.equals(localName)) {
                Uri u = Uri.parse(atts.getValue("uri"));
                if (u != null) {
                    int attrLen2 = atts.getLength() - 2;
                    if (attrLen2 > 0) {
                        String[] selectionArgs = new String[attrLen2];
                        for (int i = 0; i < attrLen2; i++) {
                            selectionArgs[i] = atts.getValue(i + 2);
                        }
                        this.mContentResolver.delete(u, atts.getValue(1), selectionArgs);
                    } else if (attrLen2 == 0) {
                        this.mContentResolver.delete(u, atts.getValue(1), (String[]) null);
                    } else {
                        this.mContentResolver.delete(u, (String) null, (String[]) null);
                    }
                } else {
                    throw new SAXException("attribute " + atts.getValue("uri") + " parsing failure");
                }
            } else {
                throw new SAXException("unknown element: " + localName);
            }
        } else if (this.mValues != null) {
            if (!this.mUris.empty()) {
                Uri nextUri = insertRow();
                if (nextUri != null) {
                    this.mUris.pop();
                    this.mUris.push(nextUri);
                    parseRow(atts);
                    return;
                }
                throw new SAXException("insert to uri " + ((Uri) this.mUris.lastElement()).toString() + " failure");
            }
            throw new SAXException("uri is empty");
        } else if (atts.getLength() == 0) {
            this.mUris.push((Uri) this.mUris.lastElement());
        } else {
            parseRow(atts);
        }
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        if (!ROW.equals(localName)) {
            return;
        }
        if (!this.mUris.empty()) {
            if (this.mValues != null) {
                insertRow();
            }
            this.mUris.pop();
            return;
        }
        throw new SAXException("uri mismatch");
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String name) throws SAXException {
    }

    public void startDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }
}
