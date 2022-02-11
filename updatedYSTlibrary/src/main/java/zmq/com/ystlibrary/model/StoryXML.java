
package zmq.com.ystlibrary.model;
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

