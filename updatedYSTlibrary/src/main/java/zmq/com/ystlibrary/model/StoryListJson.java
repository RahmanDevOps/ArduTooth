package zmq.com.ystlibrary.model;

import java.util.List;

public class StoryListJson {
    public boolean status;
    public String message;
    public List<Data> data;

    public class Data {
        public boolean status;
        public String zipurl;
        public String author_name;
        public String category;
        public String created_by;
        public String end_reached;
        public String english_name;
        public String icon_url;
        public String language_version;
        public String question_answer_reached;
        public String rating;
        public String releasig_date;
        public String start_reached;
        public String story_id;
        public String story_name;
        public String version;
        public String view;
        public String youtube_video_url;
        public List<Language> languages;
    }

    public class Language {
        public String create_time;
        public String id;
        public String language_code;
        public String language_name;
        public String status;
    }
}
