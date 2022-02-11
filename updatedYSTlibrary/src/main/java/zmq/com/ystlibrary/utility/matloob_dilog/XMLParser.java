package zmq.com.ystlibrary.utility.matloob_dilog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import zmq.com.ystlibrary.model.StoryXML;


public class XMLParser {
    public static  StoryXML xlmParsing(InputStream is) {
        StoryXML storyXML = null;
        String text = "";
//        File sdcard = Environment.getExternalStorageDirectory();
//        File xmlFile = new File(sdcard.getAbsolutePath() + "/" + Utility.FOLDER_NAME + "/" + Utility.SubFolderName + "/" + filePath);
        try {
//            InputStream is = new FileInputStream(xmlFile);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("story")) {
                            storyXML = new StoryXML();
                        }else if(tagname.equalsIgnoreCase("languages")){
                            storyXML.languages = new ArrayList<>();
                            parseLanguages(parser,storyXML.languages);
                        }else if(tagname.equalsIgnoreCase("device")){
                            storyXML.device = new StoryXML.Device();
                            parseDevice(parser,storyXML.device);
                        }else if(tagname.equalsIgnoreCase("characters")){
                            storyXML.characters = new ArrayList<>();
                            parseCharacters(parser,storyXML.characters);
                        }else if(tagname.equalsIgnoreCase("backgrounds")){
                            storyXML.backgrounds = new ArrayList<>();
                            parseBackgrounds(parser,storyXML.backgrounds);
                        }else if(tagname.equalsIgnoreCase("audios")){
                            storyXML.audios = new ArrayList<>();
                            parseAudios(parser,storyXML.audios);
                        }else if(tagname.equalsIgnoreCase("scenes")){
                            storyXML.scenes = new ArrayList<>();
                            parseScenes(parser,storyXML.scenes);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("id")) {
                            storyXML.id = text;
                        }else if (tagname.equalsIgnoreCase("name")) {
                            storyXML.name = text;
                        }else if (tagname.equalsIgnoreCase("version")) {
                            storyXML.version = text;
                        }else if (tagname.equalsIgnoreCase("status")) {
                            storyXML.status = text;
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {
            e.printStackTrace();
        }
        return storyXML;
    }




    private static void parseLanguages(XmlPullParser parser, ArrayList<StoryXML.Language> languages) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    break;

                case XmlPullParser.TEXT:
                    break;

                case XmlPullParser.END_TAG:
                    if(tagname.equalsIgnoreCase("languages")){
                        exit = true;
                    }
                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }

    private static void parseDevice(XmlPullParser parser, StoryXML.Device device) throws XmlPullParserException, IOException  {
        int event = parser.getEventType();
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    break;

                case XmlPullParser.TEXT:
                    break;

                case XmlPullParser.END_TAG:
                    if(tagname.equalsIgnoreCase("device")){
                        exit = true;
                    }
                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }

    private static void parseCharacters(XmlPullParser parser, ArrayList<StoryXML.Character> characters) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        String text = null;
        StoryXML.Character character = null;
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(tagname.equalsIgnoreCase("character")){
                        character = new StoryXML.Character();
                    }else if(tagname.equalsIgnoreCase("expressions")){
                        character.expressions = new ArrayList<>();
                        parseExpressions(parser,character.expressions);
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("id")) {
                        character.id = text;
                    }else if (tagname.equalsIgnoreCase("imagepath")) {
                        character.imagepath = text;
                        character.name = text.split("/")[text.split("/").length-1];
                    }else if (tagname.equalsIgnoreCase("name")) {
                        //character.name = text;
                    }else if (tagname.equalsIgnoreCase("size")) {
                        character.size = text;
                    }else if(tagname.equalsIgnoreCase("character")){
                        characters.add(character);
                    }else if(tagname.equalsIgnoreCase("characters")){
                        exit = true;
                    }
                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }

    private static void parseExpressions(XmlPullParser parser, ArrayList<StoryXML.Expression> expressions) throws XmlPullParserException, IOException  {
        int event = parser.getEventType();
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    break;

                case XmlPullParser.TEXT:
                    break;

                case XmlPullParser.END_TAG:
                    if(tagname.equalsIgnoreCase("expressions")){
                        exit = true;
                    }
                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }


    private static void parseBackgrounds(XmlPullParser parser, ArrayList<StoryXML.Background> backgrounds) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        String text = null;
        StoryXML.Background background = null;
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(tagname.equalsIgnoreCase("background")){
                        background = new StoryXML.Background();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("id")) {
                        background.id = text;
                    }else if (tagname.equalsIgnoreCase("imagepath")) {
                        background.imagepath = text;
                        background.name = text.split("/")[text.split("/").length-1];
                    } else if (tagname.equalsIgnoreCase("name")) {
//                        background.name = text;
                    }else if (tagname.equalsIgnoreCase("size")) {
                        background.size = text;
                    }else if (tagname.equalsIgnoreCase("layout")) {
                        background.layout = text;
                    }else if(tagname.equalsIgnoreCase("background")) {
                        backgrounds.add(background);
                    }else if(tagname.equalsIgnoreCase("backgrounds")){
                        exit = true;
                    }

                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }


    private static void parseAudios(XmlPullParser parser, ArrayList<StoryXML.Audio> audios) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        String text = null;
        StoryXML.Audio audio = null;
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(tagname.equalsIgnoreCase("audio")){
                        audio = new StoryXML.Audio();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("id")) {
                        audio.id = text;
                    }else if (tagname.equalsIgnoreCase("audiopath")) {
                        audio.audiopath = text;
                        audio.name = text.split("/")[text.split("/").length-1];
                    } else if (tagname.equalsIgnoreCase("name")) {
//                        audio.name = text;
                    }else if (tagname.equalsIgnoreCase("size")) {
                        audio.size = text;
                    }else if(tagname.equalsIgnoreCase("audio")) {
                        audios.add(audio);
                    }else if(tagname.equalsIgnoreCase("audios")){
                        exit = true;
                    }

                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }


    private static void parseScenes(XmlPullParser parser, ArrayList<StoryXML.Scene> scenes) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        String text = null;
        StoryXML.Scene scene = null;
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(tagname.equalsIgnoreCase("scene")){
                        scene = new StoryXML.Scene();
                    }else if(tagname.equalsIgnoreCase("elements")){
                        scene.elements = new ArrayList<>();
                        parseElement(parser,scene.elements);
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("id")) {
                        scene.id = text;
                    }else if (tagname.equalsIgnoreCase("name")) {
                        scene.name = text;
                    }else if (tagname.equalsIgnoreCase("background")) {
                        scene.background = text;
                    }else if (tagname.equalsIgnoreCase("layout")) {
                        scene.layout = text;
                    }else if (tagname.equalsIgnoreCase("type")) {
                        scene.type = text;
                    }else if(tagname.equalsIgnoreCase("scene")) {
                        scenes.add(scene);
                    } else if(tagname.equalsIgnoreCase("scenes")){
                        exit = true;
                    }

                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }

    private static void parseElement(XmlPullParser parser, ArrayList<StoryXML.Elements> elements) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        String text = null;
        StoryXML.Elements element = null;
        boolean exit = false;
        while (true){
            String tagname = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(tagname.equalsIgnoreCase("element")){
                        element = new StoryXML.Elements();
                    }else if(tagname.equalsIgnoreCase("properties")){
                        element.properties = new StoryXML.Property();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("id")) {
                        element.id = text;
                    }else if (tagname.equalsIgnoreCase("sequence")) {
                        element.sequence = text;
                    }else if (tagname.equalsIgnoreCase("type")) {
                        element.type = text;
                    }else if (tagname.equalsIgnoreCase("message")) {
                        element.properties.message = text;
                    }else if (tagname.equalsIgnoreCase("audio_id")) {
                        element.properties.audio_id = text;
                    }else if (tagname.equalsIgnoreCase("character")) {
                        element.properties.character = text;
                    }else if (tagname.equalsIgnoreCase("x")) {
                        element.properties.x = text;
                    }else if (tagname.equalsIgnoreCase("y")) {
                        element.properties.y = text;
                    }else if (tagname.equalsIgnoreCase("z")) {
                        element.properties.z = text;
                    }else if (tagname.equalsIgnoreCase("dilog")) {
                        element.properties.dilog = text;
                    }else if (tagname.equalsIgnoreCase("question")) {
                        element.properties.question = text;
                    }else if (tagname.equalsIgnoreCase("correct")) {
                        element.properties.correct = text;
                    }else if (tagname.equalsIgnoreCase("incorrect")) {
                        element.properties.incorrect = text;
                    }else if (tagname.equalsIgnoreCase("q_audio_id")) {
                        element.properties.q_audio_id = text;
                    }else if (tagname.equalsIgnoreCase("a_audio_id")) {
                        element.properties.a_audio_id = text;
                    }else if (tagname.equalsIgnoreCase("w_audio_id")) {
                        element.properties.w_audio_id = text;
                    }else if(tagname.equalsIgnoreCase("element")) {
                        elements.add(element);
                    }else if(tagname.equalsIgnoreCase("elements")){
                        exit = true;
                    }

                    break;
            }
            if(exit){
                break;
            }else{
                event = parser.next();
            }
        }
    }
}
