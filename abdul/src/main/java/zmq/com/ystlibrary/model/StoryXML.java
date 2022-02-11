
package zmq.com.ystlibrary.model;
/*
package zmq.com.ystlibrary.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "story",strict = false)
public class StoryXML {
    @Element(name = "id")
    public String id;
    @Element(name = "name")
    public String name;
    @Element(name = "writer")
    public String writer;
    @Element(name = "category")
    public String category;
    @Element(name = "version")
    public String version;
    @Element(name = "status")
    public String status;
    @Element(name = "approved")
    public String approved;
    @Element(name = "access")
    public String access;
    @Element(name = "hits")
    public String hits;

    @ElementList(required = false)
    public ArrayList<Language> languages;
    @Element
    public Device device;
    @ElementList(required = false)
    public ArrayList<Character> characters;
    @ElementList(required = false)
    public ArrayList<Background> backgrounds;
    @ElementList(required = false)
    public ArrayList<Audio> audios;

    @ElementList(required = false)
    public ArrayList<Scene> scenes;

    @Root(name = "language")
    public static class Language{
        @Element(name = "id")
        public String id;
        @Element(name = "name")
        public String name;
        @Element(name = "code")
        public String code;

    }

    @Root(name = "device")
    public static class Device{
        @Element(name = "width")
        public String width;
        @Element(name = "height")
        public String height;
    }

    @Root(name = "character")
    public static class Character{
        @Element(name = "id")
        public String id;
        @Element(name = "imagepath")
        public String imagepath;
        @Element(name = "name")
        public String name;
        @Element(name = "size")
        public String size;
        @Element(name = "res_type")
        public String res_type;
        @Element(name = "used_flag")
        public String used_flag;

        @ElementList(required = false)
        public ArrayList<Expression> expressions;
    }

    @Root(name = "expression")
    public static class Expression{
        @Element(name = "id")
        public String id;
        @Element(name = "imagepath")
        public String imagepath;
        @Element(name = "size")
        public String size;
    }

    @Root(name = "background")
    public static class Background{
        @Element(name = "id")
        public String id;
        @Element(name = "imagepath")
        public String imagepath;
        @Element(name = "name")
        public String name;
        @Element(name = "layout")
        public String layout;
        @Element(name = "size")
        public String size;
        @Element(name = "used_flag")
        public String used_flag;
    }

    @Root(name = "audio")
    public static class Audio{
        @Element(name = "id")
        public String id;
        @Element(name = "audiopath")
        public String audiopath;
        @Element(name = "name")
        public String name;
        @Element(name = "size")
        public String size;
        @Element(name = "used_flag")
        public String used_flag;
    }

    @Root(name = "scene")
    public static class Scene{
        @Element(name = "id")
        public String id;
        @Element(name = "name")
        public String name;
        @Element(name = "background")
        public String background;
        @Element(name = "layout")
        public String layout;
        @Element(name = "sequence")
        public String sequence;
        @Element(name = "type")
        public String type;

        @ElementList(required = false)
        public ArrayList<Elements> elements;
    }

    @Root(name = "element")
    public static class Elements{
        @Element(name = "id")
        public String id;
        @Element(name = "sequence")
        public String sequence;
        @Element(name = "delay")
        public String delay;
        @Element(name = "type")
        public String type;

        @Element(required = false)
        public Property properties;
    }

    @Root(name = "properties")
    public static class Property{
        @Element(name = "message",required = false)
        public String message;
        @Element(name = "audio_id",required = false)
        public String audio_id;
        @Element(name = "next-scene",required = false)
        public String next_scene;
        @Element(name = "character",required = false)
        public String character;
        @Element(name = "expression",required = false)
        public String expression;
        @Element(name = "animation",required = false)
        public String animation;
        @Element(name = "x",required = false)
        public String x;
        @Element(name = "y",required = false)
        public String y;
        @Element(name = "z",required = false)
        public String z="0";
        @Element(name = "dilog",required = false)
        public String dilog;

        @Element(name = "question",required = false)
        public String question;
        @Element(name = "q_type",required = false)
        public String q_type;
        @Element(name = "correct",required = false)
        public String correct;
        @Element(name = "incorrect",required = false)
        public String incorrect;
        @Element(name = "q_audio_id",required = false)
        public String q_audio_id;
        @Element(name = "a_audio_id",required = false)
        public String a_audio_id;
        @Element(name = "w_audio_id",required = false)
        public String w_audio_id;
    }

}

*/


import java.util.ArrayList;


public class StoryXML {
    public String id;
    public String name;
    public String writer;
    public String category;
    public String version;
    public String status;
    public String approved;
    public String access;
    public String hits;
    public ArrayList<Language> languages;
    public Device device;
    public ArrayList<Character> characters;
    public ArrayList<Background> backgrounds;
    public ArrayList<Audio> audios;
    public ArrayList<Scene> scenes;


    public static class Language{
        public String id;
        public String name;
        public String code;

    }

    public static class Device{
        public String width;
        public String height;
    }

    public static class Character{
        public String id;
        public String imagepath;
        public String name;
        public String size;
        public String res_type;
        public String used_flag;
        public ArrayList<Expression> expressions;
    }

    public static class Expression{
        public String id;
        public String imagepath;
        public String size;
    }


    public static class Background{
        public String id;
        public String imagepath;
        public String name;
        public String layout;
        public String size;
        public String used_flag;
    }

    public static class Audio{
        public String id;
        public String audiopath;
        public String name;
        public String size;
        public String used_flag;
    }

    public static class Scene{
        public String id;
        public String name;
        public String background;
        public String layout;
        public String sequence;
        public String type;

        public ArrayList<Elements> elements;
    }


    public static class Elements{
        public String id;
        public String sequence;
        public String delay;
        public String type;

        public Property properties;
    }

    public static class Property{
        public String message;
        public String audio_id;
        public String next_scene;
        public String character;
        public String expression;
        public String animation;
        public String x;
        public String y;
        public String z="0";
        public String dilog;
        public String question;
        public String q_type;
        public String correct;
        public String incorrect;
        public String q_audio_id;
        public String a_audio_id;
        public String w_audio_id;
    }

}

