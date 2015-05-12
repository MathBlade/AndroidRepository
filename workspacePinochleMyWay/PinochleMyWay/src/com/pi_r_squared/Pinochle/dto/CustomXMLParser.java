package com.pi_r_squared.Pinochle.dto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;


//borrowed and tweaked from http://developer.android.com/training/basics/network-ops/xml.html#read
public class CustomXMLParser {
    // We don't use namespaces
    private final String ns = null;
   
    public List<Setting> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    
    
    
    
    
    private List<Setting> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Setting> settings = new ArrayList<Setting>();

        parser.require(XmlPullParser.START_TAG, ns, "settingsList");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("setting")) {
                settings.add(readEntry(parser));
            }
        }  
        return settings;
    }
    
    private Setting readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "setting");
        String nameOfSetting = null;
        List<String> settingValues = null;
        String type = null;
        List<String> possibleValues = null;
        String description = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                nameOfSetting = readName(parser);    
            }
            else if(name.equals("description")){
            	description = readDescription(parser);
            }
            else if (name.equals("valuesList")) {
                settingValues = readSettingsValues(parser);
            }
            else if(name.equals("type")){
            	type = readType(parser);
            }
            else if(name.equals("possibleValues")){
            	possibleValues = readPossibleValues(parser);
            }
         
            
        }
        return new Setting(nameOfSetting, description, settingValues, type, possibleValues);
    }
    
    private List<String> readSettingsValues(XmlPullParser parser) throws XmlPullParserException, IOException {
	
    	List<String> settingValues = new ArrayList<String>();
    	parser.require(XmlPullParser.START_TAG, ns, "valuesList");
    	while(parser.nextTag() == XmlPullParser.START_TAG) {
    	   
    		if (parser.getName().equals("value")){
    			settingValues.add(readASetting(parser));
    		}
    		else {
    			break;
    		}
    	}
    	
        return settingValues;
	}

    private List<String> readPossibleValues(XmlPullParser parser) throws XmlPullParserException, IOException {
	
    	List<String> possibleValues = new ArrayList<String>();
    	parser.require(XmlPullParser.START_TAG, ns, "possibleValues");
    	while(parser.nextTag() == XmlPullParser.START_TAG) {
    	   
    		if (parser.getName().equals("value")){
    			possibleValues.add(readAPossibleValue(parser));
    		}
    		else {
    			break;
    		}
    	}
    	
        return possibleValues;
	}
   
    private String readAPossibleValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "value");
        String possibleValue = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "value");
        return possibleValue;
    }
    
    private String readASetting(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "value");
        String settingValue = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "value");
        return settingValue;
    }
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
	private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String nameOfSetting = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return nameOfSetting;
    }
	private String readType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "type");
        String nameOfSetting = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "type");
        return nameOfSetting;
    }
    
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    

   /* private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
    */
    

}
